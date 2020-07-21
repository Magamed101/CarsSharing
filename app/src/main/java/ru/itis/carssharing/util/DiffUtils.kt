package ru.itis.carssharing.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun <T> RecyclerView.Adapter<out RecyclerView.ViewHolder>.applyDiff(
    oldList: List<T>,
    newList: List<T>,
    areItemsTheSame: (T, T) -> Boolean = { _, _ -> false },
    areContentsTheSame: (T, T) -> Boolean = { _, _ -> false }
) {
    DiffUtil.calculateDiff(
        object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }
    ).dispatchUpdatesTo(this)
}