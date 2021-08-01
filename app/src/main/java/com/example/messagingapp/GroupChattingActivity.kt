package com.example.messagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messagingapp.ModelClass.MessageModel
import com.example.messagingapp.adapter.MessageAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_group_chatting.*


class GroupChattingActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var messagelist :ArrayList<MessageModel>
    private lateinit var adapter: MessageAdapter
    private lateinit var datastorei: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chatting)

        database = FirebaseDatabase.getInstance()
        datastorei = FirebaseStorage.getInstance().reference

        sendbtngrp.setOnClickListener {

        }

    }
}