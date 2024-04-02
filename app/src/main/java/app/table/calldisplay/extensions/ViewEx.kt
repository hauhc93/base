package app.table.calldisplay.extensions

import android.view.View
import app.table.calldisplay.util.DebounceOnClickListener

fun View.onClick(interval: Long = 400L, listenerBlock: (View) -> Unit) =
    setOnClickListener(DebounceOnClickListener(interval, listenerBlock))