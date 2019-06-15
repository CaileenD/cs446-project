package ca.uwaterloo.tonality;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startGame(View view)
    {
        Intent intent = new Intent(MainMenu.this, MainGameActivity.class);
        startActivity(intent);
    }
}
