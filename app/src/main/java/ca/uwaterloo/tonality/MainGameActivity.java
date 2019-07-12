package ca.uwaterloo.tonality;

import android.app.Dialog;
import android.content.Intent;
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
import java.util.Vector;

public class MainGameActivity extends AppCompatActivity {

    private String TAG = "MainGameActivity";

    private static final long startTimeInSecs = 10;
    private TextView timerDisplay;
    private long secondsLeft = startTimeInSecs;
    private AudioSoundPlayer soundPlayer;
    public static final int numButtons = 7; // number of note buttons at bottom of screen
    public noteCountDownTimer countDown; // Counts how long user has to select note
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int rightGuesses = 0;
    private int wrongGuesses = 0;
    final int min = 1;
    private Random randGenerator = new Random();
    private int currentRandomNote = 0;
    Dialog popUpDialog;

    View.OnClickListener listener;
    Vector<Button> noteButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        Intent intent = this.getIntent();
        String selectedScale = intent.getStringExtra("selectedScale");
        String [] scaleNotes = intent.getStringArrayExtra("scaleNotes");
        soundPlayer = new AudioSoundPlayer(this, selectedScale);
        popUpDialog = new Dialog(this);
        countDown = new noteCountDownTimer(10000, 1000); // 10 second timer
        countDown.start();
        timerDisplay = findViewById(R.id.timer);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNote(Integer.parseInt(v.getTag().toString()));
            }
        };

        noteButtons = new Vector<>();

        for (int i=0; i<7; i++) {
            int resID = getResources().getIdentifier("button" + (i + 1), "id", getPackageName());
            Button currentButton = findViewById(resID);
            currentButton.setOnClickListener(listener);
            currentButton.setText(scaleNotes[i]);
            noteButtons.add(currentButton);
        }
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
            soundPlayer.release();
            TextView txt;
            Button level;
            popUpDialog.setContentView(R.layout.popup_dialog);
            txt = (TextView) popUpDialog.findViewById(R.id.popUpText);
            level = popUpDialog.findViewById(R.id.continueButton);
            level.setText(getString(R.string.tryagain));
            txt.setText(getString(R.string.game_over));
            popUpDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            popUpDialog.show();
        }

        if(gameWon){
            soundPlayer.release();
            TextView txt;
            Button level;
            popUpDialog.setContentView(R.layout.popup_dialog);
            level = popUpDialog.findViewById(R.id.continueButton);
            level.setText(getString(R.string.next));
            txt = (TextView)popUpDialog.findViewById(R.id.popUpText);
            txt.setText(getString(R.string.win));
            popUpDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            popUpDialog.show();
        }
    }

    protected void playRandomNote(){
        currentRandomNote = randGenerator.nextInt((numButtons - min) + 1) + min;
        soundPlayer.playNote(currentRandomNote);
    }

    private void checkUserNote(int playedNote){
        if(rightGuesses == 0 && wrongGuesses == 0){
            currentRandomNote = soundPlayer.firstRandomNote;
        }
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
        if(!gameOver && !gameWon){
            playRandomNote();
            countDown = new noteCountDownTimer(10000, 1000);
            countDown.start();
        }

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

    public void restartLevel(View view)
    {
        MainGameActivity.this.recreate();
    }

    public void mainMenu(View view){
        Intent intent = new Intent(MainGameActivity.this, MainMenuActivity.class);
        finish();
        startActivity(intent);
    }

}
