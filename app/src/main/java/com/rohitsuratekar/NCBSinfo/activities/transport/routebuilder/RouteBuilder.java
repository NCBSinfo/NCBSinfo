package com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;

/**
 * This will provide Transport Model
 */
public class RouteBuilder {

    private RouteModel model;
    private Context context;

    public RouteBuilder(Routes route, Context context) {
        this.context = context;
        this.model = new RouteInformation(route).get();
    }

    public TransportRoute build() {
        if (model.getType().equals(Routes.type.SHUTTLE)) {
            return new ShuttleBuilder(model, context);
        } else if (model.getType().equals(Routes.type.BUGGY)) {
            return new BuggyBuilder(model, context);
        }
        return null;
    }


}
