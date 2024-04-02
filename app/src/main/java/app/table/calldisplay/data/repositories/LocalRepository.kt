package app.table.calldisplay.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import app.table.calldisplay.data.local.datasource.LocalDataSource
import app.table.calldisplay.data.local.model.SettingInfoData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LocalRepository @Inject constructor(private val sharedPreferences: SharedPreferences) :
    LocalDataSource {

    companion object {
        internal const val KEY_SETTING_INFO = "KEY_SETTING_INFO"
        internal const val KEY_SHOP_ID = "KEY_SHOP_ID"
    }

    override fun getSettingInfo() =
        Json.decodeFromString<SettingInfoData>(
            sharedPreferences.getString(KEY_SETTING_INFO, "{}") ?: "{}"
        )

    override fun saveSettingInfo(settingInfo: SettingInfoData) {
        sharedPreferences.edit(true) {
            putString(
                KEY_SETTING_INFO,
                Json.encodeToString(SettingInfoData.serializer(), settingInfo)
            )
        }
    }

    override fun getShopId() = sharedPreferences.getString(KEY_SHOP_ID, "0") ?: "0"

    override fun saveShopId(id: String) {
        sharedPreferences.edit(true) { putString(KEY_SHOP_ID, id) }
    }
}
