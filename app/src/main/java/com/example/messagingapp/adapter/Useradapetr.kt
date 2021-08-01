package com.example.messagingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messagingapp.MessageActivity
import com.example.messagingapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.user_list.view.*

class Useradapetr(val context : Context , val Userlist:ArrayList<com.example.messagingapp.ModelClass.User>):RecyclerView.Adapter<Useradapetr.userviewholder>() {


    inner class userviewholder(val userview:View): RecyclerView.ViewHolder(userview){

        init {

            userview.setOnClickListener {
                val user1 = Userlist[adapterPosition]
                val itent = Intent(context,MessageActivity::class.java)
                itent.putExtra("username",user1.Name)
                itent.putExtra("userid",user1.userid)
                itent.putExtra("userimage",user1.UserImage)
                context.startActivity(itent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_list,parent,false)
        return userviewholder(view)
    }

    override fun onBindViewHolder(holder: userviewholder, position: Int) {


        val user = Userlist[position]
        val senderid = FirebaseAuth.getInstance().currentUser!!.uid
        val senderroom = "$senderid${user.userid}"

        FirebaseDatabase.getInstance().getReference("Chatting").child(senderroom).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()){
                   val lastmsg = snapshot.child("lastMsg").value
                   val lastmsgtime = snapshot.child("lastMsgtime").value
                   holder.userview.userlastmsg.text = lastmsg.toString()
               }
                else{
                   holder.userview.userlastmsg.text = "Tap to chat"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        holder.userview.username.text = user.Name
        Glide.with(context).load(user.UserImage).into(holder.userview.userimage)
    }

    override fun getItemCount(): Int {
        return Userlist.size
    }
}