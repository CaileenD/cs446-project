package ca.uwaterloo.tonality;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        Spinner spinner = findViewById(R.id.scaleSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scales, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        loadLevelImages();

        points = findViewById(R.id.points);
        points.setText(String.valueOf(PointStorage.getInstance().getScore()));
    }

    /**
     * Sets alpha of levels according to unlocked status
     */
    private void loadLevelImages() {
        for (int i = 0; i < 6; ++i) {
            // first level of any scale unlocked
            if (i == 0) {
                PointStorage.getInstance().store(selectedScale, String.valueOf(1), true);
            }

            int bubbleId = getResources().getIdentifier("levelBubble" + (i + 1), "id", getPackageName());
            int noteId = getResources().getIdentifier("levelNote" + (i + 1), "id", getPackageName());

            ImageView levelBubble = findViewById(bubbleId);
            ImageView levelNote = findViewById(noteId);
            int unlocked = PointStorage.getInstance().load(selectedScale, String.valueOf(i + 1) )? 1 : 0;

            levelBubble.setAlpha((float) Math.max(0.5, unlocked));
            levelNote.setAlpha((float) Math.max(0.5, unlocked));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String scale = adapterView.getItemAtPosition(i).toString();
        selectedScale = scale;
        loadLevelImages();
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
