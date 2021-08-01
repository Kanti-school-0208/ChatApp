package com.example.messagingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.messagingapp.hepler.Login
import com.example.messagingapp.hepler.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mauth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mauth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            val email = schoolemaill.text.toString().trim()
            val pass = schoolpassl.text.toString().trim()

            if(email.isEmpty()){
                schoolemaill.error = "Enter Email id"
                schoolemaill.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                schoolemaill.error = "Enter valid  Email id"
                schoolemaill.requestFocus()
                return@setOnClickListener
            }
            if(pass.isEmpty() || pass.length < 6){
                schoolpassl.error = "Enter  al least 6 digit password"
                schoolpassl.requestFocus()
                return@setOnClickListener
            }

            loginuser(email , pass)
        }

        clickherel.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }
        forgetpassword.setOnClickListener {
            startActivity(Intent(this, ResetpasswordActivity::class.java))
        }
    }
    private fun loginuser(email: String, pass: String) {

        mauth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener (this){ task ->
                if(task.isSuccessful){
                    Login()
                    finish()
                }
                else{
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }
    }
    override fun onStart() {
        super.onStart()
        mauth.currentUser?.let {
            Login()
        }
    }
}