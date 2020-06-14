package com.seiko.tv.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.BaseGridView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seiko.tv.R
import com.seiko.tv.data.model.AirDayAnimeBean
import com.seiko.tv.databinding.TvItemVideoListBinding
import com.seiko.tv.ui.widget.LinearEdgeDecoration
import com.seiko.tv.ui.widget.TvListViewHolder
import com.seiko.tv.ui.widget.TvViewHolderScrollState

class VideoListAdapter(
    private val context: Context
) : ListAdapter<AirDayAnimeBean, VideoListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AirDayAnimeBean>() {
            override fun areItemsTheSame(
                oldItem: AirDayAnimeBean,
                newItem: AirDayAnimeBean
            ): Boolean {
                return oldItem.day == newItem.day
            }
            override fun areContentsTheSame(
                oldItem: AirDayAnimeBean,
                newItem: AirDayAnimeBean
            ): Boolean {
                return oldItem.animeList.size == newItem.animeList.size
            }
        }
    }

    private val inflater = LayoutInflater.from(context)
    private val scrollState = TvViewHolderScrollState()
    private val pool = RecyclerView.RecycledViewPool().apply {
        setMaxRecycledViews(0, 50)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val binding = TvItemVideoListBinding.inflate(inflater, parent, false)
        val vh = VideoListViewHolder(context, binding, scrollState, pool)
        vh.onCreated()
        return vh
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.onBound()
    }

    override fun onViewDetachedFromWindow(holder: VideoListViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    override fun onViewRecycled(holder: VideoListViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        scrollState.clear()
    }
}

class VideoListViewHolder(
    context: Context,
    private val binding: TvItemVideoListBinding,
    scrollState: TvViewHolderScrollState,
    pool: RecyclerView.RecycledViewPool
) : TvListViewHolder(binding.root, scrollState) {

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

    fun bind(item: AirDayAnimeBean) {
        binding.tvTitle.text = getWeekName(item.day)
        adapter.submitList(item.animeList)
    }

    override fun getRecyclerView(): BaseGridView {
        return binding.list
    }

    override fun getScrollKey(): String? {
        return binding.tvTitle.text.toString()
    }

    override fun applyFocusUiState(hasFocus: Boolean) {
        super.applyFocusUiState(hasFocus)
        binding.tvTitle.alpha = if (hasFocus) 1.0f else 0.3f
    }
}

private fun getWeekName(id: Int) = when(id) {
    0 -> "周日"
    1 -> "周一"
    2 -> "周二"
    3 -> "周三"
    4 -> "周四"
    5 -> "周五"
    6 -> "周六"
    else -> ""
}