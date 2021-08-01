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
import com.bumptech.glide.Glide
import com.example.messagingapp.ModelClass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_seeprofile.*
import kotlinx.android.synthetic.main.activity_seeprofile.seepropic

class SeeprofileActivity : AppCompatActivity() {

    private lateinit var mauth: FirebaseAuth
    private lateinit var usercollection: CollectionReference
    lateinit var userid : String
    var staffimageurl: Uri? = null
    var datastore: StorageReference = FirebaseStorage.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seeprofile)

        mauth = FirebaseAuth.getInstance()
        usercollection = FirebaseFirestore.getInstance().collection("User")
        userid = mauth.currentUser!!.uid

        retrivedata( userid)
        editprofilebtn.setOnClickListener {
            seeeprofilelayout.visibility = View.GONE
            scrollupdate.visibility = View.VISIBLE
        }
        back.setOnClickListener {
            seeeprofilelayout.visibility = View.VISIBLE
            scrollupdate.visibility = View.GONE
        }
        updateprofilebtn.setOnClickListener {

        }
        changepropic.setOnClickListener {
            chooseimagel()
        }

        updateprofilebtn.setOnClickListener {


            val Nam = changename.text.toString().trim()
            val phoneno = changephone.text.toString().trim()
            val email = changeuseremail.text.toString().trim()
            val dob = changeuserdob.text.toString().trim()
            if (staffimageurl != null && Nam.isNotEmpty() && phoneno.isNotEmpty()&& dob.isNotEmpty() && email.isNotEmpty()) {
                changeprogress_profile.visibility = View.VISIBLE
                UploaduserDatal(staffimageurl!!,Nam,phoneno,dob,email)
            } else {
                Toast.makeText(this, "Filled All Data", Toast.LENGTH_LONG).show()
            }

        }
        
    }
    private fun UploaduserDatal(uri: Uri, nam: String, phoneno: String, dob: String, email: String) {

        val datastore1: StorageReference = datastore.child("Users").child(nam + "." + getrefrencel(uri))
        datastore1.putFile(uri).addOnSuccessListener {
            datastore1.downloadUrl
                .addOnSuccessListener {
                    val user: HashMap<String, Any> = hashMapOf("Name" to nam,"PhoneNo" to "+91$phoneno","Emailid" to email,"DOB" to dob,"UserImage" to it.toString())
                    usercollection.document(userid).update(user)
                    changeprogress_profile.visibility = View.GONE
                    Toast.makeText(this, "Profile is updated", Toast.LENGTH_LONG).show()
                }
        }
            .addOnProgressListener {
                Toast.makeText(this, "Profile is updating...", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun getrefrencel(muri: Uri): String {
        val cr: ContentResolver = contentResolver
        val mine: MimeTypeMap = MimeTypeMap.getSingleton()
        return mine.getExtensionFromMimeType(cr.getType(muri)).toString()
    }

    private fun chooseimagel() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 11)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK && data != null) {
            staffimageurl = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, staffimageurl)
            changepropic.setImageBitmap(bitmap)
        }
    }

    private fun retrivedata(userid: String) {

        usercollection.document(userid).addSnapshotListener { value: DocumentSnapshot?, error: FirebaseFirestoreException? ->

            if (error != null){
                Toast.makeText(this,error.message,Toast.LENGTH_SHORT).show()
            }
            else{
                val user: User? = value?.toObject(User::class.java)
                emailuser.text = user!!.Emailid
                phoneuser.text = user.PhoneNo
                dobuser.text = user.DOB
                nameuser.text = user.Name
                Glide.with(this).load(user.UserImage).into(seepropic)
                progressBar2.visibility = View.GONE
            }

        }
    }
}