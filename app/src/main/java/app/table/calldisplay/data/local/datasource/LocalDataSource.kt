package app.table.calldisplay.data.local.datasource

import app.table.calldisplay.data.local.model.SettingInfoData

interface LocalDataSource {

    fun getSettingInfo(): SettingInfoData

    fun saveSettingInfo(settingInfo: SettingInfoData)

    fun getShopId(): String

    fun saveShopId(id: String)
}
