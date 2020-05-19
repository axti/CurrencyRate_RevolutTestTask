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
    private val list = mutableListOf<String>()

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
        //Set Base Currency first
        list.indexOf(data.baseCurrency)
            .takeIf { it != 0 }
            ?.let { index ->
                if (index > 0)
                    list.remove(data.baseCurrency)
                list.add(0, data.baseCurrency)
            }

        //Add currency if not exist
        data.rates.keys.forEach { name ->
            if (list.indexOf(name) < 0) {
                list.add(name)
            }
        }

        //Remove if not exist in a new list
        list.filterNot { data.rates.keys.contains(it) || data.baseCurrency == it }
            .let { list.removeAll(it) }

        return ArrayList<Currency>().apply {
            add(Currency(data.baseCurrency, 1.0 * baseMultiplier, true))
            addAll(data.rates.entries.map { Currency(it.key, it.value * baseMultiplier) })
            sortWith(compareBy { list.indexOf(it.name) })
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