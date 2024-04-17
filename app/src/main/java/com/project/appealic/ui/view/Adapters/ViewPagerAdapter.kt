package com.project.appealic.ui.view.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.appealic.ui.view.Fragment.InfoMusicFragment
import com.project.appealic.ui.view.Fragment.LyrisFragment
import com.project.appealic.ui.view.Fragment.PlaySongFragment

class ViewPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: List<Fragment> = listOf(
        InfoMusicFragment(),
        PlaySongFragment(),
        LyrisFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}