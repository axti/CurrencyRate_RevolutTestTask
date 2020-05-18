package com.cloverrepublic.revolut.currency.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.cloverrepublic.revolut.currency.R
import com.cloverrepublic.revolut.currency.data.ListItemData
import com.cloverrepublic.revolut.currency.data.ListItemData.Companion.BASE_CURRENCY
import com.cloverrepublic.revolut.currency.data.ListItemData.Companion.CURRENCY
import com.cloverrepublic.revolut.currency.data.ListItemData.Companion.ERROR
import com.cloverrepublic.revolut.currency.data.RatesModel
import com.cloverrepublic.revolut.currency.data.ResourceManager
import com.cloverrepublic.revolut.currency.utils.rx.SchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


/**
 * Created by axti on 05.05.2020.
 */
class MainViewModel(
    private val ratesModel: RatesModel,
    private val schedulerProvider: SchedulerProvider,
    private val resourceManager: ResourceManager
) : ViewModel() {

    private val onScroll: Subject<Unit> = PublishSubject.create()
    private var currencyChanged = false
    private var receivedError = false

    fun getLatestCurrencyList(): Flowable<List<ListItemData>> {
        return ratesModel.getLatestRates()
            //.with(schedulerProvider)
            .subscribeOn(schedulerProvider.io())
            .map {
                val list = ArrayList<ListItemData>()
                if (it.error != null) {
                    list.add(
                        ListItemData.Builder()
                            .setTitle(it.error)
                            .setType(ERROR)
                            .build()
                    )
                    if (!receivedError) {
                        receivedError = true
                        onScroll.onNext(Unit)
                    }
                } else {
                    if (receivedError) {
                        receivedError = false
                        onScroll.onNext(Unit)
                    }
                }
                if (it.list != null) {
                    list.addAll(it.list.map { item ->
                        ListItemData.Builder()
                            .setTitle(item.name)
                            .setValue(item.value)
                            .setIconId(getIconIdByCurrencyTag(item.name))
                            .setSubTitleId(getSubTitleIdByTag(item.name))
                            .setType(if (item.isBase) BASE_CURRENCY else CURRENCY)
                            .build()
                    })
                    if (currencyChanged) {
                        currencyChanged = false
                        onScroll.onNext(Unit)
                    }
                }

                list
            }
    }

    fun shouldScrollToTop(): Observable<Unit> = onScroll

    @DrawableRes
    private fun getIconIdByCurrencyTag(name: String): Int {
        if (name.length > 2) {
            val resId =
                resourceManager.getDrawableResourceByName(name.substring(0, 2).toLowerCase())
            if (resId > 0)
                return resId
        }
        return R.drawable.placeholder
    }

    @StringRes
    private fun getSubTitleIdByTag(name: String): Int {
        val resId = resourceManager.getStringResourceByName("currency_${name.toLowerCase()}_name")
        if (resId > 0)
            return resId
        return R.string.unknown_currency_description
    }

    fun setBaseCurrency(currencyName: String) {
        ratesModel.setBaseCurrency(currencyName)
        currencyChanged = true
    }

    fun setBaseMultiplier(multiplier: Double) = ratesModel.setBaseMultiplier(multiplier)
}