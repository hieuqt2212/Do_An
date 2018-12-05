package com.example.mba0229p.da_nang_travel.widgets

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import kotlinx.android.synthetic.main.item_recycler_footer.view.*

class EndlessRecyclerAdapter(private val mWrapped: RecyclerView.Adapter<RecyclerView.ViewHolder>, private val mViewState: EndlessRecyclerView.ViewState, private val mProgressHome: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            notifyItemRangeChanged(fromPosition, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onChanged() {
            super.onChanged()
            notifyDataSetChanged()
        }
    }
    val wrapped: RecyclerView.Adapter<*>
        get() = mWrapped
    private val footerCount: Int
        get() = if (mViewState.getState() == EndlessRecyclerView.STATE_HIDE) 0 else 1

    init {
        mWrapped.registerAdapterDataObserver(mAdapterDataObserver)
    }

    fun updateState() {
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mWrapped.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mWrapped.onDetachedFromRecyclerView(recyclerView)
        mWrapped.unregisterAdapterDataObserver(mAdapterDataObserver)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        mWrapped.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        mWrapped.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        mWrapped.onViewRecycled(holder)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        mWrapped.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            position.toLong()
        } else {
            mWrapped.getItemId(position)
        }
    }

    override fun getItemCount(): Int {
        return mWrapped.itemCount + footerCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mWrapped.itemCount) {
            VIEW_TYPE_FOOTER
        } else mWrapped.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            createFooterViewHolder(parent)
        } else {
            mWrapped.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (type == VIEW_TYPE_FOOTER) {
            bindFooterViewHolder(holder)
        } else {
            mWrapped.onBindViewHolder(holder, position)
        }
    }

    private fun createFooterViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = SimpleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_footer, parent, false)).apply {
        itemView.rlFooterRecycler.apply {
            if (mProgressHome) {
                setBackgroundColor(Color.TRANSPARENT)
                rlFooterRecycler.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun bindFooterViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as? SimpleViewHolder)?.bind(mViewState.getState())
    }

    /**
     * holder footer recycler view
     */
    internal class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(state: Int) {
            when (state) {
                EndlessRecyclerView.STATE_SHOW -> itemView.rlFooterRecycler.visibility = View.VISIBLE
                EndlessRecyclerView.STATE_HIDE -> itemView.rlFooterRecycler.visibility = View.GONE
                else -> {
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_FOOTER = Integer.MAX_VALUE - 1
    }
}
