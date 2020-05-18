package com.cloverrepublic.revolut.currency.data

import io.reactivex.Flowable

/**
 * Created by axti on 05.05.2020.
 */
interface RatesModel{
    fun getLatestRates(): Flowable<CurrencyData>
    fun setBaseCurrency(currency: String)
    fun setBaseMultiplier(multiplier: Double)
}