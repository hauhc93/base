package app.table.calldisplay.ui.home.model

import app.table.calldisplay.data.local.model.AppConstant

data class RequestOrder(val shopId: Int, val tableNo: Int, val services: List<Service>) {

    val countTotalService: Int
        get() = services.sumOf { it.count }

    val countDisplay: String
        get() = countTotalService.toString()

    val updateTimeSeconds: Long
        get() = services.maxOf { it.updateTimeSeconds }

    val firstCallTimeSeconds: Long
        get() = services.minOf { it.firstCallTimeSeconds }

    val tableDisplay: String
        get() = tableNo.toString()

    val firstServiceCall: Service
        get() = services.first { it.firstCallTimeSeconds == firstCallTimeSeconds }

    val hasOneService: Boolean
        get() = services.filter { it.id != AppConstant.SERVICE_LOW_BATTERY.service }.isNotEmpty()

    val hasTowService: Boolean
        get() = services.filter { it.id != AppConstant.SERVICE_LOW_BATTERY.service }.size > 1

    val hasThreeService: Boolean
        get() = services.filter { it.id != AppConstant.SERVICE_LOW_BATTERY.service }.size > 2

    val lowBattery: Boolean
        get() = services.any { it.id == AppConstant.SERVICE_LOW_BATTERY.service }

    fun getServiceOrNullForDisplay(index: Int): Service? = services.filter { it.id != AppConstant.SERVICE_LOW_BATTERY.service }.getOrNull(index)
}
