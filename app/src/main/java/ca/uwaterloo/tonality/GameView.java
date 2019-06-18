package ca.uwaterloo.tonality;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    public static final int numButtons = 7; // number of note buttons at bottom of screen
    private ArrayList<noteButton> scaleNotes = new ArrayList<noteButton>();
    private Paint magenta, green;        // TODO: These colours are hideous so change them
    private int buttonRadius, buttonHeight;    // TODO: Remove when we change buttons to circles
    private AudioSoundPlayer soundPlayer;
    public noteCountDownTimer countDown; // Counts how long user has to select note
    private long secondsLeft; // Shows user how many seconds are left to select the note
    private boolean gameOver = false;
    private int rightGuesses = 0;
    private int wrongGuesses = 0;

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        magenta = new Paint();
        magenta.setColor(Color.MAGENTA);
        green = new Paint();
        green.setColor(Color.GREEN);
        soundPlayer = new AudioSoundPlayer(context);
        init(context);
    }

    public void init(Context context){
        countDown = new noteCountDownTimer(10000, 1000); // 10 second timer
        playRandomNote();
        countDown.start();

    }

    protected void playRandomNote(){
        final int min = 1;
        final int random = new Random().nextInt((numButtons - min) + 1) + min;
        soundPlayer.playNote(random);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        buttonRadius = w / 15;
        buttonHeight = buttonRadius;

        int left = buttonRadius;

        for(int i=0; i < numButtons; i++){
            left += buttonRadius * 1.5;
            int right = left + buttonRadius;

            RectF rect = new RectF(left, 5, right, buttonHeight);
            scaleNotes.add(new noteButton(rect, i+1));
        }

    }

    public class noteCountDownTimer extends CountDownTimer {
        public noteCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            // TODO: Game over or lose a point code goes here
            wrongGuesses++;
            if(wrongGuesses < 5){
                resetTimer();
            } else {
                gameOver = true;
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {
            secondsLeft = millisUntilFinished / 1000;
            // TODO: Update screen with seconds remaining
        }
    }

    // Draw the buttons on the screen
    @Override
    protected void onDraw(Canvas canvas) {
        for (noteButton k : scaleNotes) {
            canvas.drawOval(k.rect, k.pressed ? green : magenta);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            noteButton k = keyForCoords(x,y);

            if (k != null) {
                k.pressed = isDownAction;
            }
        }

        ArrayList<noteButton> tmp = new ArrayList<>(scaleNotes);

        for (noteButton k : tmp) {
            if (k.pressed) {
                if (!soundPlayer.isNotePlaying(k.sound)) {
                    soundPlayer.playNote(k.sound);
                    requestLayout();   // calls onDraw() again
                    resetTimer();
                } else {
                    releaseKey(k);
                }
            } else {
                soundPlayer.stopNote(k.sound);
                releaseKey(k);
            }
        }

        return true;
    }

    private void resetTimer(){
        countDown.cancel();
        try{
            Thread.sleep(2000);
        } catch(Exception e){
            // Do nothing
        }
        playRandomNote();
        countDown = new noteCountDownTimer(10000, 1000);
        countDown.start();
    }

    private noteButton keyForCoords(float x, float y) {

        for (noteButton k : scaleNotes) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        return null;
    }

    private void releaseKey(final noteButton k) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k.pressed = false;
                handler.sendEmptyMessage(0);
            }
        }, 100);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            requestLayout();
        }
    };

}
