package com.seiko.tv.domain

import com.seiko.common.data.Result
import com.seiko.tv.data.api.model.BangumiListResponse
import com.seiko.tv.data.model.AirDayAnimeBean
import com.seiko.tv.data.model.AnimeBean
import com.seiko.tv.data.repo.ApiRepository
import com.seiko.tv.util.toBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class GetAirDayAnimeListUseCase : KoinComponent {

    private val repo: ApiRepository by inject()

    suspend operator fun invoke(): Result<List<AirDayAnimeBean>> {
        val list = when(val result = repo.getSeriesBangumiList()) {
            is Result.Success -> result.data
            is Result.Error -> return result
        }
        // 今天第周几
        val weekDay = getDayOfWeek()
        // 每周新番集合
        val resultList = getAirDayAnimeList(weekDay, list)
        return Result.Success(resultList)
    }

}

private suspend fun getAirDayAnimeList(
    weekDay: Int,
    list: List<BangumiListResponse.BangumiIntroEntity>
): List<AirDayAnimeBean> {
    return withContext(Dispatchers.Default) {
        // 按顺序生成 周日 ~ 周六 数据
        val weekDays = listOf(
            AirDayAnimeBean(0),
            AirDayAnimeBean(1),
            AirDayAnimeBean(2),
            AirDayAnimeBean(3),
            AirDayAnimeBean(4),
            AirDayAnimeBean(5),
            AirDayAnimeBean(6)
        )

        // 导入动漫信息
        for (item in list) {
            weekDays[item.airDay].animeList.add(item.toBean())
        }

        // 0 1 2 3 4 5 6
        // day = 3
        // 3 ~ 7, 0 ~ 3 = 3 4 5 6 0 1 2
        // day = 0
        // 0 ~ 7, 0 ~ 0 = 0 1 2 3 4 5 6
        // day = 6
        // 6 ~ 7, 0 ~ 6 = 6 0 1 2 3 4 5
        val descList = weekDays.subList(weekDay, 7) + weekDays.subList(0, weekDay)

        // day = 3
        // 3 2 1 0 6 5 4
        // day = 0
        // 0 6 5 4 3 2 1
        // day = 6
        // 6 5 4 3 2 1 0
        descList.subList(0, 1) + descList.subList(1, 7).asReversed()
    }
}

/**
 * 今天周几
 * PS: 0代表周日，1-6代表周一至周六。
 */
private fun getDayOfWeek(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
}