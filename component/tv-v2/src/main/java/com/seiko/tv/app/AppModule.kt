package com.seiko.tv.app

import com.seiko.tv.data.api.ApiService
import com.seiko.tv.data.api.ApiServiceGenerator
import com.seiko.tv.data.repo.ApiRepository
import com.seiko.tv.domain.GetAirDayAnimeListUseCase
import com.seiko.tv.ui.VideoViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter

val appModule = module {
    single { createApiService(get(), get()) }
    single { createApiRepository(get()) }

    single { GetAirDayAnimeListUseCase() }

    viewModel { VideoViewModel(get()) }
}

private fun createApiService(
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory
): ApiService {
    return ApiServiceGenerator(
        okHttpClient,
        converterFactory
    ).create()
}

private fun createApiRepository(
    apiService: ApiService
): ApiRepository {
    return ApiRepository(apiService)
}