package ca.uwaterloo.tonality;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class LevelSelectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String selectedScale = "C Major"; // default value
    private TextView points;
    private List<ImageView> stars;
    private List<TextView> levelCosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks screen in portrait
        levelCosts = new ArrayList<>();

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

        loadLevelCosts();
        loadPoints();
        stars = loadImageViews();
        setStars();
    }

    private void loadLevelCosts() {
        for (int i = 1; i <= 6; ++i) {
            String id = "levelCost" + i;
            int resID = getResources().getIdentifier(id, "id", getPackageName());
            TextView v = findViewById(resID);
            levelCosts.add(v);
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
        int lineId = getResources().getIdentifier("line" + (level), "id", getPackageName());
        ImageView levelBubble = findViewById(bubbleId);
        ImageView levelNote = findViewById(noteId);
        ImageView lineConnector = findViewById(lineId);
        int unlocked = PointStorage.getInstance().load(selectedScale, String.valueOf(level) )? 1 : 0;

        levelBubble.setAlpha((float) Math.max(0.5, unlocked));
        levelNote.setAlpha((float) Math.max(0.5, unlocked));
        if (level != 1) lineConnector.setAlpha((float) Math.max(0.05, unlocked));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String scale = adapterView.getItemAtPosition(i).toString();
        selectedScale = scale;
        resetCosts();
        loadAllLevelImage();
        setStars();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void resetCosts() {
      for (TextView v : levelCosts)  {
          v.setText("");
      }
    }

    private void showCost(final int levelDifficulty) {
        for(int i = 0; i < levelCosts.size(); ++i) {
            TextView v = levelCosts.get(i);
            if (levelDifficulty != i + 1) {
                v.setText("");
            } else {
                // if cost is already showing, purchase else display cost
                if (v.getText().toString().trim().length() == 0) {
                    v.setText(String.valueOf(levelDifficulty + 1));
                } else {
                    //purchase level if enough points and previous level unlocked
                    long points = PointStorage.getInstance().getScore();
                    boolean prevUnlocked = PointStorage.getInstance().load(selectedScale, String.valueOf(levelDifficulty - 1));


                    if (levelDifficulty + 1 <= points && prevUnlocked) { //unlock level
                        PointStorage.getInstance().store(selectedScale, String.valueOf(levelDifficulty), true);
                        PointStorage.getInstance().incrementScore(-(levelDifficulty+1));

                        // change alpha of level
                        loadLevelImage(levelDifficulty);
                        loadPoints();
                        resetCosts();
                    }
                }
            }
        }
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
            showCost(levelDifficulty);
        }
    }

    private List<ImageView> loadImageViews(){
        List<ImageView> stars = new ArrayList<>();

        stars.add((ImageView)findViewById(R.id.level1EmptyStar1));
        stars.add((ImageView)findViewById(R.id.level1EmptyStar2));
        stars.add((ImageView)findViewById(R.id.level1EmptyStar3));

        stars.add((ImageView)findViewById(R.id.level2EmptyStar1));
        stars.add((ImageView)findViewById(R.id.level2EmptyStar2));
        stars.add((ImageView)findViewById(R.id.level2EmptyStar3));

        stars.add((ImageView)findViewById(R.id.level3EmptyStar1));
        stars.add((ImageView)findViewById(R.id.level3EmptyStar2));
        stars.add((ImageView)findViewById(R.id.level3EmptyStar3));

        stars.add((ImageView)findViewById(R.id.level4EmptyStar1));
        stars.add((ImageView)findViewById(R.id.level4EmptyStar2));
        stars.add((ImageView)findViewById(R.id.level4EmptyStar3));

        stars.add((ImageView)findViewById(R.id.level5EmptyStar1));
        stars.add((ImageView)findViewById(R.id.level5EmptyStar2));
        stars.add((ImageView)findViewById(R.id.level5EmptyStar3));

        stars.add((ImageView)findViewById(R.id.level6EmptyStar1));
        stars.add((ImageView)findViewById(R.id.level6EmptyStar2));
        stars.add((ImageView)findViewById(R.id.level6EmptyStar3));

        return stars;
    }

    private void setStars(){
        for (int i = 0; i < stars.size(); i += 3){
            String level = Integer.toString((i / 3) + 2);
            try{
                if (LevelStorage.getInstance().getStarsForLevel(selectedScale, level) == 1){
                    stars.get(i).setImageResource(R.drawable.filled_star);
                    stars.get(i + 1).setImageResource(R.drawable.empty_star);
                    stars.get(i + 2).setImageResource(R.drawable.empty_star);
                }
                else if (LevelStorage.getInstance().getStarsForLevel(selectedScale, level) == 2){
                    stars.get(i).setImageResource(R.drawable.filled_star);
                    stars.get(i + 1).setImageResource(R.drawable.filled_star);
                    stars.get(i + 2).setImageResource(R.drawable.empty_star);
                }
                else if (LevelStorage.getInstance().getStarsForLevel(selectedScale, level) == 3){
                    stars.get(i).setImageResource(R.drawable.filled_star);
                    stars.get(i + 1).setImageResource(R.drawable.filled_star);
                    stars.get(i + 2).setImageResource(R.drawable.filled_star);
                }
                else{
                    stars.get(i).setImageResource(R.drawable.empty_star);
                    stars.get(i + 1).setImageResource(R.drawable.empty_star);
                    stars.get(i + 2).setImageResource(R.drawable.empty_star);
                }
            }
            catch (RuntimeException e){

            }
        }

    }
}
