package at.favre.app.morseme.morse.translators;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class SineMorseTranslator extends ATranslator{

	private List<EMorsePart> sequenze;

	private final int sampleRate = 8000;
	private final double freqOfTone = 800; // hz
	private byte generatedSnd[];
	private AudioTrack audioTrack;

	public SineMorseTranslator(List<EMorsePart> sequenze, ITranslatorListener listener, long morseLengthMs, long pauseLengthMs) {
		super(sequenze, listener, morseLengthMs, pauseLengthMs);
	}

	@Override
	protected void startTranslator(long length) {
		genTone(length);
		playSound();
	}

	@Override
	protected void stopTranslator() {
		audioTrack.release();
	}

	void genTone(long durationMS){
		int numSamples = (int) durationMS  * sampleRate / 1000;
		double[] sample = new double[numSamples];
		generatedSnd = new byte[2 * numSamples];

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
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					sampleRate, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
					AudioTrack.MODE_STATIC);
			audioTrack.write(generatedSnd, 0, generatedSnd.length);
			audioTrack.play();
		} catch (Exception e) {
			Log.w(TAG,"Error while plaing sound",e);
		}
	}


}
