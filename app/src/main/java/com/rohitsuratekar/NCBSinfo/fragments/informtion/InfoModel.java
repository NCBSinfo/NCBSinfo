package com.rohitsuratekar.NCBSinfo.fragments.informtion;

public class InfoModel {
    private int title;
    private int details;
    private int image;
    private int action;

    public InfoModel() {
    }

    public InfoModel(int title, int details, int image, int action) {
        this.title = title;
        this.details = details;
        this.image = image;
        this.action = action;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDetails() {
        return details;
    }

    public void setDetails(int details) {
        this.details = details;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
