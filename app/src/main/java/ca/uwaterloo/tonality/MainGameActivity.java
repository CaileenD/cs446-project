package ca.uwaterloo.tonality;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MainGameActivity extends AppCompatActivity {

    private String TAG = "MainGameActivity";

    private static final long startTimeInSecs = 10;
    private TextView timerDisplay;
    private long secondsLeft = startTimeInSecs;
    private AudioSoundPlayer soundPlayer = new AudioSoundPlayer(this);
    public static final int numButtons = 7; // number of note buttons at bottom of screen
    public noteCountDownTimer countDown; // Counts how long user has to select note
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int rightGuesses = 0;
    private int wrongGuesses = 0;
    final int min = 1;
    private Random randGenerator = new Random();
    private int currentRandomNote = 0;
    View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        countDown = new noteCountDownTimer(10000, 1000); // 10 second timer
        playRandomNote();
        countDown.start();
        timerDisplay = findViewById(R.id.timer);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNote(Integer.parseInt(v.getTag().toString()));
            }
        };

        // Set the button listeners
        final Button button1 = findViewById(R.id.button1);
        final Button button2 = findViewById(R.id.button2);
        final Button button3 = findViewById(R.id.button3);
        final Button button4 = findViewById(R.id.button4);
        final Button button5 = findViewById(R.id.button5);
        final Button button6 = findViewById(R.id.button6);
        final Button button7 = findViewById(R.id.button7);

        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);

    }

    private void updateCountDownText(){
        String timeLeftFormatted = String.format(Locale.getDefault(), "0:%02d", secondsLeft);
        timerDisplay.setText(timeLeftFormatted);
    }

    public void playNote(int note){
        soundPlayer.playNote(note);

        checkUserNote(note);    // see if user selected correct note
        resetTimer();

        if(gameOver){
            //Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show();
            //TODO: Create new activity for game over
        }

        if(gameWon){
             //Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
            //TODO: Create new activity for winning the game
        }
    }

    protected void playRandomNote(){
        currentRandomNote = randGenerator.nextInt((numButtons - min) + 1) + min;
        soundPlayer.playNote(currentRandomNote);
    }

    private void checkUserNote(int playedNote){
        if(playedNote == currentRandomNote){
            rightGuesses++;
            Toast.makeText(this, "Right guess: " + rightGuesses, Toast.LENGTH_LONG).show();
            if(rightGuesses >= 5){
                gameWon = true;
            }
        } else {
            wrongGuesses++;
            if(wrongGuesses >= 5 ){
                gameOver = true;
            }
            Toast.makeText(this, "Wrong guess: " + wrongGuesses, Toast.LENGTH_LONG).show();
        }
    }

    private void resetTimer(){
        countDown.cancel();
        try{
            Thread.sleep(2000); // Have a pause between playing user note and next random note
        } catch(Exception e){
            Log.e(TAG, "Error! " + e.getMessage());
        }
        playRandomNote();
        countDown = new noteCountDownTimer(10000, 1000);
        countDown.start();
    }

    public class noteCountDownTimer extends CountDownTimer {
        public noteCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            wrongGuesses++;
            if(wrongGuesses < 5){
                resetTimer();
            } else {
                gameOver = true;
            }
        }
        @Override
        public void onTick(long timeLeftInMilliSecs) {
            secondsLeft = timeLeftInMilliSecs/1000;
            updateCountDownText();
        }
    }
}
