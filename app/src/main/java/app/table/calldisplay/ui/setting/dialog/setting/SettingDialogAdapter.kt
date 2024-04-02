package app.table.calldisplay.ui.setting.dialog.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.table.calldisplay.R
import app.table.calldisplay.data.local.model.DialogType
import app.table.calldisplay.data.local.model.SettingItemData
import app.table.calldisplay.databinding.ItemDialogSettingBinding

class SettingDialogAdapter(
    private val items: List<SettingItemData>,
    private var onCheckerChangerListener: (Int) -> Unit = {}
) : RecyclerView.Adapter<SettingDialogAdapter.SettingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        return SettingViewHolder(
            ItemDialogSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onCheckerChangerListener
        )
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size

    class SettingViewHolder(
        private val view: ItemDialogSettingBinding,
        private var onCheckerChangerListener: (Int) -> Unit = {}
    ) : RecyclerView.ViewHolder(view.root) {

        init {
            view.radio.setOnClickListener {
                onCheckerChangerListener.invoke(adapterPosition)
            }
        }

        fun onBind(item: SettingItemData) {
            view.run {
                radio.isChecked = item.isState
                when (item.typeOfDialog) {
                    DialogType.SOUND.ordinal -> radio.text = item.ringtoneName
                    DialogType.REQUEST.ordinal -> radio.text =  view.root.context.getString(R.string.textViewRequestNumber,item.ringtoneName)
                    else -> {
                        if(item.timer == 0){
                            radio.text =  view.root.context.getString(R.string.textViewDisableAutoDelete)
                        }else{
                            radio.text =  view.root.context.getString(R.string.textViewTimeNumber,item.timer)
                        }
                    }
                }
            }
        }
    }
}
