package app.table.calldisplay.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

internal fun Context.showKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        view.postDelayed({
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
    }
}
