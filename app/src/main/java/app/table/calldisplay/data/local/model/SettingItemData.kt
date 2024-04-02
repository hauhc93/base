package app.table.calldisplay.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SettingItemData(
    var typeOfDialog: Int? = null,
    var isState: Boolean = false,
    var ringtoneName: String? = "",
    var url: String? = "",
    var timer: Int? = null
) : Parcelable
