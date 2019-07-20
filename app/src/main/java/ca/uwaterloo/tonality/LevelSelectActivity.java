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

        loadPoints();

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

    private void loadPoints() {
        points = findViewById(R.id.points);
        points.setText(String.valueOf(PointStorage.getInstance().getScore()));
    }

    /**
     * Sets alpha of levels according to unlocked status
     */
    private void loadAllLevelImage() {
        for (int i = 0; i < 6; ++i) {
            // first level of any scale unlocked
            if (i == 0) {
                PointStorage.getInstance().store(selectedScale, String.valueOf(1), true);
            }

            loadLevelImage(i + 1);
        }
    }

    private void loadLevelImage(int level) {
        int bubbleId = getResources().getIdentifier("levelBubble" + (level), "id", getPackageName());
        int noteId = getResources().getIdentifier("levelNote" + (level), "id", getPackageName());

        ImageView levelBubble = findViewById(bubbleId);
        ImageView levelNote = findViewById(noteId);
        int unlocked = PointStorage.getInstance().load(selectedScale, String.valueOf(level) )? 1 : 0;

        levelBubble.setAlpha((float) Math.max(0.5, unlocked));
        levelNote.setAlpha((float) Math.max(0.5, unlocked));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String scale = adapterView.getItemAtPosition(i).toString();
        selectedScale = scale;
        loadAllLevelImage();

        try{
            if (LevelStorage.getInstance().getStarsForLevel(selectedScale, "1") == 1){
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.empty_star);
                star3.setImageResource(R.drawable.empty_star);
            }
            else if (LevelStorage.getInstance().getStarsForLevel(selectedScale, "2") == 2){
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.empty_star);
            }
            else if (LevelStorage.getInstance().getStarsForLevel(selectedScale, "2") == 3){
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.filled_star);
            }
            else{
                star1.setImageResource(R.drawable.empty_star);
                star2.setImageResource(R.drawable.empty_star);
                star3.setImageResource(R.drawable.empty_star);
            }
        }
        catch (RuntimeException e){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onLevelClick(View view) {
        String levelName = getResources().getResourceEntryName(view.getId());
        int levelDifficulty = Integer.valueOf(levelName.substring(levelName.length()-1));
        boolean unlocked = PointStorage.getInstance().load(selectedScale, String.valueOf(levelDifficulty));

        if (unlocked) {
            Intent intent = new Intent(LevelSelectActivity.this, MainGameActivity.class);
            intent.putExtra("selectedScale", selectedScale);
            intent.putExtra("levelDifficulty", levelDifficulty + 1);
            startActivity(intent);
        } else {
            long points = PointStorage.getInstance().getScore();

            if (levelDifficulty <= points) { //unlock level
                PointStorage.getInstance().store(selectedScale, String.valueOf(levelDifficulty), true);
                PointStorage.getInstance().incrementScore(-points);

                // change alpha of level
                loadLevelImage(levelDifficulty);
                loadPoints();
            }
        }
    }
}
