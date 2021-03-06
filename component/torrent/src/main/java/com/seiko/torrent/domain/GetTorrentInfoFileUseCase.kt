package com.seiko.torrent.domain

import com.seiko.common.data.Result
import com.seiko.torrent.util.constants.DATA_TORRENT_INFO_FILE_NAME
import com.seiko.torrent.util.constants.TORRENT_DATA_DIR
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileNotFoundException

/**
 * 生成种子储存路径
 */
class GetTorrentInfoFileUseCase : KoinComponent {

//    private val prefHelper: PrefDataSource by inject()

    /**
     * @param magnet 磁力链接 magnet:?xt=urn:btih:WEORDPJIJANN54BH2GNNJ6CSN7KB7S34
     * 将'/'换成'\'
     */
    operator fun invoke(magnet: String): Result<File> {
        // 下载路径
        val dataDir: File by inject(named(TORRENT_DATA_DIR))

        val torrentInfoDir = File(dataDir, DATA_TORRENT_INFO_FILE_NAME)


        if (!torrentInfoDir.exists() && !torrentInfoDir.mkdirs()) {
            return Result.Error(FileNotFoundException("File can't create: ${torrentInfoDir.absolutePath}"))
        }

//        val torrentName = if (magnet.length > 32) magnet.substring(0, 32) else magnet
        return Result.Success(File(torrentInfoDir, "${magnet}.torrent"))
    }
}