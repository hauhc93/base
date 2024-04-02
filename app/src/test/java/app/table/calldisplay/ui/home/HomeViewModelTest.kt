package app.table.calldisplay.ui.home

import android.net.Uri
import app.table.calldisplay.base.BaseTest
import app.table.calldisplay.data.repositories.HomeRepository
import app.table.calldisplay.data.repositories.LocalRepository
import app.table.calldisplay.ui.home.model.RequestOrder
import app.table.calldisplay.ui.home.model.Service
import java.lang.reflect.Method
import java.util.Timer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : BaseTest() {
    private lateinit var viewModel: HomeViewModel

    @Mock
    lateinit var localRepository: LocalRepository

    @Mock
    lateinit var homeRepository: HomeRepository

    private val mtRequestOrders: MutableStateFlow<List<RequestOrder>> =
        MutableStateFlow(emptyList())

    override fun setup() {
        super.setup()
        localRepository = Mockito.mock(LocalRepository::class.java)
        homeRepository = Mockito.mock(HomeRepository::class.java)

        viewModel = Mockito.spy(HomeViewModel(homeRepository))
    }

    @Test
    fun testVMNotNull() {
        Assert.assertNotNull(viewModel)
    }

    @Test
    fun testIsHasBeenNewRequestTrue() {
        Assert.assertEquals(viewModel.isHasBeenNewRequest(10), true)
    }

    @Test
    fun testIsHasBeenNewRequestFalse() {
        viewModel.updateOldUpdateTimeSeconds(10)
        Assert.assertEquals(viewModel.isHasBeenNewRequest(9), false)
    }

    @Test
    fun testUpdateOldUpdateTimeSecondsNotNull() {
        Assert.assertNotNull(viewModel.updateOldUpdateTimeSeconds(10))
    }

    @Test
    fun testApplyOderRequestApiNotNull() {
        val requestOrder = Mockito.mock(RequestOrder::class.java)
        Assert.assertNotNull(viewModel.applyOderRequestApi(requestOrder))
    }

    @Test
    fun testIsRequestOrderEmpty() {
        Assert.assertEquals(viewModel.isRequestOrderEmpty.value, true)
    }

    @Test
    fun testIsRequestOrderOldEmpty() {
        Assert.assertEquals(viewModel.isRequestOrderOldEmpty.value, true)
    }

    @Test
    fun testRequestOrders() {
        Assert.assertEquals(viewModel.requestOrders.value, mtRequestOrders.value)
    }

    @Test
    fun testRequestOrdersOld() {
        Assert.assertEquals(viewModel.requestOrdersOld.value, mtRequestOrders.value)
    }

    @Test
    fun testNewUpdateTimeObserver() {
        Assert.assertEquals(viewModel.newUpdateTimeObserver.value, 0L)
    }

    @Test
    fun testHandleArrangeListTableOneSize() {
        handleArrangeListTable(listTableOneSize)
    }

    @Test
    fun testHandleArrangeListTableManySize() {
        handleArrangeListTable(listTableManySize)
    }

    @Test
    fun testHandleArrangeListTableManySizeMacro2() {
        viewModel.isTablet = true
        handleArrangeListTable(listTableManySize)
    }

    private fun handleArrangeListTable(list: List<RequestOrder>) {
        val limitRequestOrdersDisplay =
            viewModel.javaClass.getDeclaredField("limitRequestOrdersDisplay")
        limitRequestOrdersDisplay.isAccessible = true
        // set giá trị cho limitRequestOrdersDisplay
        limitRequestOrdersDisplay.setInt(viewModel, 3)
        val method = getMethodPrivate("handleArrangeListTable", List::class.java)
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = list
        method.invoke(viewModel, *parameters)
        Assert.assertNotNull(method)
    }

    @Test
    fun testNotifyNewUpdateTimeSecondsEmptyListServices() {
        val requestOrder = RequestOrder(shopId = 100, tableNo = 100, services = emptyList())
        val method = getMethodPrivate("notifyNewUpdateTimeSeconds", List::class.java)
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = arrayListOf(requestOrder)
        method.invoke(viewModel, *parameters)
        Assert.assertNotNull(method)
    }

    @Test
    fun testNotifyNewUpdateTimeSecondsListService() {
        val requestOrder =
            RequestOrder(shopId = 100, tableNo = 100, services = listService)
        val method =
            viewModel.javaClass.getDeclaredMethod("notifyNewUpdateTimeSeconds", List::class.java)
        method.isAccessible = true
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = listOf(requestOrder)
        method.invoke(viewModel, *parameters)
        Assert.assertNotNull(method)
    }

    @Test
    fun testHandleGetListRequestSuccessNotNull() {
        val requestOrder = Mockito.mock(RequestOrder::class.java)
        //get fun private of class
        val method = getMethodPrivate("handleGetListRequestSuccess", List::class.java)
        val parameters = arrayOfNulls<Any>(1)
        parameters[0] = arrayListOf(requestOrder)
        method.invoke(viewModel, *parameters)
        Assert.assertNotNull(method)
    }

    @Test
    fun testOnCleared() {
//        val method = getMethodPrivate("onCleared")
//        method.invoke(viewModel)
//        Assert.assertNotNull(method)
    }

    @Test
    fun testIsTabletMacro1() {
        Assert.assertFalse(viewModel.isTablet)
    }

    @Test
    fun testResumeDateStartScanToDeleteMacro1() {
        Mockito.`when`(homeRepository.getTimeMillisAutoDelete()).thenReturn(10000)

        try {
            val resumeDateStartScanToDelete = getMethodPrivate("resumeDateStartScanToDelete")
            resumeDateStartScanToDelete.invoke(viewModel)
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    @Test
    fun testGetRingToneUriNotNull() {
        Mockito.`when`(homeRepository.getRingToneURI()).thenReturn("google.com/abcd1234")
        Assert.assertEquals(viewModel.getRingToneUri(), Uri.parse(homeRepository.getRingToneURI()))
    }

    // region Test ConfigTimerScanToAutoDelete
//    @Test
//    fun testConfigTimerScanToAutoDeleteMacro1() {
//        viewModel.javaClass.getDeclaredField("timerAutoDelete").let {
//            it.isAccessible = true
//            it.set(viewModel, Timer())
//        }
//        Mockito.`when`(homeRepository.getTimeMillisAutoDelete()).thenReturn(1)
//
//        try {
//            viewModel.configTimerScanToAutoDelete()
//        } catch (e: Exception) {
//            Assert.fail(e.message)
//        }
//    }

    @Test
    fun testConfigTimerScanToAutoDeleteMacro2() {
        Mockito.`when`(homeRepository.getTimeMillisAutoDelete()).thenReturn(-1)

        try {
            viewModel.configTimerScanToAutoDelete()
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }
    // endregion Test ConfigTimerScanToAutoDelete

    // region Test CancelScanDelete
    @Test
    fun testCancelScanDeleteMacro() {
        viewModel.javaClass.getDeclaredField("timerAutoDelete").let {
            it.isAccessible = true
            it.set(viewModel, Timer())
        }

        try {
            viewModel.cancelScanDelete()
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    @Test
    fun testCancelScanDeleteMacro2() {
        try {
            viewModel.cancelScanDelete()
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }
    // endregion Test CancelScanDelete

    private fun getMethodPrivate(
        methodName: String,
        vararg parameterTypes: Class<*>?,
    ): Method {
        val method =
            viewModel.javaClass.getDeclaredMethod(methodName, *parameterTypes)
        method.isAccessible = true
        return method
    }

    private val listService = arrayListOf(
        Service(
            id = 100, count = 2,
            updateTimeSeconds = 1000L, firstCallTimeSeconds = 1000L
        ),
        Service(
            id = 200, count = 2,
            updateTimeSeconds = 2000L, firstCallTimeSeconds = 2000L
        ),
        Service(
            id = 300, count = 2,
            updateTimeSeconds = 1500L, firstCallTimeSeconds = 2000L
        )
    )

    private val listTableManySize = arrayListOf(
        RequestOrder(shopId = 100, tableNo = 100, services = emptyList()),
        RequestOrder(shopId = 100, tableNo = 100, services = emptyList()),
        RequestOrder(shopId = 100, tableNo = 100, services = emptyList()),
        RequestOrder(shopId = 100, tableNo = 100, services = emptyList())
    )

    private val listTableOneSize = arrayListOf(
        RequestOrder(shopId = 100, tableNo = 100, services = emptyList())
    )
}
