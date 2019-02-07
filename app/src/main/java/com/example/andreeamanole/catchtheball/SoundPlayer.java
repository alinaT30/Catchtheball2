package com.example.andreeamanole.catchtheball;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 3;

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;
    private static int loadIsCompleteSound;

    public SoundPlayer(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        } else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }
        loadIsCompleteSound = soundPool.load(context, R.raw.pacman_intro, 1);
        hitSound = soundPool.load(context, R.raw.hit, 1);
        overSound = soundPool.load(context, R.raw.over, 1);


    }

    public void playLoadIsCompleteSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        //soundPool.stop(loadIsCompleteSound);
        soundPool.play(loadIsCompleteSound, 1.0f, 1.0f, 1, 500, 1.0f);
    }


    public void playHitSound(){

        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }

    public void playOverSound(){
        soundPool.stop(loadIsCompleteSound);
        soundPool.play(overSound, 1.0f, 1.0f, 2, 0, 1.0f);
    }



}
