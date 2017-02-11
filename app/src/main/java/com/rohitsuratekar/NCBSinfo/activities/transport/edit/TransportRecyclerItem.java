package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

public class TransportRecyclerItem {
    private String text;
    private boolean isSelected;

    public TransportRecyclerItem(String text, boolean isSelected) {
        this.text = text;
        this.isSelected = isSelected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getViewType() {
        if (isSelected) {
            return 1;
        } else {
            return 0;
        }
    }
}
