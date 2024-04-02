package app.table.calldisplay.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("bindGoneIf")
fun View.goneIf(isGone: Boolean) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}
