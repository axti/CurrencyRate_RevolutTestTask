package com.cloverrepublic.revolut.currency.data

import com.cloverrepublic.revolut.currency.data.repository.RatesRepository
import com.cloverrepublic.revolut.currency.data.repository.RatesResponse
import com.cloverrepublic.revolut.currency.network.RateDataObject
import io.reactivex.Flowable

/**
 * Created by axti on 05.05.2020.
 */
class RatesModelImpl(private val ratesRepository: RatesRepository) : RatesModel {

    private var baseMultiplier = 1.0
    private var cachedRateData: RateDataObject? = null

    override fun getLatestRates(): Flowable<CurrencyData> {
        return ratesRepository.getLatestRates().map { rateResponse ->
            when (rateResponse) {
                is RatesResponse.SuccessResponse -> {
                    cachedRateData = rateResponse.dataObject
                    return@map CurrencyData(mapToList(cachedRateData))
                }
                is RatesResponse.FailedResponse -> {
                    return@map CurrencyData(
                        mapToList(cachedRateData),
                        error = "Error: ${rateResponse.errorMessage}"
                    )
                }
            }
        }
    }

    private fun mapToList(data: RateDataObject?): List<Currency>? {
        if (data == null)
            return null
        return ArrayList<Currency>().apply {
            add(Currency(data.baseCurrency, 1.0 * baseMultiplier, true))
            addAll(data.rates.entries.map { Currency(it.key, it.value * baseMultiplier) })
        }
    }

    override fun setBaseCurrency(currency: String) {
        ratesRepository.setBaseCurrency(currency)
        baseMultiplier = 1.0
    }

    override fun setBaseMultiplier(multiplier: Double) {
        baseMultiplier = multiplier
    }
}