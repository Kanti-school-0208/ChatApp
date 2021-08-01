package com.example.messagingapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.messagingapp.fragment.Calls
import com.example.messagingapp.fragment.Chats
import com.example.messagingapp.fragment.Status

class ViewpagerAdapter(fragmentManager: FragmentManager ,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{Chats()}
            1 ->{Status()}
            2 ->{Calls()}
            else -> {Chats()}
        }
    }
}