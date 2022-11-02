package com.iceka.whatsappclone.models

class StatusItem {
    var id: String? = null
        private set
    var type: String? = null
        private set
    var duration = 0
        private set
    var url: String? = null
        private set
    var caption: String? = null
        private set
    var text: String? = null
        private set
    var timestamp: Long = 0
        private set
    var expireTime: Long = 0
        private set
    var backgroundColor = 0
        private set
    var viewed: Viewed? = null
        private set
    var thumbnail: String? = null
        private set

    constructor() {}

    // For Text
    constructor(
        id: String?,
        type: String?,
        text: String?,
        timestamp: Long,
        expireTime: Long,
        backgroundColor: Int,
        thumbnail: String?,
        viewed: Viewed?
    ) {
        this.id = id
        this.type = type
        this.text = text
        this.timestamp = timestamp
        this.expireTime = expireTime
        this.backgroundColor = backgroundColor
        this.thumbnail = thumbnail
        this.viewed = viewed
    }

    // For Videos
    constructor(
        id: String?,
        type: String?,
        duration: Int,
        url: String?,
        caption: String?,
        timestamp: Long,
        expireTime: Long,
        viewed: Viewed?
    ) {
        this.id = id
        this.type = type
        this.duration = duration
        this.url = url
        this.caption = caption
        this.timestamp = timestamp
        this.expireTime = expireTime
        this.viewed = viewed
    }

    // For Images
    constructor(
        id: String?,
        type: String?,
        url: String?,
        caption: String?,
        timestamp: Long,
        expireTime: Long,
        thumbnail: String?,
        viewed: Viewed?
    ) {
        this.id = id
        this.type = type
        this.url = url
        this.caption = caption
        this.timestamp = timestamp
        this.expireTime = expireTime
        this.thumbnail = thumbnail
        this.viewed = viewed
    }
}