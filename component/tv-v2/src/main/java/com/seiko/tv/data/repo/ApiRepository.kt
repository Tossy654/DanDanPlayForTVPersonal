package com.seiko.tv.data.repo

import com.seiko.common.data.Result
import com.seiko.tv.data.api.ApiService
import com.seiko.tv.util.apiCall

class ApiRepository(
    private val api: ApiService
) {

    /**
     * 获取新番列表
     */
    suspend fun getSeriesBangumiList() = apiCall(
        request = { api.getBangumiList() },
        success = { Result.Success(it.bangumiList) }
    )

}