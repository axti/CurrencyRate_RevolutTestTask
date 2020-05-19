package com.cloverrepublic.revolut.currency.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cloverrepublic.revolut.currency.data.ListItemData

/**
 * Created by axti on 07.05.2020.
 */
class ListDiffUtilCallback(
    private var oldItems: List<ListItemData>,
    private var newItems: List<ListItemData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].title == newItems[newItemPosition].title

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].value == newItems[newItemPosition].value ||
                oldItems[oldItemPosition].isBase == newItems[newItemPosition].isBase

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return Change(
            oldItem,
            newItem
        )
    }

    companion object {
        fun create(
            oldItems: List<ListItemData>,
            newItems: List<ListItemData>
        ): ListDiffUtilCallback {
            return ListDiffUtilCallback(oldItems, newItems)
        }
    }
}