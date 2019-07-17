package com.rohitsuratekar.NCBSinfo.di

import android.content.Context
import com.rohitsuratekar.NCBSinfo.common.AppPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefModule(context: Context) {
    private val prefs = AppPrefs(context)

    @Singleton
    @Provides
    fun providePrefs(): AppPrefs {
        return prefs
    }
}