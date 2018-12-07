package com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.mba0229p.da_nang_travel.R

/**
 * @author by phuongdn on 25/06/2018.
 */
class ItemMapViewDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val total = parent.adapter?.itemCount
        val currentPosition = parent.getChildAdapterPosition(view)
        when (currentPosition) {
            0 -> {
                outRect.top = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_top)
                outRect.left = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_center)
                outRect.right = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_normal)
                outRect.bottom = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_bottom)
            }
            (total?.minus(1)) -> {
                outRect.top = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_top)
                outRect.left = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_normal)
                outRect.right = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_center)
                outRect.bottom = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_bottom)
            }
            else -> {
                outRect.top = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_top)
                outRect.left = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_normal)
                outRect.right = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_normal)
                outRect.bottom = view.context.resources.getDimensionPixelOffset(R.dimen.item_todo_map_margin_horizontal_bottom)
            }
        }
    }
}
