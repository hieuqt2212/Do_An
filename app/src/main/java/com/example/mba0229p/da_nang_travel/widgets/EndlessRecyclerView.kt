package com.example.mba0229p.da_nang_travel.widgets

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class EndlessRecyclerView : RecyclerView {
    private val mRecyclerView = this
    private val mViewState = ViewState()
    private var mAdapter: EndlessRecyclerAdapter? = null
    private var mLoadMoreListener: ((EndlessRecyclerView) -> Unit)? = null
    private var mProgressHome: Boolean = false
    @Volatile
    var mScrollLoadType = ScrollType.FIRST_LOAD

    private val mEndlessScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (mViewState.mode != MODE_AUTO) {
                return
            }
            if (mScrollLoadType == ScrollType.FIRST_LOAD || mScrollLoadType == ScrollType.PENDING ||
                    mScrollLoadType == ScrollType.DONE) {
                return
            }
            val recyclerViewHelper = RecyclerViewHelper(recyclerView)
            val threshold = mViewState.threshold
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = recyclerViewHelper.itemCount
            val firstVisibleItem = recyclerViewHelper.findFirstVisibleItemPosition()
            if (totalItemCount - visibleItemCount <= firstVisibleItem + threshold) {
                mViewState.incIndex()
                setLoading(ScrollType.PENDING)
                mLoadMoreListener?.invoke(mRecyclerView)
            }
        }
    }

    val isLoadingMore: Boolean
        get() {
            return when (mScrollLoadType) {
                EndlessRecyclerView.ScrollType.IN_PROGRESS, EndlessRecyclerView.ScrollType.PENDING -> true
                EndlessRecyclerView.ScrollType.FIRST_LOAD, EndlessRecyclerView.ScrollType.DONE -> false
            }
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        super.setLayoutManager(LinearLayoutManager(context))
        addOnScrollListener(mEndlessScrollListener)
    }

    private fun updateState() {
        mAdapter?.updateState()
    }

    fun showProgress() {
        if (mViewState.mode != MODE_NONE) {
            mViewState.setState(STATE_SHOW)
            updateState()
        }
    }

    fun hideProgress() {
        mViewState.setState(STATE_HIDE)
        updateState()
    }

    fun setLoading(scrollType: ScrollType) {
        mScrollLoadType = scrollType
        when (scrollType) {
            EndlessRecyclerView.ScrollType.IN_PROGRESS -> showProgress()
            EndlessRecyclerView.ScrollType.PENDING -> {
            }
            EndlessRecyclerView.ScrollType.FIRST_LOAD, EndlessRecyclerView.ScrollType.DONE -> hideProgress()
        }
    }

    fun setOnLoadMoreListener(listener: ((EndlessRecyclerView) -> Unit)?): EndlessRecyclerView {
        mLoadMoreListener = listener
        return this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnScrollListener(mEndlessScrollListener)
    }

    /**
     * Check scroll Listener
     *
     * @param isRemoved true/false
     */
    fun changeOnScrollListener(isRemoved: Boolean) {
        if (isRemoved) {
            removeOnScrollListener(mEndlessScrollListener)
        } else {
            addOnScrollListener(mEndlessScrollListener)
        }
    }

    override fun getAdapter(): Adapter<*>? {
        return mAdapter
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (adapter == null) {
            mAdapter = null
            super.setAdapter(null)
        } else {
            @Suppress("UNCHECKED_CAST")
            (adapter as? Adapter<ViewHolder>)?.run {
                mAdapter = EndlessRecyclerAdapter(this, mViewState, mProgressHome)
            }
            super.setAdapter(mAdapter)
        }
    }

    fun setProgressLoadMoreHome() {
        mProgressHome = true
    }

    /**
     * Scroll Type enum
     */
    enum class ScrollType {
        FIRST_LOAD,
        IN_PROGRESS,
        PENDING,
        DONE
    }

    /**
     * View state
     */
    class ViewState {
        var mode: Int = 0
            private set
        private var state: Int = 0
        var threshold: Int = 0
            private set
        private var index: Int = 0
        private var text: CharSequence? = null

        constructor() {
            reset()
        }

        constructor(s: ViewState) {
            this.mode = s.mode
            this.state = s.state
            this.threshold = s.threshold
            this.index = s.index
        }

        fun copy(): ViewState {
            return ViewState(this)
        }

        private fun reset() {
            mode = MODE_AUTO
            state = STATE_HIDE
            threshold = DEFAULT_THRESHOLD
            index = 0
        }

        fun getState(): Int {
            return state
        }

        fun setState(s: Int): ViewState {
            this.state = s
            return this
        }

        fun incIndex(): ViewState {
            this.index++
            return this
        }

        fun getText(): CharSequence? {
            return text
        }

        fun setText(t: CharSequence): ViewState {
            this.text = t
            return this
        }

        override fun toString(): String {
            return "ViewState{" +
                    "display=" + state +
                    ", mode=" + mode +
                    ", threshold=" + threshold +
                    '}'.toString()
        }
    }

    companion object {
        val TAG = EndlessRecyclerView::class.java.simpleName
        val DEFAULT_THRESHOLD = 3
        val STATE_HIDE = 0
        val STATE_SHOW = 1
        val MODE_AUTO = 0
        val MODE_NONE = 1
    }
}