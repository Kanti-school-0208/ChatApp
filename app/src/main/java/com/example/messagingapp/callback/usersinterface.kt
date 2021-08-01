package com.example.messagingapp.callback

import com.example.messagingapp.ModelClass.User
import com.example.messagingapp.ModelClass.UserstatusModel

interface usersinterface {

    fun userloadsuccess(users:ArrayList<User>)
    fun userloadfailed(msg:String)

//    fun userstatusloadsuccess(usersstatus:ArrayList<UserstatusModel>)
//    fun userstatusloadfailed(msg:String)
}