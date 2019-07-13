package ca.uwaterloo.tonality;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InternalStorageWriter {

    private static final String filename = "score_data";
    private FileOutputStream fos;
    private FileInputStream fis;
    private InputStreamReader isr;
    private BufferedReader br;

    private boolean fileExists(Context context) {
        boolean exists = true;
        File file = context.getFileStreamPath(filename);

        if (file == null || !file.exists()) {
            exists = false;
        }

        return exists;
    }

    public InternalStorageWriter(Context context) throws FileNotFoundException {
        if (!fileExists(context)) {
            new File(context.getFilesDir(), filename);
        }

        // write
        fos = context.openFileOutput(filename, Context.MODE_PRIVATE);

        // read
        fis = context.openFileInput(filename);
        isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
    }

    public void incrementScore(long score) {
        try {
            fos.write(String.valueOf(readScore() + score).getBytes());
            fos.flush();
        } catch (IOException e) {
            // could not write score
        }
    }

    public long readScore() {
        long score;

        try {
            score = Long.parseLong(br.readLine());
        } catch (IOException e) {
            score = 0;
        }

        return score;
    }
}
