package com.seiko.tv.util

import com.seiko.tv.data.api.model.BangumiListResponse
import com.seiko.tv.data.model.AnimeBean

fun BangumiListResponse.BangumiIntroEntity.toBean(): AnimeBean {
    return AnimeBean(
        id = animeId,
        title = animeTitle,
        imageUrl = imageUrl,
        status = getBangumiStatus(isOnAir, 0, airDay)
    )
}

private fun getBangumiStatus(isOnAir: Boolean, episodesList: Int, airDay: Int): String {
    if (!isOnAir) {
        return if (episodesList > 0) "已完结 · ${episodesList}话全" else "已完结"
    }

    val onAirDay = when(airDay) {
        0 -> "每周日更新"
        1 -> "每周一更新"
        2 -> "每周二更新"
        3 -> "每周三更新"
        4 -> "每周四更新"
        5 -> "每周五更新"
        6 -> "每周六更新"
        else -> "更新时间未知"
    }
    return "连载中 · $onAirDay"
}
