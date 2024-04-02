package app.table.calldisplay.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import app.table.calldisplay.base.ui.BaseListAdapter
import app.table.calldisplay.databinding.ItemServiceBinding
import app.table.calldisplay.ui.home.model.Service

class ServiceListAdapter : BaseListAdapter<Service>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        ServiceViewHolder(
            ItemServiceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) :
        BaseItemViewHolder(binding.root) {

        override fun bind(data: Service) {
            binding.data = data
        }
    }
}
