package app.table.calldisplay.bindingadapter

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import app.table.calldisplay.R

@BindingAdapter("bindStateColorTable")
fun TextView.bindStateColorTable(countService: Int) {
    if (countService > 1) {
        setBackgroundColor(resources.getColor(R.color.tableHighLight, null))
        setTextColor(Color.WHITE)
    } else {
        setBackgroundColor(Color.WHITE)
        setTextColor(Color.BLACK)
    }
}
