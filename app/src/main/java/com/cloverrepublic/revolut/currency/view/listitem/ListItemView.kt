package com.cloverrepublic.revolut.currency.view.listitem

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.cloverrepublic.revolut.currency.data.ListItemData
import com.cloverrepublic.revolut.currency.utils.ext.format
import com.cloverrepublic.revolut.currency.view.adapter.ListItemListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.base_list_item.view.*
import kotlin.math.abs

/**
 * Created by axti on 05.05.2020.
 */
abstract class ListItemView : ConstraintLayout {
    private val disposables = CompositeDisposable()
    protected lateinit var itemData: ListItemData
    var listItemListener: ListItemListener? = null

    /**
     * Each view provides its own layout with one method for easy access
     *
     * @return layout resource id
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    init {
        inflateView()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    /**
     * Inflation method with one common parameter
     */
    private fun inflateView() {
        View.inflate(context, layoutResource, this)
    }

    /**
     * Updates the list items texts and image.
     *
     * @param dataItem The Item that contains the new texts and image.
     */
    @CallSuper
    open fun update(dataItem: ListItemData, position: Int) {
        itemData = dataItem
        currencyCode?.text = dataItem.title
        updateValue(dataItem.value)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.layoutParams?.apply {
            width = MATCH_PARENT
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables.dispose()
//        currencyRate?.removeTextChangedListener(textListener)
    }

    fun updateValue(value: Double?) {
        currencyRate?.run {
            value?.let {
                val digitNum = when(it){
                    in 0.001..0.009 -> 3
                    else -> 2
                }
                val str = it.format(digitNum)
                if (this.isFocused) {
                    val newCursorPosition: Int =
                        str.length - abs(this.selectionEnd - (this.text?.length ?: 0))
                    this.setText(str)
                    this.setSelection(0.coerceAtLeast(newCursorPosition).coerceAtMost(str.length))
                } else
                    this.setText(str)
            }
        }
    }
}