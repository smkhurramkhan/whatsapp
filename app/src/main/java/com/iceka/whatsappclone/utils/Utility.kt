package com.iceka.whatsappclone.utils

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ProgressBar

object Utility {
    @JvmStatic
    fun setMargins(view: View) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, 8, 0)
            view.requestLayout()
        }
    }

    @JvmStatic
    fun setProgressMax(pb: ProgressBar) {
        pb.max = 100 * 100
    }
}