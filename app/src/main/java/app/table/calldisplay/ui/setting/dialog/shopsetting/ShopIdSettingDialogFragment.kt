package app.table.calldisplay.ui.setting.dialog.shopsetting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import app.table.calldisplay.base.ui.BaseDialogFragment
import app.table.calldisplay.databinding.FragmentDialogShopidSettingBinding
import app.table.calldisplay.extensions.onClick
import app.table.calldisplay.extensions.showKeyboard
import app.table.calldisplay.ui.setting.SettingViewModel

class ShopIdSettingDialogFragment : BaseDialogFragment<FragmentDialogShopidSettingBinding>() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
    }

    companion object {

        fun newInstance() = ShopIdSettingDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogShopidSettingBinding.inflate(inflater)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        binding?.edtShopId?.run {
            hint = viewModel.getShopId().value
            requestFocus()
            context?.showKeyboard(this)
        }
    }

    private fun initListener() {
        binding?.run {
            tvCancel.onClick {
                dismiss()
            }

            tvOk.onClick {
                if (edtShopId.text?.isNotEmpty() == true) {
                    viewModel.saveShopId(edtShopId.text.toString().toBigInteger().toString())
                }
                dismiss()
            }
        }
    }
}
