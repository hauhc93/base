package app.table.calldisplay.base.ui

import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private val BLOCKED_KEYS = arrayOf(
            KeyEvent.KEYCODE_BACK,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
        )
    }

    override fun onResume() {
        super.onResume()

        hideNavigationBarAndStatusBar()
    }

    override fun onBackPressed() = Unit

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (BLOCKED_KEYS.contains(event?.keyCode)) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    internal fun hideNavigationBarAndStatusBar() {
        WindowCompat.getInsetsController(window, window.decorView).also {
            it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
