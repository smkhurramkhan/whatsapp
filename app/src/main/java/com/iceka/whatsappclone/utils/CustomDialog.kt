package com.iceka.whatsappclone.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.iceka.whatsappclone.R

class CustomDialog {
    companion object {

        private var customDialogs: CustomDialog? = null
        fun getInstance(): CustomDialog? {
            if (customDialogs == null) customDialogs = CustomDialog()
            return customDialogs
        }

        fun setLoadingDialog(context: Context, isCancelable: Boolean): AlertDialog? {
            val builder = AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView: View = inflater.inflate(R.layout.loading_screen, null)
            builder.setView(dialogView)
            val alertDialog = builder.create()
            alertDialog.setCancelable(isCancelable)
            try {
                alertDialog.show()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
            alertDialog.window!!.setBackgroundDrawable(null)
            return alertDialog
        }

    }
}