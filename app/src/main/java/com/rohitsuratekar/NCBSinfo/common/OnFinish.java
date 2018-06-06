package com.rohitsuratekar.NCBSinfo.common;

import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 06-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public interface OnFinish {
    void finished();

    void allRoutes(List<RouteData> routeDataList);
}
