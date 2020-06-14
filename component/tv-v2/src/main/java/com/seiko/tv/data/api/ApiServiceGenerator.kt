package com.seiko.tv.data.api

import com.seiko.tv.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

class ApiServiceGenerator(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory
) {

    companion object {
        private const val DANDAN_API_BASE_URL = "https://api.acplay.net/"
    }

    private val newOkHttpClient = okHttpClient.newBuilder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }

    private val retrofit = Retrofit.Builder()
        .baseUrl(DANDAN_API_BASE_URL)
        .callFactory(newOkHttpClient.build())
        .addConverterFactory(converterFactory)

    fun create(): ApiService {
        return retrofit.build().create(ApiService::class.java)
    }

}