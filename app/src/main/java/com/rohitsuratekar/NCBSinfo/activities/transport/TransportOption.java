package com.rohitsuratekar.NCBSinfo.activities.transport;

/**
 * Created by Rohit Suratekar on 15-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportOption {
    private int action;
    private int icon;
    private int text;

    public TransportOption(int action, int icon, int text) {
        this.action = action;
        this.icon = icon;
        this.text = text;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }
}
