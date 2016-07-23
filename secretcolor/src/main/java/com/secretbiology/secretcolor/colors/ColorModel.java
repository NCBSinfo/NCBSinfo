package com.secretbiology.secretcolor.colors;

import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 22-07-16.
 */
public class ColorModel {
    String name;
    List<ColorLevels> levels;

    public ColorModel(String name, List<ColorLevels> levels) {
        this.name = name;
        this.levels = levels;
    }

    public String getName() {
        return name;
    }


    public List<ColorLevels> getLevels() {
        return levels;
    }

}
