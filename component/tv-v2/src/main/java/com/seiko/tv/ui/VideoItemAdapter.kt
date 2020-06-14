package com.seiko.tv.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.seiko.tv.data.Video
import com.seiko.tv.databinding.TvItemVideoBinding
import com.seiko.tv.ui.widget.TvItemViewHolder
import com.seiko.tv.util.loadImage

class VideoItemAdapter(context: Context) : ListAdapter<Video, VideoItemVideoHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return true
            }
        }
    }

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemVideoHolder {
        val binding = TvItemVideoBinding.inflate(inflater, parent, false)
        val vh = VideoItemVideoHolder(binding)
        vh.onCreated()
        return vh
    }

    override fun onBindViewHolder(holder: VideoItemVideoHolder, position: Int) {
        holder.onPreBound()
        holder.bind(getItem(position))
        holder.onBound()
    }

    override fun onViewRecycled(holder: VideoItemVideoHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

}

class VideoItemVideoHolder(
    private val binding: TvItemVideoBinding
) : TvItemViewHolder(binding.root) {

    fun bind(item: Video) {
        binding.tvImage.loadImage(itemView, item.backgroundImageUrl)
        binding.tvTitle.text = item.title
        binding.tvSubtitle.text = item.studio
    }

    override fun onFocused() {
        super.onFocused()
        binding.root.isSelected = true
        binding.tvTitle.isSelected = true
        binding.tvSubtitle.isSelected = true
    }

    override fun onUnFocused() {
        super.onUnFocused()
        binding.root.isSelected = false
        binding.tvTitle.isSelected = false
        binding.tvSubtitle.isSelected = false
    }
}