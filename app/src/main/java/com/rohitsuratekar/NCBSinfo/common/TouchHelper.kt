package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.EditTransportTripAdapter

class TouchHelper {

    companion object {

        fun get(context: Context, recyclerView: RecyclerView): ItemTouchHelper.SimpleCallback {

            return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                // we want to cache these and not allocate anything repeatedly in the onChildDraw method
                lateinit var background: Drawable
                lateinit var xMark: Drawable
                var xMarkMargin: Int = 0
                var initiated: Boolean = false
                //TODO: Handle setColorFilter
                private fun init() {
                    background = ColorDrawable(Color.RED)
                    xMark = ContextCompat.getDrawable(context, R.drawable.icon_cancel)!!
                    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    xMarkMargin =
                        context.resources.getDimension(R.dimen.et_trips_undo_padding).toInt()
                    initiated = true
                }

                // not important, we don't want drag & drop
                override fun onMove(
                    recyclerView1: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun getSwipeDirs(
                    recyclerView1: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val position = viewHolder.adapterPosition
                    val testAdapter = recyclerView1.adapter as EditTransportTripAdapter
                    return if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                        0
                    } else super.getSwipeDirs(recyclerView1, viewHolder)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val swipedPosition = viewHolder.adapterPosition
                    val adapter = recyclerView.adapter as EditTransportTripAdapter
                    val undoOn = adapter.isUndoOn()
                    if (undoOn) {
                        adapter.pendingRemoval(swipedPosition)
                    } else {
                        adapter.remove(swipedPosition)
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView1: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView

                    // not sure why, but this method get's called for viewholder that are already swiped away
                    if (viewHolder.adapterPosition == -1) {
                        // not interested in those
                        return
                    }

                    if (!initiated) {
                        init()
                    }

                    // draw red background
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)

                    // draw x mark
                    val itemHeight = itemView.bottom - itemView.top
                    val intrinsicWidth = xMark.intrinsicWidth
                    val intrinsicHeight = xMark.intrinsicWidth

                    val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
                    val xMarkRight = itemView.right - xMarkMargin
                    val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val xMarkBottom = xMarkTop + intrinsicHeight
                    xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)

                    xMark.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView1,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

            }
        }
    }
}
