package com.rohitsuratekar.NCBSinfo;

import java.util.Random;

public class Helper {

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
    }
}
