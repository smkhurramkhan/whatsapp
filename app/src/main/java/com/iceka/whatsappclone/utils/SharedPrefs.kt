package com.iceka.whatsappclone.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context?) {

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    init {
        if (context != null) {
            prefs = context.getSharedPreferences("tasVas", Context.MODE_PRIVATE)
            editor = prefs.edit()
            editor.apply()
        }
    }


    fun getUserName(): String? {
        return prefs.getString("username", "")
    }

    fun setUserName(username: String) {
        editor.putString("username", username)
        editor.commit()
    }


    fun setUserNumber(languageId: String) {
        editor.putString("UserNumber", languageId)
        editor.commit()
    }

    fun getUserNumber(): String? {
        return prefs.getString("UserNumber", null)
    }


}