package app.table.calldisplay.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import app.table.calldisplay.R
import app.table.calldisplay.base.ui.BaseActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.analytics.logEvent(
            "sys_log",
            bundleOf("message" to "START APP: MainActivity.onCreate starts")
        )
    }

    override fun onBackPressed() = Unit
}
