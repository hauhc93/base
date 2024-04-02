package app.table.calldisplay.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import app.table.calldisplay.base.ui.BaseListAdapter
import app.table.calldisplay.databinding.ItemRequestOrderOldBinding
import app.table.calldisplay.ui.home.model.RequestOrder

class RequestOldListAdapter : BaseListAdapter<RequestOrder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        RequestOrderOldViewHolder(
            ItemRequestOrderOldBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class RequestOrderOldViewHolder(private val binding: ItemRequestOrderOldBinding) :
        BaseItemViewHolder(binding.root) {
        override fun bind(data: RequestOrder) {
            binding.data = data
        }
    }
}
