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
    private int favoriteRoute = -1;

    public HomeObject(SparseArray<RouteData> routeData, SparseArray<List<TripData>> tripData, int fav) {
        this.routeData = routeData;
        this.tripData = tripData;
        if (routeData.size() > 0) {
            if (fav != -1) {
                setRoute(fav);
            } else {
                setRoute(routeData.get(routeData.keyAt(0)).getRouteID());
            }
        }
        this.favoriteRoute = fav;
    }

    void setRoute(int routeNo) {
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
        if (relatedRoutes.size() == 0) {
            for (int i = 0; i < routeData.size(); i++) {
                if (routeData.get(routeData.keyAt(i)).getOrigin().equals(this.destination)) {
                    if (routeData.get(routeData.keyAt(i)).getRouteID() != routeNo) {
                        relatedRoutes.add(routeData.get(routeData.keyAt(i)).getRouteID());
                    }
                }
            }
        }
        Collections.shuffle(relatedRoutes);

    }

    String getOrigin() {
        return origin;
    }

    String getDestination() {
        return destination;
    }

    String getType() {
        return type;
    }

    List<Integer> getRelatedRoutes() {
        return relatedRoutes;
    }

    public SparseArray<RouteData> getRouteData() {
        return routeData;
    }

    SparseArray<List<TripData>> getTripData() {
        return tripData;
    }

    NextTrip getNextTrip() {
        return nextTrip;
    }

    int getRouteNo() {
        return routeNo;
    }

    int getFavoriteRoute() {
        return favoriteRoute;
    }

    void setFavoriteRoute(int favoriteRoute) {
        this.favoriteRoute = favoriteRoute;
    }

    void goNext() {
        int index = this.routeData.indexOfKey(this.routeNo);
        index++;
        if (index == this.routeData.size()) {
            index = 0;
        }
        setRoute(this.routeData.get(this.routeData.keyAt(index)).getRouteID());
    }

    void goBack() {
        int index = this.routeData.indexOfKey(this.routeNo);
        index--;
        if (index == -1) {
            index = this.routeData.size() - 1;
        }
        setRoute(this.routeData.get(this.routeData.keyAt(index)).getRouteID());
    }
}
