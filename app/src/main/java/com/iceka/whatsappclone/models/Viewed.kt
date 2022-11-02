package com.iceka.whatsappclone.models

class Viewed {
    var uid: String? = null
        private set
    var timestamp: Long = 0
        private set

    constructor(uid: String?, timestamp: Long) {
        this.uid = uid
        this.timestamp = timestamp
    }

    constructor() {}
}