package app.table.calldisplay.ui.setting

import app.table.calldisplay.base.BaseTest
import app.table.calldisplay.data.local.model.SettingInfoData
import app.table.calldisplay.data.local.model.SettingItemData
import app.table.calldisplay.data.repositories.LocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest : BaseTest() {
    private lateinit var viewModel: SettingViewModel

    @Mock
    lateinit var localRepository: LocalRepository
    lateinit var settingInfo: Field
    lateinit var timer: Field
    lateinit var ringtoneItems: Field
    lateinit var requestItems: Field


    override fun setup() {
        super.setup()
        viewModel = SettingViewModel(localRepository)
        settingInfo = viewModel.javaClass.getDeclaredField("settingInfo")
        settingInfo.isAccessible = true
        settingInfo.set(viewModel, SettingInfoData(request = 1, time = 2))
    }

    @Test
    fun testVNNotNull() {
        Assert.assertNotNull(viewModel)
    }

    @Test
    fun testSetRequestItems() {
        Assert.assertNotNull(viewModel.setRequestItems())
    }

    @Test
    fun testSetRingtoneItems() {
        ringtoneItems = viewModel.javaClass.getDeclaredField("ringtoneItems")
        ringtoneItems.isAccessible = true
        ringtoneItems.set(viewModel, listOf<SettingItemData>())
        val items = mapOf("123" to "123", "" to "")
        Assert.assertNotNull(viewModel.setRingtoneItems(items, "123"))
    }

    @Test
    fun testSaveSettingInfo() {
        ringtoneItems = viewModel.javaClass.getDeclaredField("ringtoneItems")
        ringtoneItems.isAccessible = true
        ringtoneItems.set(
            viewModel,
            listOf(SettingItemData(isState = true), SettingItemData(isState = false))
        )
        requestItems = viewModel.javaClass.getDeclaredField("requestItems")
        requestItems.isAccessible = true
        requestItems.set(
            viewModel,
            mutableListOf(SettingItemData(isState = true), SettingItemData(isState = false))
        )
        timer = viewModel.javaClass.getDeclaredField("timers")
        timer.isAccessible = true
        timer.set(
            viewModel, listOf(
                SettingItemData(timer = 0, isState = true),
                SettingItemData(timer = 2, isState = false)
            )
        )
        Assert.assertNotNull(viewModel.saveSettingInfo())
    }

    @Test
    fun testGetRingtoneItem() {
        Assert.assertNotNull(viewModel.getRingtoneItems())
    }

    @Test
    fun testGetTimer() {
        timer = viewModel.javaClass.getDeclaredField("timers")
        timer.isAccessible = true
        timer.set(
            viewModel, listOf(
                SettingItemData(timer = 0),
                SettingItemData(timer = 2),
                SettingItemData(timer = 5),
                SettingItemData(timer = 7)
            )
        )
        Assert.assertNotNull(viewModel.getTimer())
    }

    @Test
    fun testGetRequestItems() {
        Assert.assertNotNull(viewModel.getRequestItems())
    }

    @Test
    fun testSaveShopId() {
        Assert.assertNotNull(viewModel.saveShopId("123"))
    }

    @Test
    fun testGetShopId() {
        Assert.assertNotNull(viewModel.getShopId())
    }

    @Test
    fun testGetSettingInfo() {
        Assert.assertNotNull(viewModel.getSettingInfo())
    }
}
