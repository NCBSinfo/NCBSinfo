package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

/**
 * Created by Dexter on 01-01-2017.
 */

public class StepModel {
    private int icon;
    private int color;
    private boolean isDone;
    private boolean isSkipped;

    public StepModel(int icon, int color, boolean isDone) {
        this.icon = icon;
        this.color = color;
        this.isDone = isDone;
        this.isSkipped = false;
    }

    public StepModel(int icon, int color, boolean isDone, boolean isSkipped) {
        this.icon = icon;
        this.color = color;
        this.isDone = isDone;
        this.isSkipped = isSkipped;
    }

    public StepModel(int icon, int color) {
        this.icon = icon;
        this.color = color;
        this.isDone = false;
        this.isSkipped = false;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void setSkipped(boolean skipped) {
        isSkipped = skipped;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
