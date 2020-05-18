package com.cloverrepublic.revolut.currency.di

import com.cloverrepublic.revolut.currency.BuildConfig
import com.cloverrepublic.revolut.currency.network.RatesApiService
import com.cloverrepublic.revolut.currency.utils.rx.SchedulerProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by axti on 05.05.2020.
 */
val networkModule = module {

    single(named(OKHTTP_CLIENT_STANDARD)) { createOkHttpClient() }

    single {
        createWebService<RatesApiService>(
            get(named(OKHTTP_CLIENT_STANDARD)),
            getProperty(SERVER_URL), get(), GsonConverterFactory.create()
        )
    }
}

private fun createHttpLoggingInterceptor(): Interceptor {
    return HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.HEADERS
        else
            HttpLoggingInterceptor.Level.NONE
    }
}

private fun createOkHttpClientBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder()
        .connectTimeout(500L, TimeUnit.MILLISECONDS)
        .readTimeout(500L, TimeUnit.MILLISECONDS)
        .addInterceptor(createHttpLoggingInterceptor())
}

private fun createOkHttpClient(): OkHttpClient = createOkHttpClientBuilder().build()

inline fun <reified T : Any> createWebService(
    okHttpClient: OkHttpClient,
    baseUrl: String,
    schedulerProvider: SchedulerProvider,
    converter: Converter.Factory
): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converter)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io()))
        .build()
        .create(T::class.java)
}

private const val OKHTTP_CLIENT_STANDARD = "standard"
private const val SERVER_URL = "SERVER_URL"