package com.seiko.tv.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.BaseGridView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seiko.tv.R
import com.seiko.tv.data.VideoList
import com.seiko.tv.databinding.TvItemVideoListBinding
import com.seiko.tv.ui.widget.LinearEdgeDecoration
import com.seiko.tv.ui.widget.TvListViewHolder
import com.seiko.tv.ui.widget.TvViewHolderScrollState

class VideoListAdapter(
    private val context: Context
) : ListAdapter<VideoList, VideoListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoList>() {
            override fun areItemsTheSame(oldItem: VideoList, newItem: VideoList): Boolean {
                return oldItem.title == newItem.title
            }
            override fun areContentsTheSame(oldItem: VideoList, newItem: VideoList): Boolean {
                return oldItem.videos.size == newItem.videos.size
            }
        }
    }

    private val inflater = LayoutInflater.from(context)
    private val scrollState = TvViewHolderScrollState()
    private val pool = RecyclerView.RecycledViewPool().apply {
        setMaxRecycledViews(0, 30)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val binding = TvItemVideoListBinding.inflate(inflater, parent, false)
        val vh = VideoListViewHolder(context, binding, scrollState, pool)
        vh.onCreated()
        return vh
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.onPreBound()
        holder.bind(getItem(position))
        holder.onBound()
    }

    override fun onViewRecycled(holder: VideoListViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }
}

class VideoListViewHolder(
    context: Context,
    private val binding: TvItemVideoListBinding,
    scrollState: TvViewHolderScrollState,
    pool: RecyclerView.RecycledViewPool
) : TvListViewHolder(binding.root, scrollState) {

    private var videoList: VideoList? = null
    private val adapter = VideoItemAdapter(context)

    init {
        val padding = context.resources.getDimensionPixelSize(R.dimen.tv_safe_padding_horizontal)
        binding.list.addItemDecoration(
            LinearEdgeDecoration(
                startPadding = padding,
                orientation = RecyclerView.HORIZONTAL
            )
        )
        val spacing = context.resources.getDimensionPixelSize(R.dimen.tv_item_spacing)
        binding.list.setItemSpacing(spacing)
        binding.list.setRecycledViewPool(pool)
        binding.list.adapter = adapter
    }

    fun bind(item: VideoList) {
        videoList = item
        binding.tvTitle.text = item.title
        adapter.submitList(item.videos)
    }

    override fun getRecyclerView(): BaseGridView {
        return binding.list
    }

    override fun getScrollKey(): String? {
        return videoList?.title
    }

    override fun onFocused() {
        super.onFocused()
        binding.tvTitle.alpha = 1.0f
    }

    override fun onUnFocused() {
        super.onUnFocused()
        binding.tvTitle.alpha = 0.3f
    }
}