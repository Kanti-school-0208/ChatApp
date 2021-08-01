package com.example.messagingapp.ModelClass

class UserstatusModel {

    var susername : String? = null
    var suserimage : String? = null
    var lastupdate : Long? = null
    var Statuslist : ArrayList<StatusListModel>? = null

    constructor()
    constructor(
        susername: String?,
        suserimage: String?,
        lastupdate: Long?,
        Statuslist:  ArrayList<StatusListModel>?
    ) {
        this.susername = susername
        this.suserimage = suserimage
        this.lastupdate = lastupdate
        this.Statuslist = Statuslist
    }
}