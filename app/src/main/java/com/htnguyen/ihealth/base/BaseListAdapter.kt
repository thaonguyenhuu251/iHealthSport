package com.htnguyen.ihealth.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.htnguyen.ihealth.support.DiffResult


abstract class BaseListAdapter<T, V : ViewDataBinding> : RecyclerView.Adapter<BaseListAdapter.BindingHolder<V>>() {

    abstract class Scaffold<T, V : ViewDataBinding> : BaseListAdapter<T, V>() {

        var items: List<T> = ArrayList()
            private set

        abstract fun onBindViewHolder(holder: BindingHolder<V>, item: T)

        /**
         * Submit
         */
        fun submit(item: T) = submit(listOf(item))

        fun submit(items: List<T> = ArrayList()) {
            val diff = DiffResult(this.items, items, ::areItemsTheSame, ::areContentsTheSame)
            this.items = items
            diff.dispatchUpdatesTo(this)
        }


        final override fun getItemCount() = items.size

        final override fun onBindViewHolder(holder: BindingHolder<V>, position: Int) {
            onBindViewHolder(holder, items[position])
        }

        final override fun onBindViewHolder(holder: BindingHolder<V>, position: Int, payloads: MutableList<*>) {
            if (payloads.size <= 0) return onBindViewHolder(holder, position)
            @Suppress("UNCHECKED_CAST") onBindViewHolder(holder, payloads[0] as T)
        }

    }

    /**
     * Layout ID
     */
    abstract fun getLayoutId(position: Int): Int

    abstract fun areItemsTheSame(old: T, new: T): Boolean

    open fun areContentsTheSame(old: T, new: T): Boolean = old == new

    final override fun getItemViewType(position: Int): Int = getLayoutId(position)

    final override fun onCreateViewHolder(parent: ViewGroup, layout: Int): BindingHolder<V> {
        val inflater = LayoutInflater.from(parent.context)
        return BindingHolder(DataBindingUtil.inflate(inflater, layout, parent, false))
    }


    class BindingHolder<B : ViewDataBinding>(val binding: B) : ViewHolder(binding.root)

}