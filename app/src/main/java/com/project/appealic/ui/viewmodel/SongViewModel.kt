package com.project.appealic.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.project.appealic.data.model.Album
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.UserEntity
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.CompletableFuture


class SongViewModel(private val songRepository: SongRepository, private val userRepository: UserRepository) : ViewModel() {


    private val _likedSongs = MutableLiveData<List<Track>>()
    val likedSongs: LiveData<List<Track>> get() = _likedSongs

    private val _gerneTracks = MutableLiveData<List<Track>>()
    val gerneTracks: LiveData<List<Track>> get() = _gerneTracks

    private val _recentTrack = MutableLiveData<List<Track>>()
    val recentTrack: LiveData<List<Track>> get() = _recentTrack

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _newReleaseTracks = MutableLiveData<List<Track>>()
    val newReleaseTracks: LiveData<List<Track>> get() = _newReleaseTracks

    private val _recTracks = MutableLiveData<List<Track>>()
    val recTracks: LiveData<List<Track>> get() = _recTracks

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums
    private val firebaseDB = Firebase.firestore




    fun getAllTracks(){
        songRepository.getAllTrack()
            .addOnSuccessListener { tracks ->
                    if(tracks != null) _tracks.postValue(tracks.toObjects(Track::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error",exception.toString())
            }
    }
    fun getNewReleaseTracks(){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        songRepository.getAllTrack().addOnSuccessListener { tracks ->
            val tracks = tracks.documents.mapNotNull { track ->
                track.toObject(Track::class.java)
            }
            val sortedTracks =  tracks.sortedByDescending { track->
                track.releaseDate?.let { dateFormat.parse(it) }
            }
            _newReleaseTracks.postValue(sortedTracks.take(6))
        }.addOnFailureListener { exception ->
            Log.e("error",exception.toString())}
    }


    fun recommendSong(tracks: List<Track>) {
        _recTracks.postValue(emptyList())
        // Bước 1: Thu thập dữ liệu
        val sourceLiveData = tracks
        val allTracks = mutableListOf<Track>()
        // Bước 2: Xác định tất cả các bài hát có sẵn và bài hát được truyền vào
        _tracks.value?.let { allTracks.addAll(it) }
        allTracks.addAll(sourceLiveData)

        // Bước 3: Lọc các bài hát không trùng lặp
        val uniqueSongs = allTracks.distinctBy { it.trackId }

        // Bước 4: Loại bỏ các bài hát đã có sẵn từ danh sách bài hát đề xuất
        val filteredRecommendations = uniqueSongs.filter { song ->
            !tracks.any { it.trackId == song.trackId }
        }
        // Bước 5: Đề xuất bài hát mới
        val recommendedSongs = if (filteredRecommendations.size <= 10) {
            filteredRecommendations
        } else {
            val shuffledUniqueSongs = filteredRecommendations.shuffled()
            shuffledUniqueSongs.take(10)
        }
        // Đưa danh sách bài hát đề xuất vào LiveData _recTracks để cập nhật giao diện người dùng
        _recTracks.postValue(recommendedSongs)
    }

    fun getAllArtists() {
        songRepository.getAllArtist()
            .addOnSuccessListener { artists ->
                if (artists != null)
                    _artists.postValue(artists.toObjects(Artist::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }

    fun getAllPlaylists() {
        songRepository.getAllPlaylists()
            .addOnSuccessListener { playlists ->
                if (playlists != null)
                    _playlists.postValue(playlists.toObjects(Playlist::class.java))
            }
            .addOnFailureListener { exception ->
                Log.e("error", exception.toString())
            }
    }




    fun getTrackFromGenres(genre: String) {
        songRepository.getAllTrack()
            .addOnSuccessListener { tracks ->
                val genreTrack =
                    tracks.documents.filter { it.toObject(Track::class.java)?.genre == genre }
                _gerneTracks.postValue(genreTrack.map { it.toObject(Track::class.java)!! })
            }
    }

    fun getTrackByUrl(trackUrl : String) {
        songRepository.getTrackByUrl(trackUrl)
            .addOnSuccessListener { track->
                if (track != null)
                    _recentTrack.postValue(track.toObjects(Track::class.java))
            }
    }

    fun getUserData(): LiveData<UserEntity>? {
        return userRepository.getUserData()
    }

    fun getRecentSongs(userId: String): LiveData<List<SongEntity>> {
        return songRepository.getRecentSongs(userId)
    }

    fun insertSong(song: SongEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            songRepository.insertSong(song)
        }
    }

    fun updateTracks(tracks: List<Track>) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _tracks.value = tracks
            }
        }
    }

    fun getLikedSongs(userId: String) {
        songRepository.getLikedSongFromUser(userId)
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val favoriteSongIds = userDoc["likedSong"] as? List<String> ?: emptyList()

                    val tasks = favoriteSongIds.map { trackId ->
                        val trackDocRef = firebaseDB.collection("tracks").document(trackId)
                        trackDocRef.get().continueWith { task ->
                            if (task.isSuccessful) {
                                task.result?.toObject(Track::class.java)
                            } else {
                                null
                            }
                        }
                    }

                    Tasks.whenAllSuccess<Track?>(tasks).addOnSuccessListener { documents ->
                        val tracksList = documents.filterNotNull()
                        // Xử lý danh sách trackList ở đây
                        _likedSongs.postValue(tracksList)
                    }.addOnFailureListener { exception ->
                        Log.e(TAG, "Error fetching liked songs: $exception")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching user document: $exception")
            }
    }

    fun addTrackToUserLikedSongs(userId: String, trackId: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            songRepository.addTrackToUserLikedSongs(userId, trackId)
        }
    }

    fun removeTrackFromUserLikedSongs(userId: String, trackId: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            songRepository.removeTrackFromUserLikedSongs(userId, trackId)
        }
    }


    fun SearchSongResults(searchQuery: String?) {
        // Gọi phương thức trong Repository để tải dữ liệu từ Firebase dựa trên searchQuery
        val searchResultsLiveData = songRepository.loadSongSearchResults(searchQuery)
        // Cập nhật LiveData _tracks với dữ liệu mới
        searchResultsLiveData.observeForever { tracks ->
            _tracks.postValue(tracks) }
}
}