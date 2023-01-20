package id.dwichan.coreandroidapp.ui

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import id.dwichan.coreandroidapp.R
import id.dwichan.coreandroidapp.diffutil.DataDiffUtilCallback
import id.dwichan.coreandroidapp.model.LoadingModel
import id.dwichan.coreandroidapp.model.Model

abstract class AndroidRecyclerView: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: MutableList<Model> = arrayListOf()

    private var page = 1

    private var totalItemBeforeLoadMore: Int? = null
    private var totalItemCount: Int? = null
    private var lastVisibleItem: Int? = null
    private var pastVisibleItem: IntArray = intArrayOf()

    // state
    private var isLoadingMore = false
    private var isDataEnd = false
    private var isRefresh = false

    /**
     * Implements Load More support
     *
     * @param recyclerView RecyclerView Component
     * @param visibleThreshold (optional) default 1
     * @param onLoadMore action when Load More is triggered
     */
    fun implementLoadMore(
        recyclerView: RecyclerView,
        visibleThreshold: Int = 1,
        onLoadMore: (page: Int) -> Unit
    ) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null) {
            when (layoutManager) {
                is LinearLayoutManager -> {
                    totalItemBeforeLoadMore = layoutManager.itemCount
                    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            totalItemCount = layoutManager.itemCount
                            lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                            if (!isRefresh && !isDataEnd && !isLoadingMore && totalItemCount!! <= (lastVisibleItem!! + visibleThreshold)) {
                                page++
                                onLoadMore(page)
                                isLoadingMore = true
                            }
                        }
                    })
                }
                is StaggeredGridLayoutManager -> {
                    totalItemBeforeLoadMore = layoutManager.itemCount
                    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            totalItemCount = layoutManager.itemCount
                            pastVisibleItem = layoutManager.findLastVisibleItemPositions(null)
                            if (!isRefresh && !isDataEnd && !isLoadingMore && totalItemCount!! <= (getBiggerValues(pastVisibleItem) + visibleThreshold)) {
                                page++
                                onLoadMore(page)
                                isLoadingMore = true
                            }
                        }
                    })
                }
                else -> {
                    Log.e("AndroidRecyclerView", recyclerView.context.getString(R.string.message_recycler_layout_manager_not_supported))
                }
            }
        } else {
            Log.e("AndroidRecyclerView", recyclerView.context.getString(R.string.message_recycler_no_layout_manager))
        }
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: List<Model>) {
        val diffUtil = DataDiffUtilCallback(this.data, data)
        val diffUtilCalculate = DiffUtil.calculateDiff(diffUtil)
        this.data.clear()
        this.data.addAll(data)
        diffUtilCalculate.dispatchUpdatesTo(this)
    }

    fun addData(item: Model) {
        data.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun addData(item: Model, position: Int) {
        data.add(item)
        notifyItemInserted(position)
    }

    fun changeData(item: Model, position: Int) {
        data.removeAt(position)
        data.add(position, item)
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int) {
        if (position < itemCount) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteItem(item: Model) {
        data.remove(item)
    }

    fun clearData() {
        val diffUtil = DataDiffUtilCallback(this.data, arrayListOf())
        val diffUtilCalculate = DiffUtil.calculateDiff(diffUtil)
        this.data.clear()
        diffUtilCalculate.dispatchUpdatesTo(this)
    }

    fun performDataLoading() {
        isLoadingMore = true
        addData(LoadingModel())
    }

    fun performDataLoaded() {
        if (isLoadingMore) {
            isLoadingMore = false
            if (itemCount > 0) {
                if (data[itemCount - 1] is LoadingModel) deleteItem(itemCount - 1)
            }
        }
    }

    fun getLoadMoreState(): Boolean = isLoadingMore

    fun getData(): List<Model> = data

    private fun getBiggerValues(array: IntArray): Int {
        return when {
            array[0] > array[1] -> array[0]
            array[0] < array[1] -> array[1]
            array[0] == array[1] -> array[0]
            else -> 0
        }
    }
}