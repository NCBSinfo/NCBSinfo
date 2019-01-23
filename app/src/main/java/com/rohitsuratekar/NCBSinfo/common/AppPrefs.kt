package com.rohitsuratekar.NCBSinfo.common

import android.content.Context
import android.preference.PreferenceManager

private const val FAVORITE_ORIGIN = "favorite_origin"
private const val FAVORITE_DESTINATION = "favorite_destination"
private const val FAVORITE_TYPE = "favorite_type"
private const val NOTIFICATIONS = "notifications"
private const val LOCATION_SORT = "location_sort"
private const val UPDATE_VERSION = "update_version"
private const val CRASH_REPORTING = "crash_reporting"
private const val CRASH_REPORTING_REQUEST = "crash_reporting_request"

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

    fun updateVersion(): Int {
        return pref.getInt(UPDATE_VERSION, -1)
    }

    fun updateVersion(version: Int) {
        pref.edit().putInt(UPDATE_VERSION, version).apply()
    }

    fun crashReportingEnabled(): Boolean {
        return pref.getBoolean(CRASH_REPORTING, false)
    }

    fun crashReportingEnabled(value: Boolean) {
        pref.edit().putBoolean(CRASH_REPORTING, value).apply()
    }

    fun showCrashReportingRequest(): Boolean {
        return pref.getBoolean(CRASH_REPORTING_REQUEST, true)
    }

    fun showCrashReportingRequest(value: Boolean) {
        pref.edit().putBoolean(CRASH_REPORTING_REQUEST, value).apply()
    }
}