package com.iceka.whatsappclone

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.jetbrains.annotations.NotNull
import timber.log.Timber

class MyApplication : Application() {
    val hTag = "khurramTag %s"

    val context: MyApplication
        get() = instance?.context!!


    override fun onCreate() {
        super.onCreate()
        instance = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initTimber()


    }


    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    @NotNull message: String,
                    t: Throwable?
                ) {
                    super.log(priority, String.format(hTag, tag), message, t)
                }
            })
        }
    }

    companion object {
        @JvmStatic
        var instance: MyApplication? = null
    }
}