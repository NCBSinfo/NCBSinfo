package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.widget.ImageView;

public class TransportEditState {

    private int location;
    private boolean opened;
    private boolean skipped;
    private boolean completed;
    private ImageView imageView;

    public TransportEditState(int location, ImageView imageView) {
        this.location = location;
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }


    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void changingToLocation(int loc) {
        if (opened) {
            if (loc != location && !completed) {
                skipped = true;
            }
        }
    }
}
