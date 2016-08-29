package com.rohitsuratekar.NCBSinfo.activities.settings.eggs.snake;

/**
 * Created by Rohit Suratekar on 24-07-16.
 * Just set of static constants.
 * Used "enums" for fast performance and restricted states
 */
public interface GameConstants {

    enum Directions {UP, DOWN, LEFT, RIGHT}

    enum Status {RUNNING, PAUSE, LOST, WIN}


    int DEFAULT_SLEEP_TIME = 120;
    int DEFAULT_TILE_SIZE = 30; //If not set in xml
}
