package com.cloverrepublic.revolut.currency.data.repository

import com.cloverrepublic.revolut.currency.network.RatesApiService
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * Created by axti on 05.05.2020.
 */
class RatesRepositoryImpl(private val apiService: RatesApiService) : RatesRepository {

    private var baseCurrency: String? = null

    override fun getLatestRates(): Flowable<RatesResponse> {
        return Flowable.interval(1, TimeUnit.SECONDS)
            .flatMapSingle {
                apiService.loadLatestRates(baseCurrency)
                    .map<RatesResponse> { RatesResponse.SuccessResponse(it) }
                    .onErrorReturn { RatesResponse.FailedResponse(it.localizedMessage) }
            }
    }

    override fun setBaseCurrency(currency: String) {
        baseCurrency = currency
    }
}