package com.cloverrepublic.revolut.currency.di

import com.cloverrepublic.revolut.currency.data.RatesModel
import com.cloverrepublic.revolut.currency.data.RatesModelImpl
import com.cloverrepublic.revolut.currency.data.ResourceManager
import com.cloverrepublic.revolut.currency.data.repository.RatesRepository
import com.cloverrepublic.revolut.currency.data.repository.RatesRepositoryImpl
import com.cloverrepublic.revolut.currency.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by axti on 05.05.2020.
 */
val mainModule = module {

    single { RatesModelImpl(get()) as RatesModel }

    single { RatesRepositoryImpl(get()) as RatesRepository }

    factory { ResourceManager(androidApplication()) }

    viewModel { MainViewModel(get(), get(), get()) }
}