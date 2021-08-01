package com.example.messagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.messagingapp.hepler.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_resetpassword.*

class ResetpasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword)

        setpass.setOnClickListener {
            val email = forgetemail.text.toString().trim()

            if(email.isEmpty()){
                forgetemail.error = "Enter Email id"
                forgetemail.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                forgetemail.error = "Enter valid  Email id"
                forgetemail.requestFocus()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE
                    if(task.isSuccessful){
                        this.toast("Reset password Email has been sent to your Email Id ")
                    }else{
                        this.toast(task.exception?.message!!)
                    }

                }
        }
    }
}