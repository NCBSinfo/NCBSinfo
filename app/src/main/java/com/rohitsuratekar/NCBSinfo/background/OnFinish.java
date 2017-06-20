package com.rohitsuratekar.NCBSinfo.background;

import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public interface OnFinish {
    void finished();

    void allRoutes(List<RouteData> routeDataList);
}
