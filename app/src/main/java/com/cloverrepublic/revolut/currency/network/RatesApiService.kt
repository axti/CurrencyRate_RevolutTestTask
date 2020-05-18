package com.cloverrepublic.revolut.currency.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by axti on 05.05.2020.
 */
interface RatesApiService {

    @GET("api/android/latest")
    fun loadLatestRates(@Query("base") baseCurrency: String? = "EUR"): Single<RateDataObject>
}