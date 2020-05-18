package com.cloverrepublic.revolut.currency.view.listitem

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import kotlinx.android.synthetic.main.base_list_item.view.*

/**
 * Created by axti on 18.05.2020.
 */
class BaseCurrencyListItemView(context: Context) : NiceCurrencyListItemView(context) {

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
        currencyRate.isEnabled = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        currencyRate?.addTextChangedListener(textListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        currencyRate?.removeTextChangedListener(textListener)
    }
}
