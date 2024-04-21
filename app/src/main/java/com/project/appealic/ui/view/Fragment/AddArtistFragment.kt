package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.data.repository.ArtistRepository
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.ui.view.Adapters.ArtistAdapter
import com.project.appealic.ui.view.Adapters.ArtistResultAdapter
import com.project.appealic.ui.view.Adapters.FavouriteArtistAdapter
import com.project.appealic.ui.view.Adapters.NewReleaseAdapter
import com.project.appealic.ui.viewmodel.ArtistViewModel
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.ArtistViewModelFactory
import com.project.appealic.utils.SongViewModelFactory

class AddArtistFragment : Fragment(), FavouriteArtistAdapter.OnArtistClickListener {

    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var listViewArtists: ListView
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_favorite_artists, container, false)

        val factoryArtist = ArtistViewModelFactory(ArtistRepository(requireActivity().application))
        artistViewModel = ViewModelProvider(this, factoryArtist)[ArtistViewModel::class.java]

        listViewArtists = view.findViewById(R.id.lstAtirstFavourite)
        val adapter = FavouriteArtistAdapter(requireContext(), ArrayList(), artistViewModel, this)
        listViewArtists.adapter = adapter

        // Sử dụng artistViewModel để lấy danh sách các nghệ sĩ đã follow
        artistViewModel.likedArtist.observe(viewLifecycleOwner, Observer { likedArtists ->
            adapter.clear()
            adapter.addAll(likedArtists)
            adapter.notifyDataSetChanged()
        })

        // Gọi hàm để lấy danh sách các nghệ sĩ đã follow
        artistViewModel.getFollowArtistFromUser(userId)

        return view
    }


    override fun onArtistClick(artist: Artist) {
        // Chuyển sang trang artist profile và truyền thông tin về artist được chọn
        val artistProfileFragment = ArtistProfileFragment()
        val bundle = Bundle()
        bundle.putParcelable("selectedArtist", artist)
        artistProfileFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmenthome, artistProfileFragment)
            .addToBackStack(null)
            .commit()
    }

}
