package app.table.calldisplay.base.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import app.table.calldisplay.ui.MainActivity

abstract class BaseDialogFragment<T> : DialogFragment() {

    protected var binding: T? = null

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.hideNavigationBarAndStatusBar()
        binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogDescription = object : Dialog(requireContext()) {
        }
        dialogDescription.window?.run {
            WindowCompat.getInsetsController(this, this.decorView).also {
                it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                it.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
            isCancelable = false
        }
        return dialogDescription
    }
}
