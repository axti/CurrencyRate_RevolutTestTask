package com.cloverrepublic.revolut.currency.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

/**
 * Created by axti on 07.05.2020.
 */
object RxDiffUtil {
    fun <T> calculateDiff(
        diffCallbacks: (List<T>, List<T>) -> DiffUtil.Callback
    ): FlowableTransformer<List<T>, Pair<List<T>, DiffResult?>> {
        var initialPair: Pair<List<T>, DiffResult?> = emptyList<T>() to null
        return FlowableTransformer { upstream: Flowable<List<T>> ->
            upstream.scan(initialPair,
                { (first, _), nextItems ->
                    val callback =
                        diffCallbacks(first, nextItems)
                    val result = DiffUtil.calculateDiff(callback, true)
                    initialPair = Pair(nextItems, result)
                    Pair(nextItems, result)
                }
            )
                .skip(1)
        } // downstream shouldn't receive initialPair.
    }
}