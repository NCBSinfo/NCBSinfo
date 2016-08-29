package com.rohitsuratekar.NCBSinfo.activities.settings.eggs.snake;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 24-07-16.
 * This is main class extended to View
 * You can implement this anywhere directly into xml.
 * <p/>
 * Logic is very simple,
 * (1) Change head based on direction
 * (2) Remove tail if fruit is not eaten
 * (3) Keep tail if fruit is eaten (this will increase it's length)
 * (4) Check collision with body before updating view
 */
public class StageViewer extends View implements GameConstants {

    int tileSize = 10;
    List<Integer> Snake;
    Bitmap snakeImage, fruitImage, headColor;
    private final Paint paint = new Paint();
    private int numberOfLines;
    private int numberOfColumns;
    Directions currentDirection;
    Status GameStatus = Status.RUNNING;
    int Fruit;
    int sleepTime = DEFAULT_SLEEP_TIME;
    int Score;

    public StageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameViewer);
        tileSize = a.getInt(R.styleable.GameViewer_tileSize, DEFAULT_TILE_SIZE);
        a.recycle();
    }

    public StageViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameViewer);
        tileSize = a.getInt(R.styleable.GameViewer_tileSize, DEFAULT_TILE_SIZE);
        a.recycle();
    }

    //Initialize Game
    public void initialize() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        snakeImage = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
        snakeImage.eraseColor(getContext().getResources().getColor(R.color.colorPrimary));
        fruitImage = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
        fruitImage.eraseColor(getContext().getResources().getColor(R.color.colorAccent));
        headColor = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
        headColor.eraseColor(Color.BLACK);

        //Check if any saved game available
        if (!pref.getBoolean("SavedGame", false)) {
            sleepTime = DEFAULT_SLEEP_TIME;
            Score = 0;
            GameStatus = Status.RUNNING;
            Snake = new ArrayList<>();
            Snake.add(getLocation(15, 10));
            Snake.add(getLocation(16, 10));
            Snake.add(getLocation(17, 10));
            currentDirection = Directions.DOWN;
            Fruit = getNewFruit();
        } else {
            String snakeString = pref.getString("Snake", "");
            String[] split = snakeString.split(",");
            List<Integer> snake = new ArrayList<>();
            for (String aSplit : split) {
                snake.add(Integer.parseInt(aSplit));
            }
            Snake = new ArrayList<>();
            for (int i : snake) {
                Snake.add(i);
            }
            setScore(pref.getInt("Score", 0));
            setSleepTime(pref.getInt("SleepTime", DEFAULT_SLEEP_TIME));
            setFruit(pref.getInt("Fruit", 2));
            setGameStatus(GameConstants.Status.PAUSE);
            currentDirection = getDirection(pref.getString("Direction", Directions.DOWN.name()));
            pref.edit().putBoolean("SavedGame", false).apply();
        }
    }

    //Needed for converting string to enum
    private Directions getDirection(String string) {
        for (Directions d : Directions.values()) {
            if (d.name().equals(string)) {
                return d;
            }
        }

        return Directions.DOWN;
    }

    public void setSnake(List<Integer> snake) {
        Snake = snake;
    }

    public void setFruit(int fruit) {
        Fruit = fruit;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void setGameStatus(Status gameStatus) {
        GameStatus = gameStatus;
    }

    //Main Drawing happens here
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Always update snake before draw
        Snake = updateSnake(Snake, currentDirection);

        //Draw snake
        for (int i = 0; i < Snake.size(); i++) {
            int[] location = indexToLocation(Snake.get(i));
            if (i == 0) {
                //Different color for head
                canvas.drawBitmap(headColor, location[0] * tileSize, location[1] * tileSize, paint);
            } else {
                canvas.drawBitmap(snakeImage, location[0] * tileSize, location[1] * tileSize, paint);
            }
        }
        //Draw Fruit
        canvas.drawBitmap(fruitImage, indexToLocation(Fruit)[0] * tileSize, indexToLocation(Fruit)[1] * tileSize, paint);

        //If game status is RUNNING, then invalidate canvas after sleep time
        if (GameStatus.equals(Status.RUNNING)) {
            if (Snake.size() > numberOfColumns) {
                sleepTime = DEFAULT_SLEEP_TIME + 30;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
            invalidate();  // Needed for animation
        } else {
            saveHighScore();
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("SavedGame", false).apply();
            if (GameStatus.equals(Status.LOST)) {
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_LONG).show();
            }
            if (GameStatus.equals(Status.WIN)) {
                Toast.makeText(getContext(), "Congrats !", Toast.LENGTH_LONG).show();
            }
        }


    }

    //This will determine size of stage
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.numberOfLines = (int) Math.floor(w / tileSize);
        this.numberOfColumns = (int) Math.floor(h / tileSize);
        initialize();
    }

    //Converting X - Y system into index based system
    public int getLocation(int x, int y) {
        int index = 0;
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfLines; j++) {
                if (i == x && j == y) {
                    return index;
                }
                index++;
            }
        }
        return 0;
    }

    //Get X-Y from Index
    public int[] indexToLocation(int index) {
        int currentIndex = 0;
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfLines; j++) {
                if (currentIndex == index) {
                    return new int[]{i, j};
                }
                currentIndex++;
            }
        }
        return new int[]{0, 0};
    }

    //Update snake
    public List<Integer> updateSnake(List<Integer> snake, Directions direction) {
        int oldHead = snake.get(0);
        int X = indexToLocation(oldHead)[0];
        int Y = indexToLocation(oldHead)[1];
        switch (direction) {
            case LEFT:
                X = X - 1;
                if (X < 1) {
                    X = numberOfColumns - 1;
                }
                break;
            case RIGHT:
                X = X + 1;
                if (X > numberOfColumns - 1) {
                    X = 1;
                }
                break;
            case UP:
                Y = Y - 1;
                if (Y < 1) {
                    Y = numberOfLines - 1;
                }
                break;
            case DOWN:
                Y = Y + 1;
                if (Y > numberOfLines - 1) {
                    Y = 1;
                }
                break;
        }

        int newIndex = getLocation(X, Y);

        //Check if collision with it's body
        for (int i : snake) {
            if (i == newIndex) {
                GameStatus = Status.LOST;
            }
        }

        //Add new head
        snake.add(0, newIndex);

        //If fruit is eaten, don't remove tail
        if (isInSnake(Fruit)) {
            Score++;
            Fruit = getNewFruit();
        } else {
            snake.remove(snake.size() - 1);
        }

        return snake;
    }

    public void goLeft() {
        if (currentDirection != Directions.RIGHT && GameStatus.equals(Status.RUNNING)) {
            currentDirection = Directions.LEFT;
            invalidate();
        }
    }

    public void goRight() {
        if (currentDirection != Directions.LEFT && GameStatus.equals(Status.RUNNING)) {
            currentDirection = Directions.RIGHT;
            invalidate();
        }
    }

    public void goDown() {
        if (currentDirection != Directions.UP && GameStatus.equals(Status.RUNNING)) {
            currentDirection = Directions.DOWN;
            invalidate();
        }
    }

    public void goUp() {
        if (currentDirection != Directions.DOWN && GameStatus.equals(Status.RUNNING)) {
            currentDirection = Directions.UP;
            invalidate();
        }
    }

    public void restartGame() {
        initialize();
        invalidate();
    }

    public void pauseGame() {
        if (GameStatus.equals(Status.RUNNING)) {
            GameStatus = Status.PAUSE;
        } else if (GameStatus.equals(Status.PAUSE)) {
            GameStatus = Status.RUNNING;
            invalidate();
        }
    }

    //Random number generator
    private int randInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    //Get next fruit
    private int getNewFruit() {
        for (int i = 0; i < numberOfColumns * numberOfLines; i++) {
            int randX = randInt(2, numberOfColumns - 2);
            int randY = randInt(2, numberOfLines - 2);
            int randIndex = getLocation(randX, randY);
            if (!isInSnake(randIndex)) {
                return randIndex;
            }
        }
        GameStatus = Status.WIN;
        Toast.makeText(getContext(), "You win", Toast.LENGTH_LONG).show();
        return 0;
    }

    //Check if given index is already part of snake
    private boolean isInSnake(int fruit) {
        for (int i : Snake) {
            if (i == fruit) {
                return true;
            }
        }
        return false;
    }

    public void saveHighScore() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (Score > pref.getInt("HighScore", 0)) {
            pref.edit().putInt("HighScore", Score).apply();
        }
    }

}