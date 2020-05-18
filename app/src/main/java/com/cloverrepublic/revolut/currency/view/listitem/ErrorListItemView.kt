package com.cloverrepublic.revolut.currency.view.listitem

import android.content.Context
import com.cloverrepublic.revolut.currency.R

/**
 * Created by axti on 06.05.2020.
 */
class ErrorListItemView (context: Context) : ListItemView(context) {

    override val layoutResource: Int
        get() = R.layout.error_list_item

    init {
        setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
    }
}