package com.seiko.player.di

import com.seiko.common.util.createPreferenceDataStore
import com.seiko.player.util.constants.PREFS_NAME_DEFAULT
import com.seiko.player.data.prefs.PrefDataSource
import com.seiko.player.data.prefs.PrefDataSourceImpl
import org.koin.dsl.module

internal val prefModule = module {
    single { createPrefDataSource() }
}

private fun createPrefDataSource(): PrefDataSource {
    return PrefDataSourceImpl(
        createPreferenceDataStore(
            PREFS_NAME_DEFAULT
        )
    )
}