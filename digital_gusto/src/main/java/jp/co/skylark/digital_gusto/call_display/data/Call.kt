package jp.co.skylark.digital_gusto.call_display.data

data class Call(val shopId: Int, val tableNo: Int, val services: List<Service>) {

    val count
        get() = services.sumOf { a -> a.count }

    val updateTimeSeconds
        get() = services.maxOf { a -> a.updateTimeSeconds }

    val firstCallTimeSeconds
        get() = services.minOf { a -> a.firstCallTimeSeconds }
}
