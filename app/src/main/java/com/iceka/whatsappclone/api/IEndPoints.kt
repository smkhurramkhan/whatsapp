package com.iceka.whatsappclone.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface IEndPoints {

    @POST(EndPointConstants.SUB)
    fun sub(
        @Body `object`: JsonObject?
    ): Observable<Response<JsonObject>>

    @DELETE(EndPointConstants.UNSUB + "{b_party}/USSD")
    fun unsub(
        @Path("b_party") b_party: String
    ): Observable<Response<JsonObject>>


}