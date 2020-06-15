package com.seiko.tv.di

import com.seiko.tv.data.prefs.PrefDataSource
import com.seiko.tv.data.prefs.PrefDataSourceImpl
import com.seiko.tv.util.constants.PREFS_NAME_COOKIES
import com.seiko.tv.util.constants.PREFS_NAME_DEFAULT
import com.seiko.tv.util.http.cookie.PersistentCookieStore
import com.seiko.common.util.createPreferenceDataStore
import org.koin.dsl.module

internal val prefModule = module {
    single { createCookieStore() }
    single { createPrefDataSource() }
}

private fun createCookieStore(): PersistentCookieStore {
    return PersistentCookieStore(
        createPreferenceDataStore(
            PREFS_NAME_COOKIES
        )
    )
}

private fun createPrefDataSource(): PrefDataSource {
    return PrefDataSourceImpl(
        createPreferenceDataStore(
            PREFS_NAME_DEFAULT
        )
    )
}