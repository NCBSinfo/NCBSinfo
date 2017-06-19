package com.rohitsuratekar.NCBSinfo.background;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.RouteInfo;

import java.util.List;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public interface OnDataLoad {

    void loaded(List<RouteInfo> infoList);
}
