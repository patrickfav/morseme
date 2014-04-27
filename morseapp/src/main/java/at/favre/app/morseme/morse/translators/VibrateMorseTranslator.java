package at.favre.app.morseme.morse.translators;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;
import at.favre.app.morseme.morse.Morsable;
import at.favre.app.morseme.morse.MorseUtil;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class VibrateMorseTranslator extends AsyncTask<Void,Void,Void> implements Morsable {

	private static final String TAG = VibrateMorseTranslator.class.getSimpleName();
	private List<EMorsePart> sequenze;
	private Vibrator vibrator;
	private TranslatorListener listener;

	private Handler handler;
	private boolean run;

	public VibrateMorseTranslator(List<EMorsePart> sequenze, Vibrator vibrator, TranslatorListener listener) {
		this.sequenze = sequenze;
		this.vibrator = vibrator;
		this.listener = listener;
		this.handler = new Handler();
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
					vibrator.vibrate(lengthMs);
					SystemClock.sleep(lengthMs);
				}
			} else {
				break;
			}
		}
		run=false;
		Log.d(TAG,"done morse");
	}

	@Override
	public void morse() {

	}
}
