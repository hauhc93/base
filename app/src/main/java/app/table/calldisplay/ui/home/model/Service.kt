package app.table.calldisplay.ui.home.model

data class Service(
    val id: Int,
    val count: Int,
    val updateTimeSeconds: Long,
    val firstCallTimeSeconds: Long
) {
    companion object {
        fun fromServiceDigitalGusto(serviceDigitalGusto: jp.co.skylark.digital_gusto.call_display.data.Service):
                Service = Service(
            serviceDigitalGusto.id,
            serviceDigitalGusto.count,
            serviceDigitalGusto.updateTimeSeconds,
            serviceDigitalGusto.firstCallTimeSeconds
        )
    }
}
