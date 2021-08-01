package com.example.messagingapp.hepler

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.messagingapp.LoginActivity
import com.example.messagingapp.MainActivity

fun Context.toast (message: String) =
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()

fun Context.Login(){
    startActivity(Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    })

}
fun Context.Logout(){
    startActivity(Intent(this, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    })

}