package com.seiko.common.di

import com.seiko.common.util.OkhttpInstance
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

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