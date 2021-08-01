package com.example.messagingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messagingapp.ModelClass.MessageModel
import com.example.messagingapp.R
import com.github.pgreze.reactions.ReactionPopup
import com.github.pgreze.reactions.dsl.reactionConfig
import com.github.pgreze.reactions.dsl.reactions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.recuever_file.view.*
import kotlinx.android.synthetic.main.sender_file.view.*

class MessageAdapter(val context: Context,val message : ArrayList<MessageModel>, val senderroom : String , val recieverromm : String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

     val SEND_NO : Int = 1
     val RECIEVE_NO : Int = 2
    inner class senderviewholder(val sendview:View):RecyclerView.ViewHolder(sendview){

    }
    inner class recieverviewholder(val recievview:View):RecyclerView.ViewHolder(recievview){

    }

    override fun getItemViewType(position: Int): Int {
        val msg: MessageModel = message[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == msg.senderidid )
            SEND_NO
        else
            RECIEVE_NO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SEND_NO){
            val view: View = LayoutInflater.from(context).inflate(R.layout.sender_file,parent,false)
            senderviewholder(view)
        }
        else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.recuever_file,parent,false)
            recieverviewholder(view)
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val Msg = message[position]

        val rec : Array<Int> = arrayOf( R.drawable.angryemoji, R.drawable.heartemoji,R.drawable.boring ,R.drawable.tearsemoji,R.drawable.thumbemoji
        , R.drawable.glassemoji, R.drawable.smilethumb, R.drawable.thinkemoji)

        val config = reactionConfig(context) {
            reactions {
                resId    { R.drawable.angryemoji }
                resId    { R.drawable.heartemoji }
                resId    { R.drawable.boring }
                resId    { R.drawable.tearsemoji }
                resId    { R.drawable.thumbemoji }
                reaction { R.drawable.glassemoji scale ImageView.ScaleType.FIT_XY }
                reaction { R.drawable.smilethumb scale ImageView.ScaleType.FIT_XY }
                reaction { R.drawable.thinkemoji scale ImageView.ScaleType.FIT_XY }
            }
        }

        val popup = ReactionPopup(context, config) { pos: Int -> true.also {
            if (holder.javaClass == senderviewholder::class.java){
                val viewholder : senderviewholder = holder as senderviewholder
               viewholder.sendview.senderfilling.setImageResource(rec[pos])
                viewholder.sendview.senderfilling.visibility = View.VISIBLE
            }
            else{
                val viewholder : recieverviewholder = holder as recieverviewholder
                viewholder.recievview.recieverfilling.setImageResource(rec[pos])
                viewholder.recievview.recieverfilling.visibility = View.VISIBLE
            }
           Msg.filling = pos.toLong()
            FirebaseDatabase.getInstance().getReference("Chatting").
            child(senderroom).child("Message").
            child(Msg.messageid!!).setValue(Msg)

            FirebaseDatabase.getInstance().getReference("Chatting").
            child(recieverromm).child("Message").
            child(Msg.messageid!!).setValue(Msg)
        } }

        if (holder.javaClass == senderviewholder::class.java){
           val viewholder : senderviewholder = holder as senderviewholder
            if (Msg.message == "photo"){
                viewholder.sendview.senderimage.visibility = View.VISIBLE
                viewholder.sendview.sendermsg.visibility = View.GONE
                Glide.with(context).load(Msg.imageurl).placeholder(R.drawable.ic_baseline_image_24).into(viewholder.sendview.senderimage)
            }
           viewholder.sendview.sendermsg.text = Msg.message

            if (Msg.filling >= 0){
                viewholder.sendview.senderfilling.setImageResource(rec[Msg.filling.toInt()])

                viewholder.sendview.senderfilling.visibility = View.VISIBLE
            }
            else{
                viewholder.sendview.senderfilling.visibility = View.GONE
            }
            viewholder.sendview.sendermsg.setOnTouchListener { v, event ->
                popup.onTouch(v,event)

            }
            viewholder.sendview.senderimage.setOnTouchListener { v: View, event: MotionEvent ->
                popup.onTouch(v,event)


            }
       }
        else{
           val viewholder : recieverviewholder = holder as recieverviewholder
            if (Msg.message == "photo"){
                viewholder.recievview.recieverimage.visibility = View.VISIBLE
                viewholder.recievview.recievermsg.visibility = View.GONE
                Glide.with(context).load(Msg.imageurl).placeholder(R.drawable.ic_baseline_image_24).into(viewholder.recievview.recieverimage)
            }
           viewholder.recievview.recievermsg.text = Msg.message

            if (Msg.filling >= 0){
                viewholder.recievview.senderfilling.setImageResource(rec[Msg.filling.toInt()])
                viewholder.recievview.senderfilling.visibility = View.VISIBLE
            }
            else{
                viewholder.recievview.senderfilling.visibility = View.GONE
            }
            viewholder.recievview.recievermsg.setOnTouchListener { v, event ->
                popup.onTouch(v,event)

            }
            viewholder.recievview.recieverimage.setOnTouchListener { v, event ->
                popup.onTouch(v,event)

            }
        }
    }

    override fun getItemCount(): Int {
       return message.size
    }
}