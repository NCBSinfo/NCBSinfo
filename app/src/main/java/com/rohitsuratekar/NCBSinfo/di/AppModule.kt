package com.rohitsuratekar.NCBSinfo.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private var application: Application) {
    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

}