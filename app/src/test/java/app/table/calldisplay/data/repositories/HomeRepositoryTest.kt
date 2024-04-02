package app.table.calldisplay.data.repositories

import app.table.calldisplay.base.BaseTest
import app.table.calldisplay.data.local.model.SettingInfoData
import java.util.concurrent.TimeUnit
import jp.co.skylark.digital_gusto.call_display.DigitalGustoCallDisplay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest : BaseTest() {

    private lateinit var homeRepository: HomeRepository

    @Mock
    lateinit var digitalGustoCallDisplay: DigitalGustoCallDisplay

    @Mock
    lateinit var localRepository: LocalRepository

    override fun setup() {
        super.setup()
        digitalGustoCallDisplay = Mockito.mock(DigitalGustoCallDisplay::class.java)
        localRepository = Mockito.mock(LocalRepository::class.java)

        homeRepository = HomeRepository(digitalGustoCallDisplay, localRepository)
    }

    @Test
    fun testApplyOrderMacro1() {
        Mockito.`when`(localRepository.getShopId()).thenReturn("10")
        try {
            homeRepository.getDigitalGustoCallDisplay { _, _ -> }
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    @Test
    fun testGetDigitalGustoCallDisplayMacro1() {
        Mockito.`when`(localRepository.getShopId()).thenReturn("10")
        try {
            homeRepository.getDigitalGustoCallDisplay { _, _ -> }
        } catch (e: Exception) {
            Assert.fail(e.message)
        }
    }

    @Test
    fun testGetLimitRequestNewListMacro1() {
        val dataFake = SettingInfoData()
        Mockito.`when`(localRepository.getSettingInfo()).thenReturn(dataFake)
        Assert.assertEquals(homeRepository.getLimitRequestNewList(), dataFake.request)
    }

    @Test
    fun testGetRingToneURIMacro1() {
        val dataFake = SettingInfoData()
        Mockito.`when`(localRepository.getSettingInfo()).thenReturn(dataFake)
        Assert.assertEquals(homeRepository.getRingToneURI(), dataFake.url)
    }

    @Test
    fun testGetTimeMillisAutoDeleteMacro1() {
        val dataFake = SettingInfoData()
        Mockito.`when`(localRepository.getSettingInfo()).thenReturn(dataFake)
        Assert.assertEquals(
            homeRepository.getTimeMillisAutoDelete(),
            TimeUnit.MINUTES.toMillis(dataFake.time!!.toLong())
        )
    }

    @Test
    fun testGetTimeMillisAutoDeleteMacro2() {
        val dataFake = SettingInfoData()
        dataFake.time = null
        Mockito.`when`(localRepository.getSettingInfo()).thenReturn(dataFake)
        Assert.assertEquals(
            homeRepository.getTimeMillisAutoDelete(),
            TimeUnit.MINUTES.toMillis(dataFake.time?.toLong() ?: 0)
        )
    }
}
