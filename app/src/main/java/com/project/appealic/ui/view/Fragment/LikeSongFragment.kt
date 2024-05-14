package com.project.appealic.ui.view.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.repository.SongRepository
import com.project.appealic.data.repository.UserRepository
import com.project.appealic.databinding.FragmentLikedSongBinding
import com.project.appealic.ui.view.Adapters.LikedSongsAdapter
import com.project.appealic.ui.view.setOnItemClickListener
import com.project.appealic.ui.viewmodel.SongViewModel
import com.project.appealic.utils.SongViewModelFactory

class LikeSongFragment : Fragment() {
    private lateinit var binding: FragmentLikedSongBinding
    private lateinit var adapter: LikedSongsAdapter
    private lateinit var songViewModel: SongViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songRepository = SongRepository(requireContext().applicationContext)
        val userRepository = UserRepository(requireContext().applicationContext)
        val factory = SongViewModelFactory(songRepository, userRepository)
        songViewModel = ViewModelProvider(this, factory)[SongViewModel::class.java]

        adapter = LikedSongsAdapter(requireContext(), ArrayList())
        binding.lvLikedSongs.adapter = adapter

        songViewModel.likedSongs.observe(viewLifecycleOwner, Observer {  songs ->
            adapter.clear()
            adapter.addAll(songs)
            adapter.notifyDataSetChanged()
            binding.lvLikedSongs.setOnItemClickListener(requireContext(), songViewModel, songs)
        })
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            songViewModel.getLikedSongs(currentUserId)
        }

        binding.imvBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}