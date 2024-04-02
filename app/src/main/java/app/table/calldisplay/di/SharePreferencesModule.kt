package app.table.calldisplay.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import app.table.calldisplay.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Canow
 * <p>
 * Created by FPT.
 * <p>
 * Define dependence of local
 */
@InstallIn(SingletonComponent::class)
@Module
class SharePreferencesModule {

    @Singleton
    @Provides
    fun provideSharePreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_preferences",
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideAssets(@ApplicationContext context: Context): AssetManager = context.assets
}
