package com.seiko.torrent.di

import com.seiko.core.repo.TorrentRepository
import com.seiko.core.domain.torrent.GetTorrentInfoFileUseCase
import com.seiko.torrent.service.DownloadManager
import com.seiko.torrent.service.Downloader
import com.seiko.download.torrent.TorrentEngineOptions
import org.koin.dsl.module

internal val downloadModule = module {
    single { createDownloader(get(), get(), get()) }
}

private fun createDownloader(
    options: TorrentEngineOptions,
    torrentRepo: TorrentRepository,
    getTorrentInfoFileUseCase: GetTorrentInfoFileUseCase
): Downloader {
    return DownloadManager(
        options = options,
        torrentRepo = torrentRepo,
        getTorrentInfoFileUseCase = getTorrentInfoFileUseCase
    )
}