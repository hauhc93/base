package jp.co.skylark.digital_gusto.call_display.data

data class Service(
    val id: Int,
    val count: Int,
    val updateTimeSeconds: Long,
    val firstCallTimeSeconds: Long
)