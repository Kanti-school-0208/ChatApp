package com.example.messagingapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messagingapp.MainActivity
import com.example.messagingapp.ModelClass.UserstatusModel
import com.example.messagingapp.R
import com.example.messagingapp.fragment.Status
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.status_file.view.*
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory
import java.util.concurrent.TimeUnit


class StatusAdapter(val context: Context, val userstatuslist: ArrayList<UserstatusModel>) : RecyclerView.Adapter<StatusAdapter.statusviewholder>() {

    inner class statusviewholder(val statusview: View): RecyclerView.ViewHolder(statusview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): statusviewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.status_file, parent, false)
        return statusviewholder(view)
    }

    override fun onBindViewHolder(holder: statusviewholder, position: Int) {

        val userstatus = userstatuslist[position]

        val laststatus = userstatus.Statuslist!![userstatus.Statuslist!!.size - 1]
        Glide.with(context).load(laststatus.statusimage).into(holder.statusview.statusimage)
        holder.statusview.circular_status_view.setPortionsCount(userstatus.Statuslist!!.size)
        holder.statusview.statususername.text = userstatus.susername

        holder.statusview.circular_status_view.setOnClickListener {
            val myStories: ArrayList<MyStory> = ArrayList()

            for (i in userstatus.Statuslist!!){
                myStories.add(MyStory(i.statusimage))
            }
            StoryView.Builder((context as MainActivity).supportFragmentManager)
                .setStoriesList(myStories) // Required
                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                .setTitleText(userstatus.susername) // Default is Hidden
                .setSubtitleText("") // Default is Hidden
                .setTitleLogoUrl(userstatus.suserimage) // Default is Hidden
                .setStoryClickListeners(object : StoryClickListeners {
                    override fun onDescriptionClickListener(position: Int) {
                        //your action
                    }

                    override fun onTitleIconClickListener(position: Int) {
                        //your action
                    }
                }) // Optional Listeners
                .build() // Must be called before calling show method
                .show()
        }

    }

    override fun getItemCount(): Int {
        Log.d("NumberStatus","${ userstatuslist.size}")
        return userstatuslist.size
    }
}