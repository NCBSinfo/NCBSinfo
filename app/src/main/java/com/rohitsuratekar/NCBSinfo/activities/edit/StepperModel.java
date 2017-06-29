package com.rohitsuratekar.NCBSinfo.activities.edit;

/**
 * Created by Rohit Suratekar on 21-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class StepperModel {

    static final int STATE_INITIATED = -1;
    static final int STATE_SELECTED = 0;
    static final int STATE_COMPLETED = 1;
    static final int STATE_INCOMPLETE = 2;


    private int title;
    private int note;
    private int state;
    private boolean isSelected;

    StepperModel(int title, int note, int state, boolean isSelected) {
        this.title = title;
        this.note = note;
        this.state = state;
        this.isSelected = isSelected;
    }

    StepperModel(int title, int note, int state) {
        this.title = title;
        this.note = note;
        this.state = state;
        this.isSelected = false;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
