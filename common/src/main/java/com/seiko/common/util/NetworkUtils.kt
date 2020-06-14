package com.seiko.common.util

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class OkhttpInstance {
    private object Holder {
        internal val instance = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    companion object {
        val instance get() = Holder.instance
    }
}