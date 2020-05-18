package com.cloverrepublic.revolut.currency.view.listitem

import android.content.Context
import android.util.AttributeSet
import com.cloverrepublic.revolut.currency.R
import com.cloverrepublic.revolut.currency.data.BaseCurrencyItem

/**
 * Created by axti on 05.05.2020.
 */
class SimpleCurrencyItem : ListItemView {
    constructor(context: Context): super(context)

    constructor(context: Context, attr: AttributeSet): super(context, attr)
    override val layoutResource: Int
        get() = R.layout.base_list_item
}