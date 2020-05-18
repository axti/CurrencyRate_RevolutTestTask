package com.cloverrepublic.revolut.currency.data

import io.reactivex.Flowable

/**
 * Created by axti on 05.05.2020.
 */
abstract class BaseCurrencyItem {
    abstract val title: String
    abstract val observableValue: Flowable<Double>
}