package com.ToneAndroidPackage;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.SystemClock;


public class ToneAndroidModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
  	// and modified by Steve Pomeroy <steve@staticfree.info>

    AudioTrack audioTrack;
    private int duration = 500; // milliseconds seconds
    private int sampleRate = 8000;
    private int numSamples = duration * (sampleRate/1000);
    private double sample[] = new double[numSamples];
    private int freqOfTone = 880;
    private final byte generatedSnd[] = new byte[2 * 16000];

    public ToneAndroidModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ToneAndroid";
    }

    void playblocking(int freq, int durationms) {
      	freqOfTone = freq;
      	duration = durationms;
      	numSamples = duration * (sampleRate/1000);

        genTone();
        playSound();
    }

    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
    	try {
    	// WAS STREAM_NOTIFICATION
    	audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);

      audioTrack.write(generatedSnd, 0, numSamples * 2);
      audioTrack.play();
      while(audioTrack.getPlaybackHeadPosition() < (numSamples/2)) {
      	SystemClock.sleep(1);
      }
      audioTrack.release();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }



    @ReactMethod
    public void play(int frequency, int durationms) {
      playblocking(frequency,durationms);
    }
}
