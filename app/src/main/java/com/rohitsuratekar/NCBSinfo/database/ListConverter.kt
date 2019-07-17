package com.rohitsuratekar.NCBSinfo.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ListConverter {

    @TypeConverter
    fun toStringList(listString: String): List<String>? {
        val type = object : TypeToken<ArrayList<String>>() {

        }.type
        return Gson().fromJson<List<String>>(listString, type)
    }

    @TypeConverter
    fun toString(listStirng: List<String>): String {
        return Gson().toJson(listStirng)
    }
}