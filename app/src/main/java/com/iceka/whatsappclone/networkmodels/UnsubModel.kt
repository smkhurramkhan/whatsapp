package com.iceka.whatsappclone.networkmodels

data class UnsubModel(
    val error: String,
    val message: String,
    val path: String,
    val status: Int,
    val timestamp: String
)