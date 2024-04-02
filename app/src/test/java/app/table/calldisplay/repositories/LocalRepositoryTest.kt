package app.table.calldisplay.repositories

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import app.table.calldisplay.base.BaseTest
import app.table.calldisplay.data.local.model.SettingInfoData
import app.table.calldisplay.data.repositories.LocalRepository
import app.table.calldisplay.data.repositories.LocalRepository.Companion.KEY_SETTING_INFO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LocalRepositoryTest : BaseTest() {
    private lateinit var localRepository: LocalRepository

    @Mock
    lateinit var sharedPreferences: SharedPreferences
    var editor: Editor? = null

    override fun setup() {
        super.setup()
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        localRepository = LocalRepository(sharedPreferences)
    }

    @Test
    fun testLocalRepositoryNotNull() {
        Assert.assertNotNull(localRepository)
    }

    @Test
    fun testGetShopId() {
        `when`(localRepository.getShopId()).thenReturn("123")
        Assert.assertEquals(localRepository.getShopId(), "123")
    }

    @Test
    fun testSaveShopId() {
        Assert.assertNotNull(localRepository.saveShopId("123"))
    }

    @Test
    fun testGetSettingInfo() {
        `when`(sharedPreferences.getString(KEY_SETTING_INFO, "{}")).thenReturn("{}")
        Assert.assertNotNull(localRepository.getSettingInfo())
    }

    @Test
    fun testSaveSettingInfo() {
        Assert.assertNotNull(localRepository.saveSettingInfo(SettingInfoData()))
    }
}
