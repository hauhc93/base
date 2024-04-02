package app.table.calldisplay.ui.home.deletedialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.table.calldisplay.R
import app.table.calldisplay.base.ui.BaseDialogFragment
import app.table.calldisplay.databinding.DialogConfirmDeleteRequestBinding
import app.table.calldisplay.extensions.onClick

class ConfirmDeleteRequestDialog : BaseDialogFragment<DialogConfirmDeleteRequestBinding>() {
    internal var onConfirmClicked: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogConfirmDeleteRequestBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun getTheme(): Int = R.style.DialogRadius8

    private fun initListeners() {
        binding?.btnConfirm?.onClick {
            dismiss()
            onConfirmClicked.invoke()
        }
        binding?.btnCancel?.onClick {
            dismiss()
        }
    }
}
