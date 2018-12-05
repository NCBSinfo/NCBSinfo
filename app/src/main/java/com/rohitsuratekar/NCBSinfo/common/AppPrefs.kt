package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import android.preference.PreferenceManager

private const val FAVORITE_ORIGIN = "favorite_origin"
private const val FAVORITE_DESTINATION = "favorite_destination"
private const val FAVORITE_TYPE = "favorite_type"
private const val EGG_ACTIVE = "egg_active"
private const val NOTIFICATIONS = "notifications"
private const val SETTINGS_DEFAULT_SET = "settings_default_set"
private const val LOCATION_SORT = "location_sort"
private const val DEVELOPER_ACTIVE = "developer_active"
private const val ADMIN_CODE = "admin_code"
private const val REMOTE_FETCH = "remote_fetch_time"
private const val UPDATE_62_63 = "update_62_63"

class AppPrefs(context: Context) {
    private val pref = PreferenceManager.getDefaultSharedPreferences(context)

    fun clearAll() {
        pref.edit().clear().apply()
    }

    fun favoriteOrigin(): String {
        return pref.getString(FAVORITE_ORIGIN, "ncbs") ?: kotlin.run { return "ncbs" }
    }

    fun favoriteOrigin(origin: String) {
        pref.edit().putString(FAVORITE_ORIGIN, origin).apply()
    }

    fun favoriteDestination(): String {
        return pref.getString(FAVORITE_DESTINATION, "iisc") ?: kotlin.run { return "iisc" }
    }

    fun favoriteDestination(destination: String) {
        pref.edit().putString(FAVORITE_DESTINATION, destination).apply()
    }

    fun favoriteType(): String {
        return pref.getString(FAVORITE_TYPE, "shuttle") ?: kotlin.run { return "shuttle" }
    }

    fun favoriteType(type: String) {
        pref.edit().putString(FAVORITE_TYPE, type).apply()
    }

    fun isNotificationAllowed(): Boolean {
        return pref.getBoolean(NOTIFICATIONS, true)
    }

    fun locationSort(): Int {
        return pref.getInt(LOCATION_SORT, 0)
    }

    fun locationSort(location: Int) {
        pref.edit().putInt(LOCATION_SORT, location).apply()
    }
}