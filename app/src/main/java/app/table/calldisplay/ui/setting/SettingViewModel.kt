package app.table.calldisplay.ui.setting

import app.table.calldisplay.base.ui.BaseViewModel
import app.table.calldisplay.data.local.model.DialogType
import app.table.calldisplay.data.local.model.SettingItemData
import app.table.calldisplay.data.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val localRepository: LocalRepository,
) : BaseViewModel() {

    companion object {
        private const val REQUEST_SIZE = 5
    }

    private val requestItems: MutableList<SettingItemData> = mutableListOf()
    private var ringtoneItems: List<SettingItemData> = listOf()
    private var settingInfo = localRepository.getSettingInfo()
    private var shopId: MutableStateFlow<String> = MutableStateFlow(localRepository.getShopId())
    private val timers = listOf(
        SettingItemData(timer = 0),
        SettingItemData(timer = 2),
        SettingItemData(timer = 5),
        SettingItemData(timer = 7)
    )


    fun setRequestItems() {
        requestItems.clear()
        for (i in 0 until REQUEST_SIZE) {
            if (i == (settingInfo.request - 1)) {
                requestItems.add(
                    SettingItemData(
                        typeOfDialog = DialogType.REQUEST.ordinal,
                        isState = true,
                        ringtoneName = (i + 1).toString()
                    )
                )
            } else {
                requestItems.add(
                    SettingItemData(
                        typeOfDialog = DialogType.REQUEST.ordinal,
                        isState = false,
                        ringtoneName = (i + 1).toString()
                    )
                )
            }
        }
    }

    fun setRingtoneItems(items: Map<String, String>, ringtone: String) {
        ringtoneItems = items.entries.map {
            if (it.key == ringtone) {
                SettingItemData(
                    typeOfDialog = DialogType.SOUND.ordinal,
                    ringtoneName = it.key,
                    url = it.value,
                    isState = true
                )
            } else {
                SettingItemData(
                    typeOfDialog = DialogType.SOUND.ordinal,
                    ringtoneName = it.key,
                    url = it.value,
                    isState = false
                )
            }
        }
    }

    fun getRingtoneItems() = ringtoneItems

    fun getRequestItems() = requestItems.toList()

    fun getTimer() = timers.apply {
        this.map {
            if (it.timer == settingInfo.time) {
                it.isState = true
            }
        }
    }.toList()

    fun saveSettingInfo() {
        requestItems.mapIndexed { index, settingItemData ->
            if (settingItemData.isState) {
                settingInfo.request = index + 1
            }
        }
        ringtoneItems.map { settingItemData ->
            if (settingItemData.isState) {
                settingInfo.url = settingItemData.url
                settingInfo.ringtoneName = settingItemData.ringtoneName
            }
        }
        timers.map {
            if (it.isState) {
                settingInfo.time = it.timer
            }
        }
        localRepository.saveSettingInfo(settingInfo)
        settingInfo = localRepository.getSettingInfo()
    }

    fun getSettingInfo() = settingInfo

    fun saveShopId(id: String) {
        shopId.value = id
        localRepository.saveShopId(id)
    }

    fun getShopId() = shopId
}
