package app.table.calldisplay.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.skylark.digital_gusto.DigitalGustoApp
import jp.co.skylark.digital_gusto.call_display.DigitalGustoCallDisplay

@Module
@InstallIn(SingletonComponent::class)
object DigitalGustoModule {

    @Provides
    @Singleton
    fun provideDigitalGustoApp(@ApplicationContext context: Context): DigitalGustoApp =
        DigitalGustoApp.initializeApp(context)

    @Provides
    @Singleton
    fun provideDigitalGustoCallDisplay(digitalGustoApp: DigitalGustoApp): DigitalGustoCallDisplay =
        DigitalGustoCallDisplay.getInstance(digitalGustoApp)
}
