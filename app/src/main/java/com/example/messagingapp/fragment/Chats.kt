package com.example.messagingapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.messagingapp.ModelClass.User
import com.example.messagingapp.R
import com.example.messagingapp.adapter.Useradapetr
import com.example.messagingapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Chats : Fragment() {

    lateinit var userViewModel: UserViewModel
    lateinit var userrecycl : ShimmerRecyclerView
    private lateinit var database: FirebaseDatabase

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val c = inflater.inflate(R.layout.fragment_chats, container, false)
        database = FirebaseDatabase.getInstance()
         userrecycl = c.findViewById(R.id.userrecycler)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.userdata().observe(this, Observer {
                        Log.d("List", it.size.toString())
            userrecycl.showShimmerAdapter()
            val adapter = Useradapetr(requireContext(),it)
            userrecycl.adapter = adapter
            userrecycl.hideShimmerAdapter()
            userrecycl.layoutManager = LinearLayoutManager(requireContext())

        })

        return c
    }

    override fun onResume() {
        database.getReference("presence").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue("online")
        super.onResume()
    }
    override fun onPause() {
        database.getReference("presence").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue("offline")
        super.onPause()
    }
}
