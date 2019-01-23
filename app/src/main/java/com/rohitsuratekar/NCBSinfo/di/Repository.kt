package com.rohitsuratekar.NCBSinfo.di

import android.app.Application
import com.rohitsuratekar.NCBSinfo.common.AppPrefs
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

class Repository @Inject constructor(
    private val data: DataRepository,
    private val application: Application,
    private val prefs: AppPrefs
) {

    fun prefs(): AppPrefs {
        return prefs
    }

    fun app(): Application {
        return application
    }

    fun data(): DataRepository {
        return data
    }


}