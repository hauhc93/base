package app.table.calldisplay.ui.setting.dialog.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import app.table.calldisplay.base.ui.BaseDialogFragment
import app.table.calldisplay.data.local.model.SettingItemData
import app.table.calldisplay.databinding.FragmentDialogSettingBinding
import app.table.calldisplay.extensions.onClick
import app.table.calldisplay.ui.setting.SettingFragment
import app.table.calldisplay.ui.setting.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class SettingDialogFragment : BaseDialogFragment<FragmentDialogSettingBinding>() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
    }
    private var adapter: SettingDialogAdapter? = null

    companion object {
        private const val KEY_TITLE_SETTING = "KEY_TITLE_SETTING"
        private const val KEY_ITEMS_SETTING = "KEY_ITEMS_SETTING"

        fun newInstance(title: String, items: List<SettingItemData>) =
            SettingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TITLE_SETTING, title)
                    putParcelableArrayList(KEY_ITEMS_SETTING, items as ArrayList)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogSettingBinding.inflate(inflater)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListener()
        initView()
    }

    private fun initView() {
        binding?.tvTitle?.text = arguments?.getString(KEY_TITLE_SETTING)
    }

    private fun initListener() {
        binding?.run {
            tvCancel.onClick {
                dismiss()
            }

            tvOk.onClick {
                viewModel.saveSettingInfo()
                (parentFragment as? SettingFragment)?.onDismissDialogListener?.invoke()
                dismiss()
            }
        }
    }

    private fun initAdapter() {
        val items = arguments?.getParcelableArrayList<SettingItemData>(KEY_ITEMS_SETTING)
        adapter = SettingDialogAdapter(items ?: emptyList()) { pos ->
            items?.mapIndexed { index, settingDataItem ->
                settingDataItem.isState = index == pos
            }
            adapter?.notifyDataSetChanged()
        }
        binding?.recyclerView?.adapter = adapter
    }

}
