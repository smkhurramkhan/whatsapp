package com.iceka.whatsappclone.models

class Chat {
    var username: String? = null
    var message: String? = null
    var senderUid: String? = null
    var receiverUid: String? = null
    var imageResourceId = 0
    var timestamp: Long = 0

    constructor() {}
    constructor(username: String?, message: String?, imageResourceId: Int) {
        this.username = username
        this.message = message
        this.imageResourceId = imageResourceId
    }

    constructor(message: String?, senderUid: String?, receiverUid: String?, timestamp: Long) {
        this.message = message
        this.senderUid = senderUid
        this.receiverUid = receiverUid
        this.timestamp = timestamp
    }
}