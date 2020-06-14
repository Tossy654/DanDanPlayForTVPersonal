package com.seiko.tv.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.seiko.tv.R
import com.seiko.tv.ui.base.TvBaseFragment
import com.seiko.tv.ui.widget.LinearEdgeDecoration
import com.seiko.tv.ui.widget.TvVerticalRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoFragment : TvBaseFragment() {

    private val viewModel: VideoViewModel by viewModel()

    private lateinit var recyclerview: TvVerticalRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerview = TvVerticalRecyclerView(inflater.context)
        return recyclerview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

        viewModel.weekAnimeList.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })
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