package ca.uwaterloo.tonality;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AudioSoundPlayer {

    private static final String SOUND_FOLDER = "sounds";
    private String TAG = "AudioSoundPlayer";
    private static List<String> SOUND_MAP = new ArrayList<>();
    SoundPool soundPool;
    private AssetManager assetManager;
    private SparseArray<NoteSound> soundList = new SparseArray<>();
    private int numloaded = 0;
    public int firstRandomNote = 0;

    public AudioSoundPlayer(Context context, List<String> SOUND_MAP, final int numActiveNotes) {
        this.assetManager = context.getAssets();

        this.SOUND_MAP = SOUND_MAP;

        // Build the SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(7)
                .build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int mySoundId, int status) {
                numloaded++;
                // Play a random note
                if(numloaded == 7){
                    firstRandomNote = new Random().nextInt(numActiveNotes) + 1;
                    playNote(firstRandomNote);
                }
            }
        });

        // Load the note sounds
        loadSounds();
    }


    private class PlayThread extends Thread {
        int note;

        public PlayThread(int note) {
            this.note = note;
        }

        @Override
        public void run() {
            soundPool.play(this.note, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    private void loadSounds(){
        String[] soundFiles;
        try {
            soundFiles = assetManager.list(SOUND_FOLDER);
            Log.d(TAG, "Fetched " + soundFiles.length + " sound files");
        } catch (IOException e) {
            Log.e(TAG, "Error accessing sound folder", e);
            return;
        }

        for(String fileName:SOUND_MAP){
            try {
                String path = SOUND_FOLDER + "/" + fileName + ".wav";
                NoteSound note = new NoteSound(path);
                int noteID = load(note);
                soundList.put(noteID, note);
            } catch (IOException e) {
                Log.e(TAG, "Could not load sound: " + fileName, e);
            }
        }

    }

    private int load(NoteSound note) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(note.getPathName());
        int soundId = soundPool.load(fileDescriptor, 1);
        note.setId(soundId);
        return soundId;
    }

    public void playNote(int note){
        PlayThread playThread = new PlayThread(note);
        playThread.start();
    }

    public void release(){
        Log.d(TAG, "Cleaning resources..");
        soundPool.release();
    }

    public SparseArray<NoteSound> getSounds() {
        return this.soundList;
    }

}
