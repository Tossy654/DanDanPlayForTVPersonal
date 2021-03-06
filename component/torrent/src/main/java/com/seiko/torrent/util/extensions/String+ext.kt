package com.seiko.torrent.util.extensions

import com.seiko.torrent.util.constants.HASH_PATTERN
import com.seiko.torrent.util.constants.MAGNET_PREFIX
import java.util.*
import java.util.regex.Pattern

fun String.isMagnet(): Boolean {
    return toLowerCase(Locale.US).startsWith(MAGNET_PREFIX)
}

fun String.isHash(): Boolean {
    if (isEmpty()) return false
    val pattern = Pattern.compile(HASH_PATTERN)
    val matcher = pattern.matcher(trim { it <= ' ' })
    return matcher.matches()
}