package com.example.messagingapp.ModelClass

class StatusListModel {
    var statusimage : String? = null
    var statustime : Long? = null

    constructor()
    constructor(statusimage: String?, statustime: Long?) {
        this.statusimage = statusimage
        this.statustime = statustime
    }
}