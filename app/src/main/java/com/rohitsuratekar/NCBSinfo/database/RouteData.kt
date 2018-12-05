package com.rohitsuratekar.NCBSinfo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.rohitsuratekar.NCBSinfo.database.RouteData.Companion.ROUTE_TABLE

@Entity(tableName = ROUTE_TABLE)
class RouteData {

    @PrimaryKey(autoGenerate = true)
    @Expose
    var routeID: Int = 0
    @Expose
    var origin: String? = null
    @Expose
    var destination: String? = null
    @Expose
    var type: String? = null
    @Expose
    var favorite: String? = null // Values can be "yes" or "no"
    @Expose
    var createdOn: String? = null
    @Expose
    var modifiedOn: String? = null
    @Expose
    var author: String? = null
    @Expose
    var synced: String? = null

    companion object {
        const val ROUTE_TABLE = "routes"
    }
}