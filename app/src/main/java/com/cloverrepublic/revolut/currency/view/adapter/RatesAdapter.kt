package com.cloverrepublic.revolut.currency.view.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cloverrepublic.revolut.currency.data.ListItemData
import com.cloverrepublic.revolut.currency.data.ListItemData.Companion.CURRENCY
import com.cloverrepublic.revolut.currency.data.ListItemData.Companion.ERROR
import com.cloverrepublic.revolut.currency.view.listitem.*
import io.reactivex.functions.Consumer

/**
 * Created by axti on 05.05.2020.
 */
class RatesAdapter(private val itemListener: ListItemListener) :
    RecyclerView.Adapter<RatesAdapter.ViewHolder>(),
    Consumer<Pair<List<ListItemData>, DiffUtil.DiffResult?>> {

    private var items: ArrayList<ListItemData> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @ListItemData.ItemType viewType: Int
    ): RatesAdapter.ViewHolder {
        return ViewHolder(
            when (viewType) {
                ERROR -> ErrorListItemView(parent.context)
                CURRENCY -> NiceCurrencyListItemView(parent.context)
                else -> throw IllegalStateException("View not implemented yet type = $viewType")
            }
        )
    }

    override fun getItemCount(): Int = items.size

    @ListItemData.ItemType
    override fun getItemViewType(position: Int) = items[position].type

    override fun getItemId(position: Int): Long {
        return items[position].title.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: RatesAdapter.ViewHolder, position: Int) {
        Log.d("adapter", "onBindViewHolder")
        (holder.itemView as ListItemView).update(items[position], position)
        holder.itemView.listItemListener = itemListener
    }

    override fun onBindViewHolder(
        holder: RatesAdapter.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            Log.d("adapter", "payloads is empty $position")
            super.onBindViewHolder(holder, position, payloads)
        } else {
            Log.d("adapter", "need to update $position")
            val combinedChange = createCombinedPayload(payloads as List<Change<ListItemData>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.value != oldData.value) {
                (holder.itemView as ListItemView).updateValue(newData.value)
            }
            if (newData.isBase != oldData.isBase) {
                (holder.itemView as NiceCurrencyListItemView).updateBase(newData.isBase)
            }
        }
    }

    /**
     * View holder class with one holder for itemView itself.
     */
    inner class ViewHolder(itemView: ListItemView) : RecyclerView.ViewHolder(itemView)

    override fun accept(pair: Pair<List<ListItemData>, DiffUtil.DiffResult?>) {
//        items = pair.first
        items.clear()
        items.addAll(pair.first)
        pair.second?.dispatchUpdatesTo(this)
    }
}