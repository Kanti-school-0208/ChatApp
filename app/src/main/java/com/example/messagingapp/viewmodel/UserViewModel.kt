package com.example.messagingapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messagingapp.ModelClass.StatusListModel
import com.example.messagingapp.ModelClass.User
import com.example.messagingapp.ModelClass.UserstatusModel
import com.example.messagingapp.callback.usersinterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class UserViewModel : ViewModel(), usersinterface {

    var userdatalist : MutableLiveData<ArrayList<User>>? = null
    var message : MutableLiveData<String>? = null

//    var userstatuslist : MutableLiveData<ArrayList<UserstatusModel>>? = null
//    var message1 : MutableLiveData<String>? = null

    val usercallback : usersinterface = this
    //val userliststatus : ArrayList<UserstatusModel> = ArrayList()

    fun userdata(): MutableLiveData<ArrayList<User>> {
        if (userdatalist == null){
            userdatalist = MutableLiveData()
            message = MutableLiveData()
            loaduserdata()
        }
        return userdatalist!!
    }

//    fun userstatusdata(): MutableLiveData<ArrayList<UserstatusModel>> {
//        if (userstatuslist == null){
//            userstatuslist = MutableLiveData()
//            message = MutableLiveData()
//            loaduserstatusdata()
//        }
//        return userstatuslist!!
//    }
//
//    private fun loaduserstatusdata() {
//
//        FirebaseDatabase.getInstance().getReference("Stories").addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userliststatus.clear()
//                for (i in snapshot.children){
////                    val status = i.getValue(UserstatusModel::class.java)
////                    userliststatus.add(status!!)
//                    val status = UserstatusModel()
//                    status.susername = i.child("susername").value?.toString()
//                    status.suserimage = i.child("suserimage").value?.toString()
//                    status.lastupdate = i.child("lastupdate").value as Long?
//
//                    val userstatus : ArrayList<StatusListModel> = ArrayList()
//                    for (j in i.child("Statuslist").children){
//                        val statues = j.getValue(StatusListModel::class.java)
//                        userstatus.add(statues!!)
//                    }
//                    status.Statuslist = userstatus
//                    userliststatus.add(status)
//                }
//                usercallback.userstatusloadsuccess(userliststatus)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//    }

    private fun loaduserdata() {
        val userlist : ArrayList<User> = ArrayList()
        val usercollection : CollectionReference = FirebaseFirestore.getInstance().collection("User")
        usercollection.addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (error != null){
                error.message?.let { usercallback.userloadfailed(it) }
            }
            else{
                for (i: QueryDocumentSnapshot in snapshot!!){
                    val Data = i.toObject(User::class.java)
                    Data.userid = i.id
                    if (Data.userid != FirebaseAuth.getInstance().currentUser!!.uid){
                        userlist.add(Data)
                    }

                }
                usercallback.userloadsuccess(userlist)
            }
        }
    }

    override fun userloadsuccess(users: ArrayList<User>) {
        userdatalist!!.value = users
    }

    override fun userloadfailed(msg: String) {
       message!!.value  = msg
    }

//    override fun userstatusloadsuccess(usersstatus: ArrayList<UserstatusModel>) {
//        userstatuslist!!.value = usersstatus
//    }
//
//    override fun userstatusloadfailed(msg: String) {
//        message1!!.value  = msg
//    }
}