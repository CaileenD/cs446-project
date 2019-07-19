package ca.uwaterloo.tonality;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;

public class MainGameActivity extends AppCompatActivity implements Observer {

    private String TAG = "MainGameActivity";

    private static final long startTimeInSecs = 10;
    private TextView timerDisplay;
    private long secondsLeft = startTimeInSecs;
    private AudioSoundPlayer soundPlayer;
    public int numActiveButtons; // number of note buttons at bottom of screen
    public noteCountDownTimer countDown; // Counts how long user has to select note
    private int rightGuesses = 0;
    private int wrongGuesses = 0;
    final int min = 1;
    private Random randGenerator = new Random();
    private int currentRandomNote = 0;
    private int health = 5;
    private long score;
    Dialog popUpDialog;
    private PlayerHealthObserverable playerHealth;
    private CPUHealthObservable CPUHealth;
    private List<ImageView> playerHealthIcons;
    private List<ImageView> cpuHealthIcons;
    Animation myFadeOutAnimation;



    View.OnClickListener listener;
    Vector<Button> noteButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        Intent intent = this.getIntent();
        // Set up ui text
        String selectedScale = intent.getStringExtra("selectedScale");
        Integer levelDifficulty = intent.getIntExtra("levelDifficulty", 1);
        TextView scaleInfo = findViewById(R.id.scaleInfo);
        scaleInfo.setText(selectedScale.toUpperCase());
        TextView levelInfo = findViewById(R.id.levelInfo);
        levelInfo.setText("LEVEL" + (levelDifficulty - 1));


        // Player health observer setup
        playerHealth = PlayerHealthObserverable.getInstance(health);
        playerHealth.addObserver(this);

        CPUHealth = CPUHealthObservable.getInstance(health);
        CPUHealth.addObserver(this);

        myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        // Fill up the Image view for the player health icons
        playerHealthIcons = getImageViewsForHealth(playerHealth);
        cpuHealthIcons = getImageViewsForHealth(CPUHealth);

        // Set up game related things
        numActiveButtons = levelDifficulty;
        score = numActiveButtons;
        List<String> SOUND_MAP = ScaleBuilder.buildScale(selectedScale);
        soundPlayer = new AudioSoundPlayer(this, SOUND_MAP, numActiveButtons);
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
            String noteText = SOUND_MAP.get(i).substring(0, SOUND_MAP.get(i).length() - 1).toUpperCase();
            currentButton.setText(noteText);
            if (i+1>numActiveButtons) {
                currentButton.setEnabled(false);
                currentButton.setAlpha(0.3f);
            }
            noteButtons.add(currentButton);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* remove observer at this point */
        if(playerHealth!=null)
            playerHealth.deleteObserver(this);

        playerHealthIcons.clear();
    }

    @Override
    protected void onStop(){
        super.onStop();

        playerHealthIcons.clear();
        cpuHealthIcons.clear();
    }

    @Override
    protected void onPause(){
        super.onPause();
        popUpDialog.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();

      //  popUpDialog = new Dialog(this);
        playerHealthIcons = getImageViewsForHealth(playerHealth);
        cpuHealthIcons = getImageViewsForHealth(CPUHealth);

        playerHealth.resetHealth(health);
        CPUHealth.resetHealth(health);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable!=null && observable instanceof PlayerHealthObserverable) {
            /* Typecast to PlayerHealth */
            PlayerHealthObserverable playerHealth=(PlayerHealthObserverable) observable;

            /* update UI by using getters methods */
            playerHealth.decrementUserHealth();
            try{
                playerHealthIcons.get(playerHealth.getUserHealth()).startAnimation(myFadeOutAnimation);
                playerHealthIcons.get(playerHealth.getUserHealth()).setVisibility(View.INVISIBLE);
            }
            catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        else if (observable!=null && observable instanceof CPUHealthObservable) {
            /* Typecast to PlayerHealth */
            CPUHealthObservable cpuHealth =(CPUHealthObservable) observable;

            /* update UI by using getters methods */
            cpuHealth.decrementUserHealth();
            try{
                cpuHealthIcons.get(cpuHealth.getUserHealth()).startAnimation(myFadeOutAnimation);
                cpuHealthIcons.get(cpuHealth.getUserHealth()).setVisibility(View.INVISIBLE);
            }
            catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
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
    }

    protected void playRandomNote(){
        currentRandomNote = randGenerator.nextInt((numActiveButtons - min) + 1) + min;
        soundPlayer.playNote(currentRandomNote);
    }

    private void checkUserNote(int playedNote){
        if(rightGuesses == 0 && wrongGuesses == 0){
            currentRandomNote = soundPlayer.firstRandomNote;
        }
        if(playedNote == currentRandomNote){
            rightGuesses++;
            update(CPUHealth, 1);
            Toast.makeText(this, "Right guess: " + rightGuesses, Toast.LENGTH_LONG).show();
            if(rightGuesses >= 5){
                gameWon();
            }
        } else {
            wrongGuesses++;
            score--;
            update(playerHealth, 1);
            if(wrongGuesses >= 5 ){
                gameOver();
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
                gameOver();
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

    private void endScreen (boolean win) {
        countDown.cancel();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error! " + e.getMessage());
        }

        soundPlayer.release();
        TextView txt;
        Button level;

        popUpDialog.setContentView(R.layout.popup_dialog);

        level = popUpDialog.findViewById(R.id.continueButton);
        level.setText(getString(R.string.next));

        String s = win? getString(R.string.win) : getString(R.string.game_over);
        txt = popUpDialog.findViewById(R.id.popUpText);
        txt.setText(s);

        score = (win)? Math.max(1, score) : 0;
        saveScore();

        popUpDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if(!((Activity) this).isFinishing())
        {
            try {
                popUpDialog.show();
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }
        }

    }

    private List<ImageView> getImageViewsForHealth(Observable observable) {
        List<ImageView> healths = new ArrayList<>();

        if (observable!=null && observable instanceof PlayerHealthObserverable) {
            healths.add((ImageView) findViewById(R.id.userNote0));
            healths.add((ImageView) findViewById(R.id.userNote2));
            healths.add((ImageView) findViewById(R.id.userNote1));
            healths.add((ImageView) findViewById(R.id.userNote3));
            healths.add((ImageView) findViewById(R.id.userNote4));
        }
        else if (observable!=null && observable instanceof CPUHealthObservable) {
            healths.add((ImageView) findViewById(R.id.cpuNote0));
            healths.add((ImageView) findViewById(R.id.cpuNote1));
            healths.add((ImageView) findViewById(R.id.cpuNote2));
            healths.add((ImageView) findViewById(R.id.cpuNote3));
            healths.add((ImageView) findViewById(R.id.cpuNote4));
        }


        return healths;
    }

    private void gameWon() {
        endScreen(true);
    }

    private void gameOver() {
        endScreen(false);
    }

    public void saveScore() {
        PointStorage.getInstance().incrementScore(score);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(popUpDialog != null) {
            popUpDialog.dismiss();
        }
    }
}
