package com.seiko.tv.data.api.model

import com.squareup.moshi.JsonClass

//BangumiListResponse {
//    bangumiList (Array[BangumiIntro], optional): 番剧列表 ,
//    errorCode (integer): 错误代码，0表示没有发生错误，非0表示有错误，详细信息会包含在errorMessage属性中 ,
//    success (boolean, read only): 接口是否调用成功 ,
//    errorMessage (string, optional, read only): 当发生错误时，说明错误具体原因
//}
@JsonClass(generateAdapter = true)
data class BangumiListResponse(
    val bangumiList: List<BangumiIntroEntity>
) : JsonResultResponse() {

    //BangumiIntro {
    //    animeId (integer): 作品编号 ,
    //    animeTitle (string, optional): 作品标题 ,
    //    imageUrl (string, optional): 海报图片地址 ,
    //    searchKeyword (string, optional): 搜索关键词 ,
    //    isOnAir (boolean): 是否正在连载中 ,
    //    airDay (integer): 周几上映，0代表周日，1-6代表周一至周六 ,
    //    isFavorited (boolean): 当前用户是否已关注（无论是否为已弃番等附加状态） ,
    //    isRestricted (boolean): 是否为限制级别的内容（例如属于R18分级） ,
    //    rating (number): 番剧综合评分（综合多个来源的评分求出的加权平均值，0-10分）
    //}
    @JsonClass(generateAdapter = true)
    data class BangumiIntroEntity(
        var animeId: Long = 0,
        var animeTitle: String = "",
        var imageUrl: String = "",
        var searchKeyword: String = "",
        var isOnAir: Boolean = false,
        var airDay: Int = 0,
        var isFavorited: Boolean = false,
        var isRestricted: Boolean = false,
        var rating: Float = 0f
    )
}