package app.table.calldisplay.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import app.table.calldisplay.R
import app.table.calldisplay.data.local.model.AppConstant
import app.table.calldisplay.ui.home.model.Service

@BindingAdapter("bindTableService")
fun ImageView.bindTableService(service: Service?) {
    service?.let {
        val icon: Int = when (service.id) {
            AppConstant.SERVICE_STAFF.service -> R.drawable.ic_staff
            AppConstant.SERVICE_TAKE_OUT.service -> R.drawable.ic_take_out
            AppConstant.SERVICE_DISH.service -> R.drawable.ic_dish
            AppConstant.SERVICE_DESSERT.service -> R.drawable.ic_dessert
            else -> 0
        }
        if (icon > 0) {
            setImageResource(icon)
        }
    }
}
