package com.cloverrepublic.revolut.currency.data

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created by axti on 06.05.2020.
 */
class ResourceManager(private val context: Context) {

    @DrawableRes
    fun getDrawableResourceByName(name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    @StringRes
    fun getStringResourceByName(name: String): Int {
        return context.resources.getIdentifier(name, "string", context.packageName)
    }
}