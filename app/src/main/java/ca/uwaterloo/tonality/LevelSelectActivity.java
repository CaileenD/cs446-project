package ca.uwaterloo.tonality;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelSelectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String selectedScale = "C Major"; // default value
    private TextView points;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        LevelStorage.init(getApplicationContext());

        Spinner spinner = findViewById(R.id.scaleSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scales, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        points = findViewById(R.id.points);
        points.setText(String.valueOf(PointStorage.getInstance().getScore()));

        star1 = findViewById(R.id.level1EmptyStar1);
        star2 = findViewById(R.id.level1EmptyStar2);
        star3 = findViewById(R.id.level1EmptyStar3);

        try{
            if (LevelStorage.getInstance().getStarsForLevel(selectedScale, "2") == 1){
                star1.setImageResource(R.drawable.filled_star);
            }
            else if (LevelStorage.getInstance().getStarsForLevel(selectedScale, "2") == 2){
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
            }
            else if (LevelStorage.getInstance().getStarsForLevel(selectedScale, "2") == 3){
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.filled_star);
            }
        }
        catch (RuntimeException e){

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String scale = adapterView.getItemAtPosition(i).toString();
        selectedScale = scale;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onLevelClick(View view) {
        String levelName = getResources().getResourceEntryName(view.getId());
        int levelDifficulty = Integer.valueOf(levelName.substring(levelName.length()-1)) + 1;
        Intent intent = new Intent(LevelSelectActivity.this, MainGameActivity.class);
        intent.putExtra("selectedScale", selectedScale);
        intent.putExtra("levelDifficulty", levelDifficulty);
        startActivity(intent);
    }
}
