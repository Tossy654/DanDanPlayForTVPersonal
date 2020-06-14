package com.seiko.tv.data.api

import com.seiko.tv.data.api.model.BangumiListResponse
import retrofit2.http.GET

interface ApiService {

    /**
     * 获取新番列表
     */
    @GET("api/v2/bangumi/shin")
    suspend fun getBangumiList(): BangumiListResponse

}