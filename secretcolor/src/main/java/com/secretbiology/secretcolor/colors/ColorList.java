package com.secretbiology.secretcolor.colors;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 22-07-16.
 */



public class ColorList {

    public ColorModel getRed() {
        List<ColorLevels> list = new ArrayList<>();
        list.add(new ColorLevels(500, "#f44336"));
        list.add(new ColorLevels(50, "#ffebee"));
        list.add(new ColorLevels(100, "#ffcdd2"));
        return new ColorModel("RED", list);
    }

    public ColorModel getPurple() {
        List<ColorLevels> list = new ArrayList<>();
        list.add(new ColorLevels(500, "#9c27b0"));
        list.add(new ColorLevels(50, "#f3e5f5"));
        list.add(new ColorLevels(100, "#e1bee7"));
        return new ColorModel("PURPLE", list);
    }

}
