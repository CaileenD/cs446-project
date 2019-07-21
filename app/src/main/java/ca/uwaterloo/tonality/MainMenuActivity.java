package ca.uwaterloo.tonality;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks screen in portrait
    }

    public void startGame(View view) {
        Intent intent = new Intent(MainMenuActivity.this, LevelSelectActivity.class);
        PointStorage.init(getApplicationContext());
        startActivity(intent);
    }
}
