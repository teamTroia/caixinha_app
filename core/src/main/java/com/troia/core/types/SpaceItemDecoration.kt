package com.troia.core.types

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val itemOffset: Int,
    @RecyclerView.Orientation private val orientation: Int = RecyclerView.VERTICAL,
    private val startOffset: Int = 0,
    private val endOffset: Int = startOffset,
    private val borderOffset: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val dataSize = state.itemCount
        val position = parent.getChildAdapterPosition(view)

        val start = if (dataSize > 0 && position == 0) startOffset else 0
        val end = if (dataSize > 0 && position == dataSize - 1) endOffset else itemOffset

        if (orientation == LinearLayout.VERTICAL) {
            outRect.set(borderOffset, start, borderOffset, end)
        } else {
            outRect.set(start, borderOffset, end, borderOffset)
        }
    }
}