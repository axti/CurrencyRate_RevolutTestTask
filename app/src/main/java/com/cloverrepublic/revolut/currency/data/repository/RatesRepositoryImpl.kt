package com.cloverrepublic.revolut.currency.data.repository

import com.cloverrepublic.revolut.currency.network.RatesApiService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

/**
 * Created by axti on 05.05.2020.
 */
class RatesRepositoryImpl(private val apiService: RatesApiService) : RatesRepository {

    private var baseCurrency: Subject<String> = BehaviorSubject.createDefault("EUR")

    override fun getLatestRates(): Flowable<RatesResponse> {
        return baseCurrency.toFlowable(BackpressureStrategy.LATEST)
            .switchMap { base ->
                Flowable.interval(0, POLLING_INTERVAL_IN_MS, TimeUnit.MILLISECONDS)
                    .flatMapSingle {
                        apiService.loadLatestRates(base)
                            .map<RatesResponse> { RatesResponse.SuccessResponse(it) }
                            .onErrorReturn { RatesResponse.FailedResponse(it.localizedMessage) }
                    }
            }
    }

    override fun setBaseCurrency(currency: String) {
        baseCurrency.onNext(currency)
    }

    companion object {
        internal const val POLLING_INTERVAL_IN_MS = 1000L
    }
}