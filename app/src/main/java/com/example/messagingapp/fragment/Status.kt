package com.example.messagingapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.messagingapp.ModelClass.StatusListModel
import com.example.messagingapp.ModelClass.User
import com.example.messagingapp.ModelClass.UserstatusModel
import com.example.messagingapp.R
import com.example.messagingapp.adapter.StatusAdapter
import com.example.messagingapp.viewmodel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class Status : Fragment() {
    lateinit var Statusbtn: FloatingActionButton
    lateinit var statusrecycler: ShimmerRecyclerView
    lateinit var userViewModel: UserViewModel

    private lateinit var adapter: StatusAdapter
    var datastore: StorageReference = FirebaseStorage.getInstance().reference

    private lateinit var user : User
    private lateinit var database: FirebaseDatabase
   val  userslist : ArrayList<UserstatusModel> = ArrayList()
    val userliststatus : ArrayList<UserstatusModel> = ArrayList()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_status, container, false)

       userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        statusrecycler = v.findViewById(R.id.statusrecycler)
       // statusrecycler.showShimmerAdapter()
        database = FirebaseDatabase.getInstance()
        val usercollection : CollectionReference = FirebaseFirestore.getInstance().collection("User")
        usercollection.document(FirebaseAuth.getInstance().currentUser!!.uid).addSnapshotListener { snapshot, error ->
            user = snapshot!!.toObject(User::class.java)!!
        }
        Statusbtn = v.findViewById(R.id.statusbtn)
        Statusbtn.setOnClickListener {
            chooseimage()
        }

        FirebaseDatabase.getInstance().getReference("Stories").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userliststatus.clear()

                for (i in snapshot.children){
//                    val status = i.getValue(UserstatusModel::class.java)
//                    userliststatus.add(status!!)
                    val status = UserstatusModel()
                    status.susername = i.child("susername").value?.toString()
                    status.suserimage = i.child("suserimage").value?.toString()
                    status.lastupdate = i.child("lastupdate").value as Long?

                    val userstatus : ArrayList<StatusListModel> = ArrayList()
                    for (j in i.child("Statuslist").children){
                        val statues = j.getValue(StatusListModel::class.java)
                        userstatus.add(statues!!)
                    }
                    status.Statuslist = userstatus
                    userliststatus.add(status)
                }

                //statusrecycler.hideShimmerAdapter()
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

         adapter = StatusAdapter(requireContext(),userliststatus)

        statusrecycler.adapter = adapter

        statusrecycler.layoutManager = LinearLayoutManager(requireContext())
//        userViewModel.userstatusdata().observe(this, androidx.lifecycle.Observer {
//            userslist.clear()
//            userslist.addAll(it)
//           // adapter.notifyDataSetChanged()
//            val adapter = StatusAdapter(requireContext(),userslist)
//
//            statusrecycler.adapter = adapter
//            statusrecycler.layoutManager = LinearLayoutManager(requireContext())
//        })

        return v
    }

    private fun chooseimage() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11 && resultCode == Activity.RESULT_OK && data != null) {

            val dat = Date()
            val datastore1: StorageReference =
                datastore.child("Status").child("${dat.date}" + getsrefrence(data.data!!))
            datastore1.putFile(data.data!!).addOnSuccessListener {
                datastore1.downloadUrl
                    .addOnSuccessListener {
                        val userstatus = UserstatusModel()
                        userstatus.susername = user.Name
                        userstatus.suserimage = user.UserImage
                        userstatus.lastupdate = dat.time

                        val userhashmap = hashMapOf("susername" to userstatus.susername,"suserimage" to userstatus.suserimage,"lastupdate" to userstatus.lastupdate)
                        database.getReference("Stories").child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(
                            userhashmap as Map<String, Any>
                        )

                        database.getReference("Stories").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Statuslist")
                            .push().setValue(StatusListModel(it.toString(), userstatus.lastupdate))
                        
                    }


            }
        }
    }
    private fun getsrefrence(muri: Uri): String {
        val cr: ContentResolver = requireContext().contentResolver
        val mine: MimeTypeMap = MimeTypeMap.getSingleton()
        return mine.getExtensionFromMimeType(cr.getType(muri)).toString()
    }
}