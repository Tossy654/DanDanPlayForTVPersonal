package com.seiko.tv.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.seiko.common.util.OkhttpInstance
import com.seiko.tv.R
import java.io.InputStream

fun ImageView.loadImage(url: String?) {
    TvGlide.with(this).loadImage(url, this)
}

fun GlideRequests.loadImage(url: String?, imageView: ImageView) {
    this.load(url)
        .override(160, 90)
        .apply(RequestOptions().placeholder(R.drawable.tv_picture_icon_placeholder))
        .centerCrop()
        .into(imageView)
}

@GlideModule(glideName = "TvGlide")
open class TvGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(
            RequestOptions().format(DecodeFormat.PREFER_RGB_565)
        )

        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(2f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
    }

    // 禁止解析Manifest文件,提升初始化速度，避免一些潜在错误
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    // 注册自定义组件
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(OkhttpInstance.instance)
        )
    }
}