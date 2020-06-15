package com.seiko.tv.vm

import androidx.lifecycle.*
import com.seiko.tv.domain.bangumi.GetSeriesBangumiAirDayBeansUseCase
import com.seiko.tv.domain.bangumi.GetBangumiFavoriteUseCase
import com.seiko.tv.data.model.AirDayBangumiBean
import com.seiko.tv.data.model.HomeImageBean
import com.seiko.common.data.Result
import com.seiko.tv.domain.bangumi.GetBangumiHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*

class HomeViewModel(
    getWeekBangumiList: GetSeriesBangumiAirDayBeansUseCase,
    private val getFavoriteBangumiList: GetBangumiFavoriteUseCase,
    private val getBangumiHistoryList: GetBangumiHistoryUseCase
): ViewModel() {

    /**
     * 每周更新
     */
    val weekBangumiList: LiveData<List<AirDayBangumiBean>> =
        getWeekBangumiList.invoke(getDayOfWeek())
            .flowOn(Dispatchers.IO)
            .flatMapConcat { result ->
                flow {
                    when(result) {
                        is Result.Success -> emit(result.data)
                        is Result.Error -> Timber.e(result.exception)
                    }
                }
            }
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    /**
     * 今日更新
     */
    val todayBangumiList: LiveData<List<HomeImageBean>> = weekBangumiList.switchMap { data ->
        liveData<List<HomeImageBean>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            if (data.isNotEmpty()) {
                emit(data[0].bangumiList)
            } else {
                emit(emptyList())
            }
        }
    }

    /**
     * 我的收藏（动态）
     */
    val favoriteBangumiList: LiveData<List<HomeImageBean>> =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(getFavoriteBangumiList.invoke(10))
        }

    /**
     * 我的历史（动态），前20条
     */
    val historyBangumiList: LiveData<List<HomeImageBean>> =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(getBangumiHistoryList.invoke(10))
        }

}

/**
 * 今天周几
 * PS: 0代表周日，1-6代表周一至周六。
 */
private fun getDayOfWeek(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
}