package com.htnguyen.ihealth.support

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult


fun <T> DiffResult(old: List<T>, new: List<T>, areItemsTheSame: (T, T) -> Boolean, areContentsTheSame: (T, T) -> Boolean): DiffResult {
    return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(op: Int, np: Int) = areItemsTheSame(old[op], new[np])
        override fun areContentsTheSame(op: Int, np: Int) = areContentsTheSame(old[op], new[np])
        override fun getChangePayload(op: Int, np: Int) = new[np]
    })
}