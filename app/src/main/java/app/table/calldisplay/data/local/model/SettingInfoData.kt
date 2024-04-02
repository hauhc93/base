package app.table.calldisplay.data.local.model

import kotlinx.serialization.Serializable

@Serializable
class SettingInfoData(
    var ringtoneName: String? = "",
    var url: String? = "",
    var request: Int = 1,
    var time: Int? = 2
)
