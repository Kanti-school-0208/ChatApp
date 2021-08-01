package com.example.messagingapp.ModelClass

class MessageModel {
     var messageid: String? = null
     var message: String? = null
    var imageurl: String? = null
     var senderidid: String? = null
     var filling: Long = -1
     var time: Long? = null
    constructor()

    constructor( message: String?, senderidid: String?, filling: Long, time: Long?,imageurl: String?) {

        this.message = message
        this.senderidid = senderidid
        this.filling = filling
        this.time = time
        this.imageurl = imageurl
    }
    constructor( message: String?, senderidid: String?, filling: Long, time: Long?) {

        this.message = message
        this.senderidid = senderidid
        this.filling = filling
        this.time = time
    }
}