package com.cloverrepublic.revolut.currency.view.listitem

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DrawableRes
import com.cloverrepublic.revolut.currency.R
import com.cloverrepublic.revolut.currency.data.ListItemData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nice_list_item.view.*

/**
 * Created by axti on 05.05.2020.
 */
open class NiceCurrencyListItemView(context: Context) : ListItemView(context) {

    override val layoutResource: Int
        get() = R.layout.nice_list_item

    init {
        setOnClickListener { listItemListener?.onSelectBaseCurrency(itemData.title ?: "") }
        setBackground()
    }

    override fun update(dataItem: ListItemData, position: Int) {
        super.update(dataItem, position)
        loadImage(dataItem.iconId)
        dataItem.subTitleId?.let { currencyDescription?.setText(it) }
    }

    private fun loadImage(@DrawableRes resId: Int) {
        Picasso.get().load(resId)
            .resize(300, 300)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(currencyImage)
    }

    private fun setBackground() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }
}