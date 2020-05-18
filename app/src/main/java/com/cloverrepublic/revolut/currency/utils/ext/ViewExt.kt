package com.cloverrepublic.revolut.currency.utils.ext

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by axti on 06.05.2020.
 */
inline fun View.waitForLayout(crossinline f: () -> Unit) =
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            Log.e("ViewTreeObserver", "onGlobalLayout")
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
            f()
        }
    })