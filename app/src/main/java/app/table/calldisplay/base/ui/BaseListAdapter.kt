package app.table.calldisplay.base.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T>(diffCallBack: BaseDiffUtilItemCallback<T> = BaseDiffUtilItemCallback()) :
    ListAdapter<T, BaseListAdapter<T>.BaseItemViewHolder>(diffCallBack) {

    override fun onBindViewHolder(holder: BaseItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder Abstract
     */
    open inner class BaseItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal open fun bind(data: T) = Unit
    }

    /**
     * ItemCallback Abstract
     */
    @SuppressLint("DiffUtilEquals")
    open class BaseDiffUtilItemCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem
    }
}
