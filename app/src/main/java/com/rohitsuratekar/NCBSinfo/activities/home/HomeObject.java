package com.rohitsuratekar.NCBSinfo.activities.home;

import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.activities.transport.NextTrip;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rohit Suratekar on 13-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class HomeObject {
    private int routeNo;
    private String origin;
    private String destination;
    private String type;
    private NextTrip nextTrip;
    private SparseArray<RouteData> routeData;
    private SparseArray<List<TripData>> tripData;
    private List<Integer> relatedRoutes = new ArrayList<>();

    public HomeObject(SparseArray<RouteData> routeData, SparseArray<List<TripData>> tripData) {
        this.routeData = routeData;
        this.tripData = tripData;
        if (routeData.size() > 0) {
            setRoute(routeData.get(routeData.keyAt(0)).getRouteID());
        }
    }

    public void setRoute(int routeNo) {
        this.routeNo = routeNo;
        this.nextTrip = new NextTrip(tripData.get(routeNo));
        this.origin = routeData.get(routeNo).getOrigin();
        this.destination = routeData.get(routeNo).getDestination();
        this.type = routeData.get(routeNo).getType();
        relatedRoutes.clear();
        for (int i = 0; i < routeData.size(); i++) {
            if (routeData.get(routeData.keyAt(i)).getOrigin().equals(this.origin)) {
                if (routeData.get(routeData.keyAt(i)).getRouteID() != routeNo) {
                    relatedRoutes.add(routeData.get(routeData.keyAt(i)).getRouteID());
                }
            }
        }
        Collections.shuffle(relatedRoutes);

    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getType() {
        return type;
    }

    public List<Integer> getRelatedRoutes() {
        return relatedRoutes;
    }

    public SparseArray<RouteData> getRouteData() {
        return routeData;
    }

    public SparseArray<List<TripData>> getTripData() {
        return tripData;
    }

    public NextTrip getNextTrip() {
        return nextTrip;
    }

    public int getRouteNo() {
        return routeNo;
    }
}
