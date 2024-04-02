package app.table.calldisplay.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import app.table.calldisplay.base.ui.BaseListAdapter
import app.table.calldisplay.databinding.ItemRequestOrderBinding
import app.table.calldisplay.ui.home.model.RequestOrder

class RequestListAdapter : BaseListAdapter<RequestOrder>() {
    internal var onItemLongClicked: (requestOrder: RequestOrder) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        RequestOrderViewHolder(
            ItemRequestOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class RequestOrderViewHolder(private val binding: ItemRequestOrderBinding) :
        BaseItemViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener {
                onItemLongClicked.invoke(getItem(adapterPosition))
                true
            }
        }

        override fun bind(data: RequestOrder) {
            binding.data = data
        }
    }
}
