package com.dandanplay.tv.di

import android.content.Context
import com.dandanplay.tv.data.api.DanDanApiGenerator
import com.dandanplay.tv.data.api.DanDanApiService
import com.dandanplay.tv.data.api.ResDanDanApiGenerator
import com.dandanplay.tv.data.api.ResDanDanApiService
import com.dandanplay.tv.data.prefs.PrefDataSource
import com.seiko.common.http.cookie.CookiesManager
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Converter

internal val networkModule = module {
    single { createApiService(androidContext(), get(), get(), get(), get()) }
    single { createResApiService(get(), get()) }
}

private fun createApiService(
    context: Context,
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory,
    cookiesManager: CookiesManager,
    prefDataSource: PrefDataSource
): DanDanApiService {
    return DanDanApiGenerator(context,
        okHttpClient,
        converterFactory,
        cookiesManager,
        prefDataSource
    ).create()
}

private fun createResApiService(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory
): ResDanDanApiService {
    return ResDanDanApiGenerator(
        okHttpClient,
        converterFactory
    ).create()
}
