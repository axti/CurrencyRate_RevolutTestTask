package com.cloverrepublic.revolut.currency.view.listitem

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.annotation.DrawableRes
import com.cloverrepublic.revolut.currency.R
import com.cloverrepublic.revolut.currency.data.ListItemData
import com.cloverrepublic.revolut.currency.utils.ext.dismissKeyboard
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.base_list_item.view.*
import kotlinx.android.synthetic.main.nice_list_item.view.*

/**
 * Created by axti on 05.05.2020.
 */
open class NiceCurrencyListItemView(context: Context) : ListItemView(context) {

    override val layoutResource: Int
        get() = R.layout.nice_list_item

    private val textListener = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (currencyRate.isFocused && !s?.toString().isNullOrEmpty()) {
                listItemListener?.onAmountChanged(s.toString().toDouble())
            }
        }
    }

    init {
        setBackground()
        currencyRate.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus)
                v.dismissKeyboard()
        }
    }

    override fun update(dataItem: ListItemData, position: Int) {
        super.update(dataItem, position)
        loadImage(dataItem.iconId)
        dataItem.subTitleId?.let { currencyDescription?.setText(it) }
        updateBase(dataItem.isBase)
    }

    fun updateBase(isBase: Boolean?) {
        if (isBase == true) {
            setOnClickListener(null)
            currencyRate?.setOnClickListener(null)
            currencyRate?.isFocusableInTouchMode = true
            currencyRate?.addTextChangedListener(textListener)
        } else {
            currencyRate?.isFocusable = false
            currencyRate?.setOnClickListener { this.callOnClick() }
            currencyRate?.removeTextChangedListener(textListener)
            setOnClickListener { listItemListener?.onSelectBaseCurrency(itemData.title ?: "") }
        }
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

    override fun onDetachedFromWindow() {
        currencyRate?.run {
            removeTextChangedListener(textListener)
            if (!hasFocus())
                dismissKeyboard()
        }
        setOnClickListener(null)
        super.onDetachedFromWindow()
    }
}