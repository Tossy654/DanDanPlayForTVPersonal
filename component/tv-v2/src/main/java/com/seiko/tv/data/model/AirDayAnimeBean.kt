package com.seiko.tv.data.model

data class AirDayAnimeBean(
    val day: Int,
    val animeList: MutableList<AnimeBean> = mutableListOf()
)