package id.dwichan.coreandroidapp.diffutil

import androidx.recyclerview.widget.DiffUtil
import id.dwichan.coreandroidapp.model.Model

class DataDiffUtilCallback(
    private val oldList: List<Model>,
    private val newList: List<Model>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList.size == newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}