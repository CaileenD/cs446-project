package ca.uwaterloo.tonality;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;
import android.util.SparseArray;

import java.io.InputStream;

public class AudioSoundPlayer {

    private String TAG = "AudioSoundPlayer";
    private SparseArray<PlayThread> threadMap; // SparseArray is more memory efficient than HashMap
    private Context context;
    private static final SparseArray<String> SOUND_MAP = new SparseArray<>();

    static {
        // C Major scale
        SOUND_MAP.put(1, "c4");
        SOUND_MAP.put(2, "d4");
        SOUND_MAP.put(3, "e4");
        SOUND_MAP.put(4, "f4");
        SOUND_MAP.put(5, "g4");
        SOUND_MAP.put(6, "a4");
        SOUND_MAP.put(7, "b4");
    }

    public AudioSoundPlayer(Context context){
        this.context = context;
        threadMap = new SparseArray<>();
    }

    public void playNote(int note) {
        if (!isNotePlaying(note)) {
            PlayThread thread = new PlayThread(note);
            thread.start();
            threadMap.put(note, thread);
        }
    }

    public void stopNote(int note) {
        PlayThread thread = threadMap.get(note);

        if (thread != null) {
            threadMap.remove(note);
        }
    }

    public boolean isNotePlaying(int note) {
        return threadMap.get(note) != null;
    }

    private class PlayThread extends Thread {
        int note;
        AudioTrack audioTrack;

        public PlayThread(int note) {
            this.note = note;
        }

        @Override
        public void run() {
            try {
                String path = "sounds/" + SOUND_MAP.get(note) + ".wav";
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor fileDescriptor = assetManager.openFd(path);
                long fileSize = fileDescriptor.getLength();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                AudioFormat audioFormat = new AudioFormat.Builder()
                                                .setSampleRate(44100)
                                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                                .build();

                audioTrack = new AudioTrack(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build(),
                        audioFormat, bufferSize, AudioTrack.MODE_STREAM, 1);

                audioTrack.play();
                InputStream audioStream;
                int headerOffset = 0x2C; long bytesWritten = 0; int bytesRead = 0;

                audioStream = assetManager.open(path);
                audioStream.read(buffer, 0, headerOffset);

                while (bytesWritten < fileSize - headerOffset) {
                    bytesRead = audioStream.read(buffer, 0, bufferSize);
                    bytesWritten += audioTrack.write(buffer, 0, bytesRead);
                }

                audioTrack.stop();
                audioTrack.release();

            } catch (Exception e) {
                Log.e(TAG, "Error occurred in AudioSoundPlayer while playing note!" + e.getMessage());
            } finally {
                if (audioTrack != null) {
                    audioTrack.release();
                }
            }
        }
    }
}
