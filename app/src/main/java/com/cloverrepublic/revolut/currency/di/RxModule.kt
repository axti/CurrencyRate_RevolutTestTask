package com.cloverrepublic.revolut.currency.di

import com.cloverrepublic.revolut.currency.utils.rx.ApplicationSchedulerProvider
import com.cloverrepublic.revolut.currency.utils.rx.SchedulerProvider
import org.koin.dsl.module

/**
 * Created by axti on 05.05.2020.
 */
val rxModule = module {
    single { ApplicationSchedulerProvider() as SchedulerProvider }
}