package com.iceka.whatsappclone.models

class Conversation {
    var userUid: String? = null
    var chatWithId: String? = null
    var chatId: String? = null
    var lastMessage: String? = null
    var user: User? = null
    var timestamp: Long = 0
    var unreadChatCount = 0

    constructor() {}
    constructor(
        userUid: String?,
        chatWithId: String?,
        lastMessage: String?,
        timestamp: Long,
        unreadChatCount: Int
    ) {
        this.userUid = userUid
        this.chatWithId = chatWithId
        this.lastMessage = lastMessage
        this.timestamp = timestamp
        this.unreadChatCount = unreadChatCount
    }

    constructor(userUid: String?, chatWithId: String?, lastMessage: String?, timestamp: Long) {
        this.userUid = userUid
        this.chatWithId = chatWithId
        this.lastMessage = lastMessage
        this.timestamp = timestamp
    }
}