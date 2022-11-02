package com.iceka.whatsappclone.network

import com.google.gson.JsonObject
import com.iceka.whatsappclone.api.ApiClient
import com.iceka.whatsappclone.api.IEndPoints
import com.iceka.whatsappclone.interfaces.NetworkInterfaceCalls
import retrofit2.Response
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException

class UnsubscibeNetworkCall(var interfaceCall: NetworkInterfaceCalls)
{
    fun unSubscribeUser(msisdn: String)
    {
        val endPoints = ApiClient.getClient().create(IEndPoints::class.java)
        val unSub = endPoints.unsub(msisdn )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
        extracted(unSub)
    }

    private fun extracted(aboutYou: Observable<Response<JsonObject>>)
    {
        aboutYou.subscribe(object : Observer<Response<JsonObject>>
        {
            override fun onCompleted()
            {

            }
            override fun onError(e: Throwable) {
                interfaceCall.onException(e.toString(), "UnSubscibeCall")
            }
            override fun onNext(jsonObjectResponse: Response<JsonObject>)
            {
                if (jsonObjectResponse.code() == 200) {
                    Timber.d("response is",""+jsonObjectResponse.code())

                    if (jsonObjectResponse.body()!!["status"].toString().toInt() == 200) {
                        Timber.d("response is",""+jsonObjectResponse.message())
                        interfaceCall.onSuccess(jsonObjectResponse.body()!!, "SubscibeCall")
                    } else {
                        try {
                            interfaceCall.onFailure(jsonObjectResponse.errorBody()!!.string(), "SubscibeCall")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }else {
                    if (jsonObjectResponse.body() != null) {
                        interfaceCall.onSuccess(jsonObjectResponse.body()!!, "UnSubscibeCall")
                    } else {
                        try {
                            interfaceCall.onFailure(jsonObjectResponse.errorBody()!!.string(), "UnSubscibeCall")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }
}