package com.example.messagingapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.messagingapp.ModelClass.MessageModel
import com.example.messagingapp.adapter.MessageAdapter
import com.example.messagingapp.hepler.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var messagelist :ArrayList<MessageModel>
    private lateinit var datastorei: StorageReference
    private lateinit var senderid : String
    private lateinit var recieverid : String
    private lateinit var senderroom : String
    private lateinit var recieverroom : String
    private lateinit var date : Date
    private lateinit var Typingstatus : TextView
    private lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setSupportActionBar(toolbarc)
         Typingstatus = findViewById(R.id.typingstatus)

        database = FirebaseDatabase.getInstance()
        datastorei = FirebaseStorage.getInstance().reference
        val username = intent.extras!!.getString("username")
         recieverid = intent.extras!!.getString("userid")!!
        val userphoto = intent.extras!!.getString("userimage")
        Glide.with(this).load(userphoto).placeholder(R.drawable.ic_baseline_image_24).into(chatpimage)
        //supportActionBar!!.title = username
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        friendname.text = username

         senderid = FirebaseAuth.getInstance().currentUser!!.uid
         senderroom = "$senderid$recieverid"
         recieverroom = "$recieverid$senderid"
         date = Date()

        database.getReference("presence").child(recieverid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val status: String = snapshot.value.toString()
                    if (status.isNotEmpty()){
                        if (status == "offline"){
                            Typingstatus.visibility = View.GONE
                        }
                        else{
                            Typingstatus.text = status.toString()
                            Typingstatus.visibility = View.VISIBLE
                        }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        attchfile.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "*/*"
            startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 140)
        }

        val handler = Handler()
        chattext.addTextChangedListener(object : TextWatcher{
            val userstoptyping = object : Runnable{
                override fun run() {
                    database.getReference("presence").child(senderid).setValue("online")
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                database.getReference("presence").child(senderid).setValue("typing...")
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userstoptyping,1000)
            }


        })

        sendbtn.setOnClickListener {
            val randomid = database.reference.push().key
            val sendermsg = chattext.text.toString().trim()

            val lastmsg = hashMapOf("lastMsg" to sendermsg, "lastMsgtime" to date.time)
            database.getReference("Chatting").
            child(senderroom).updateChildren(lastmsg as Map<String, Any>)

            database.getReference("Chatting").
            child(recieverroom).updateChildren(lastmsg as Map<String, Any>)

            database.getReference("Chatting").
            child(senderroom).child("Message").
            child(randomid.toString()).setValue(MessageModel(sendermsg,senderid,-1,date.time))
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        database.getReference("Chatting").
                        child(recieverroom).child("Message").
                        child(randomid.toString()).setValue(MessageModel(sendermsg,senderid,-1,date.time))

                    }
                    else
                        Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            chattext.text = null
        }

        messagelist = ArrayList()
        database.getReference("Chatting").
        child(senderroom).child("Message")
            .addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messagelist.clear()
                for(i in snapshot.children){
                    val msg = i.getValue(MessageModel::class.java)
                    msg!!.messageid = i.key
                   messagelist.add(msg)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity,error.message,Toast.LENGTH_SHORT).show()
            }
        })
         adapter = MessageAdapter(this,messagelist,senderroom,recieverroom)
        chatrecycler.adapter = adapter
        chatrecycler.layoutManager = LinearLayoutManager(this)





    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 140 && resultCode == Activity.RESULT_OK && data != null) {
            val selecteditem = data.data
            val calender = Calendar.getInstance()

            val datastore1: StorageReference = datastorei.child("usercontent").child(calender.timeInMillis.toString() + "")
            datastore1.putFile(selecteditem!!).addOnSuccessListener {
                datastore1.downloadUrl
                    .addOnSuccessListener {
                        val randomid = database.reference.push().key
                        val sendermsg = "photo"
                        val imageurl = it.toString()

                        val lastmsg = hashMapOf("lastMsg" to sendermsg, "lastMsgtime" to date.time)
                        database.getReference("Chatting").
                        child(senderroom).updateChildren(lastmsg as Map<String, Any>)

                        database.getReference("Chatting").
                        child(recieverroom).updateChildren(lastmsg as Map<String, Any>)

                        database.getReference("Chatting").
                        child(senderroom).child("Message").
                        child(randomid.toString()).setValue(MessageModel(sendermsg,senderid,-1,date.time,imageurl))
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    database.getReference("Chatting").
                                    child(recieverroom).child("Message").
                                    child(randomid.toString()).setValue(MessageModel(sendermsg,senderid,-1,date.time,imageurl))

                                }
                                else
                                    Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                            }
                    }
            }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
        }
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