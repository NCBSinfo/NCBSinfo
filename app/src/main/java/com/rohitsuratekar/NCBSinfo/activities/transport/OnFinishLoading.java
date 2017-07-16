package com.rohitsuratekar.NCBSinfo.activities.transport;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

interface OnFinishLoading {
    void onFinish(TransportModel model);

    void onCheckingReverse(boolean isAvailable, int index);

    void onError(String message);
}
