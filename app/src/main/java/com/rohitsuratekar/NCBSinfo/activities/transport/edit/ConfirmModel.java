package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

/**
 * Created by Rohit Suratekar on 20-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ConfirmModel {

    static final int ACTION_DAY_CONFLICT = 1;
    static final int ACTION_NO_CONFLICT = 2;
    static final int ACTION_ROUTE_CONFLICT = 3;
    static final int ACTION_DONE = 4;

    private int errorMessage;
    private int actionCode;
    private boolean isConfirmed;
    private boolean isForEdit;
    private ETDataHolder data;

    ConfirmModel() {
    }

    int getErrorMessage() {
        return errorMessage;
    }

    void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
    }

    int getActionCode() {
        return actionCode;
    }

    void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    boolean isConfirmed() {
        return isConfirmed;
    }

    void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    ETDataHolder getData() {
        return data;
    }

    void setData(ETDataHolder data) {
        this.data = data;
    }

    boolean isForEdit() {
        return isForEdit;
    }

    void setForEdit(boolean forEdit) {
        isForEdit = forEdit;
    }
}
