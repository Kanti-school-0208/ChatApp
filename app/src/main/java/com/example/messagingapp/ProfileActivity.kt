package com.example.messagingapp

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.messagingapp.hepler.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_signin.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var mauth: FirebaseAuth
    private lateinit var usercollection: CollectionReference
    var staffimageurl: Uri? = null
    var datastore: StorageReference = FirebaseStorage.getInstance().reference
     lateinit var userid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mauth = FirebaseAuth.getInstance()
        usercollection = FirebaseFirestore.getInstance().collection("User")
        userid = mauth.currentUser!!.uid
        progress_profile.visibility = View.GONE

        propic.setOnClickListener {
            chooseimage()
        }

        profilebtn.setOnClickListener {

            val Nam = name.text.toString().trim()
            val phoneno = phone.text.toString().trim()
            val email = useremail.text.toString().trim()
            val dob = userdob.text.toString().trim()
            if (staffimageurl != null && Nam.isNotEmpty() && phoneno.isNotEmpty()&& dob.isNotEmpty() && email.isNotEmpty()) {
                progress_profile.visibility = View.VISIBLE
                UploaduserData(staffimageurl!!,Nam,phoneno,dob,email)
            } else {
                Toast.makeText(this, "Filled All Data", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun UploaduserData(uri: Uri, nam: String, phoneno: String, dob: String, email: String) {

        val datastore1: StorageReference = datastore.child("Users").child(nam + "." + getrefrence(uri))
        datastore1.putFile(uri).addOnSuccessListener {
            datastore1.downloadUrl
                .addOnSuccessListener {
                    val user: HashMap<String, Any> = hashMapOf("Name" to nam,"PhoneNo" to "+91$phoneno","Emailid" to email,"DOB" to dob,"UserImage" to it.toString())
                    usercollection.document(userid).set(user)
                    progress_profile.visibility = View.GONE
                    Login()
                }
        }
            .addOnProgressListener {
                Toast.makeText(this, "Profile is creating...", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun getrefrence(muri: Uri): String {
        val cr: ContentResolver = contentResolver
        val mine: MimeTypeMap = MimeTypeMap.getSingleton()
        return mine.getExtensionFromMimeType(cr.getType(muri)).toString()
    }

    private fun chooseimage() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            staffimageurl = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, staffimageurl)
            propic.setImageBitmap(bitmap)
        }
    }
}