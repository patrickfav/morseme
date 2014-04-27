package at.favre.app.morseme.morse.translators;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;
import at.favre.app.morseme.morse.MorseUtil;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class SineMorseTranslator extends AsyncTask<Void,Void,Void>{

	private static final String TAG = SineMorseTranslator.class.getSimpleName();
	private List<EMorsePart> sequenze;
	private boolean run;
	private TranslatorListener listener;

	private final int sampleRate = 8000;
	private final double freqOfTone = 800; // hz

	private byte generatedSnd[];

	private Handler handler = new Handler();

	public SineMorseTranslator(List<EMorsePart> sequenze, TranslatorListener listener) {
		this.sequenze = sequenze;
		this.listener = listener;
		run= false;
	}

	@Override
	protected Void doInBackground(Void... voids) {

		play();
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		super.onPostExecute(aVoid);
		if(listener != null) {
			listener.onMorseComplete(false);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		run = false;
	}

	private void play() {
		Log.d(TAG,"Playing "+sequenze);
		run=true;
		for (EMorsePart eMorsePart : sequenze) {
			if(run) {
				long lengthMs = MorseUtil.getAtomicMorseLength()*eMorsePart.getRelativeLength();
				if (eMorsePart.isPause()) {
					SystemClock.sleep(lengthMs);
				} else {
					genTone(lengthMs);
					playSound();
					SystemClock.sleep(lengthMs);
				}
			} else {
				break;
			}
		}
		run=false;
		Log.d(TAG,"done morse");
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
			final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
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
