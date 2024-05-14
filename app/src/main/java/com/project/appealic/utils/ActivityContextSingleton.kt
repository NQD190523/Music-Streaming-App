package com.project.appealic.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

object ActivityContextSingleton {
    private var activityHomeContext : FragmentActivity? = null

    fun setActivityHomeContext(fragmentActivity: FragmentActivity){
        activityHomeContext = fragmentActivity
    }
    fun getActivityHomeContext() : FragmentActivity?{
        return activityHomeContext
    }
}