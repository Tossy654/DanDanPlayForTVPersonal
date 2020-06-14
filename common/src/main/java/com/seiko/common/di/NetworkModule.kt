package com.seiko.common.di

import android.content.Context
import com.seiko.common.util.OkhttpInstance
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { createSingleHttpClient() }
    single { createConverterFactory(get()) }
}

private fun createSingleHttpClient(): OkHttpClient {
    return OkhttpInstance.instance
}

private fun createConverterFactory(moshi: Moshi): Converter.Factory {
    return MoshiConverterFactory.create(moshi)
}