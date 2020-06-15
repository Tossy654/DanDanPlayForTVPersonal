package com.seiko.torrent.domain

import com.seiko.common.data.Result
import com.seiko.torrent.util.constants.DATA_TORRENT_INFO_FILE_NAME
import com.seiko.torrent.util.constants.TORRENT_DATA_DIR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileNotFoundException

/**
 * 生成种子储存路径
 */
class GetTorrentInfoFileUseCase : KoinComponent {

    /**
     * @param magnet 磁力链接 magnet:?xt=urn:btih:WEORDPJIJANN54BH2GNNJ6CSN7KB7S34
     * 将'/'换成'\'
     */
    suspend operator fun invoke(magnet: String): Result<File> {
        return withContext(Dispatchers.IO) {
            // 下载路径
            val dataDir: File by inject(named(TORRENT_DATA_DIR))

            val torrentInfoDir = File(dataDir, DATA_TORRENT_INFO_FILE_NAME)


            if (!torrentInfoDir.exists() && !torrentInfoDir.mkdirs()) {
                return@withContext Result.Error(FileNotFoundException("File can't create: ${torrentInfoDir.absolutePath}"))
            }

            return@withContext Result.Success(File(torrentInfoDir, "${magnet}.torrent"))
        }
    }
}