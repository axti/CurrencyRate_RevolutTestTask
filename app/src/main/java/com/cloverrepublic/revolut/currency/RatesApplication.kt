package com.cloverrepublic.revolut.currency

import android.app.Application
import com.cloverrepublic.revolut.currency.di.mainModule
import com.cloverrepublic.revolut.currency.di.networkModule
import com.cloverrepublic.revolut.currency.di.rxModule
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by axti on 05.05.2020.
 */
class RatesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@RatesApplication)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            modules(listOf(mainModule, networkModule, rxModule))
        }
        configurePicasso()
    }

    private fun configurePicasso() {
        val builder = Picasso.Builder(this).apply {
            loggingEnabled(BuildConfig.DEBUG)
            indicatorsEnabled(BuildConfig.DEBUG)
        }
        Picasso.setSingletonInstance(builder.build())
    }
}