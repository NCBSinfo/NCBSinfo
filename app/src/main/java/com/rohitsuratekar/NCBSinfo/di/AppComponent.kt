package com.rohitsuratekar.NCBSinfo.di

import com.rohitsuratekar.NCBSinfo.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [],
    modules = [AppModule::class, RoomModule::class, PrefModule::class]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun repository(): Repository
    fun dataRepository(): DataRepository
}