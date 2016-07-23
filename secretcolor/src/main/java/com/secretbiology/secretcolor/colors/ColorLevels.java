package com.secretbiology.secretcolor.colors;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 22-07-16.
 */
public class ColorLevels {
    int level;
    String hex;

    public ColorLevels(int level, String hex) {
        this.level = level;
        this.hex = hex;
    }

    public int getLevel() {
        return level;
    }

    public String getHex() {
        return hex;
    }

}
