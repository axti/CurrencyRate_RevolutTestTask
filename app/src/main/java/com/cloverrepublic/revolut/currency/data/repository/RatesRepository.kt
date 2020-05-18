package com.cloverrepublic.revolut.currency.data.repository

import io.reactivex.Flowable

/**
 * Created by axti on 05.05.2020.
 */
interface RatesRepository {
    fun setBaseCurrency(currency: String)
    fun getLatestRates(): Flowable<RatesResponse>
}