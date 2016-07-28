package com.rohitsuratekar.NCBSinfo.activities.settings.snake;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;

public class SnakeActivity extends AppCompatActivity {

    boolean isGamePaused;
    Button pause;
    StageViewer stage;
    TextView scoreText;
    SharedPreferences pref;
    BaseParameters baseParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snake);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        baseParameters = new BaseParameters(getBaseContext());
        isGamePaused = true;
        //Setup all UI components

        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        setTitle("Snake");

        stage = (StageViewer) findViewById(R.id.stageView);
        Button down = (Button) findViewById(R.id.moveDown);
        Button left = (Button) findViewById(R.id.moveLeft);
        Button right = (Button) findViewById(R.id.moveRight);
        Button up = (Button) findViewById(R.id.moveUp);
        Button restart = (Button) findViewById(R.id.restartButton);
        scoreText = (TextView) findViewById(R.id.scoreText);
        TextView HighScoreText = (TextView) findViewById(R.id.highScoreText);

        pause = (Button) findViewById(R.id.pauseButton);

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage.goDown();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage.goLeft();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage.goRight();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage.goUp();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage.restartGame();
            }
        });


        HighScoreText.setText(String.valueOf(pref.getInt("HighScore", 0)));

        if (pref.getBoolean("SavedGame", false)) {
            isGamePaused = false;
            pause.setText("START");
        }

        //Pause button text will change
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGamePaused) {
                    pause.setText("START");
                    isGamePaused = false;
                } else {
                    pause.setText("PAUSE");
                    isGamePaused = true;
                }
                stage.pauseGame();
            }
        });

        runnable.run();

    }

    //Pause game
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        if (isGamePaused) {
            pause.setText("START");
            isGamePaused = false;
        } else {
            pause.setText("PAUSE");
            isGamePaused = true;
        }
        stage.pauseGame();


    }


    @Override
    protected void onRestart() {
        runnable.run();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        saveGame();

        super.onDestroy();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {
            scoreText.setText(String.valueOf(stage.Score));
            handler.postDelayed(this, stage.sleepTime);
        }
    };

    //Save game
    public void saveGame() {
        if (stage.GameStatus.equals(GameConstants.Status.RUNNING) || stage.GameStatus.equals(GameConstants.Status.PAUSE)) {
            pref.edit().putBoolean("SavedGame", true).apply();
            pref.edit().putInt("Score", stage.Score).apply();
            pref.edit().putInt("SleepTime", stage.sleepTime).apply();
            pref.edit().putInt("Fruit", stage.Fruit).apply();
            pref.edit().putString("Direction", stage.currentDirection.name()).apply();

            String snakeString = "";
            for (int i = 0; i < stage.Snake.size(); i++) {
                snakeString = snakeString + String.valueOf(stage.Snake.get(i));
                if (i != stage.Snake.size() - 1) {
                    snakeString = snakeString + ",";
                }
            }
            pref.edit().putString("Snake", snakeString).apply();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
    }
}
