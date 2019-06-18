package ca.uwaterloo.tonality;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Locale;

public class MainGameActivity extends AppCompatActivity {
    private static final long startTimeInMilliSecs = 100000;
    private TextView timerDisplay;
    private CountDownTimer timer;
    private long timeLeftInMilliSecs = startTimeInMilliSecs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        timerDisplay = findViewById(R.id.timer);

        timer = new CountDownTimer(timeLeftInMilliSecs, 1000){
            @Override
            public void onTick(long milliSecsTillFinished) {
                timeLeftInMilliSecs = milliSecsTillFinished;
                updateCountDownText();
            }

            public void onFinish(){

            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMilliSecs / 1000) / 60;
        int seconds = (int) (timeLeftInMilliSecs / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timerDisplay.setText(timeLeftFormatted);
    }
}
