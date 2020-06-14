package com.seiko.tv.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seiko.tv.R
import com.seiko.tv.data.MovieList
import com.seiko.tv.data.VideoList
import com.seiko.tv.ui.base.TvBaseFragment
import com.seiko.tv.ui.widget.LinearEdgeDecoration
import com.seiko.tv.ui.widget.TvVerticalRecyclerView

class VideoFragment : TvBaseFragment() {

    private lateinit var recyclerview: TvVerticalRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tv_fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerview = view.findViewById(R.id.list)

        val padding = resources.getDimensionPixelSize(R.dimen.tv_safe_padding_vertical)
        recyclerview.addItemDecoration(
            LinearEdgeDecoration(
                startPadding = padding,
                endPadding = padding,
                orientation = RecyclerView.VERTICAL
            )
        )

        val adapter = VideoListAdapter(requireActivity())
        recyclerview.adapter = adapter


        val list = arrayListOf<VideoList>()
        MovieList.MOVIE_CATEGORY.forEach {
            val videoList = ArrayList(MovieList.list)
            repeat(4) {
                videoList.addAll(MovieList.list)
            }
            list.add(VideoList(it, videoList.shuffled()))
        }
        adapter.submitList(list)
    }

    override fun onFocused() {
        super.onFocused()
        recyclerview.selectCurrentPosition()
    }

    override fun onUnFocused() {
        super.onUnFocused()
        recyclerview.unSelectCurrentPosition()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return recyclerview.dispatchKeyEventAndConsumeEdges(
            event,
            consumeEdgeLeft = false,
            consumeEdgeTop = true,
            consumeEdgeRight = true,
            consumeEdgeBottom = true
        )
    }
}