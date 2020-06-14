package com.seiko.tv.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.seiko.common.data.Result
import com.seiko.tv.data.model.AirDayAnimeBean
import com.seiko.tv.domain.GetAirDayAnimeListUseCase
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class VideoViewModel(
    getSeriesBangumiList: GetAirDayAnimeListUseCase
) : ViewModel() {

    /**
     * 每周的新番集合
     */
    val weekAnimeList: LiveData<List<AirDayAnimeBean>>
        = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            when(val result = getSeriesBangumiList.invoke()) {
                is Result.Success -> emit(result.data)
                is Result.Error -> Timber.e(result.exception)
            }
        }

}