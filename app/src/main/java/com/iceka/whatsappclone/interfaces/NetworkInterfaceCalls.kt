package com.iceka.whatsappclone.interfaces

import com.google.gson.JsonObject

interface NetworkInterfaceCalls {

    fun onSuccess(`object`: JsonObject, className: String)
    fun onFailure(errorMessage: String, className: String)
    fun onException(exception: String, className: String)
}