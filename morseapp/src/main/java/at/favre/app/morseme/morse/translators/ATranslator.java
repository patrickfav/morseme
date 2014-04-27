package at.favre.app.morseme.morse.translators;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;

/**
 * Created by PatrickF on 27.04.2014.
 */
public abstract class ATranslator extends AsyncTask<Void,Void,Void> {
	protected static final String TAG = FlashlightMorseTranslator.class.getSimpleName();
	protected List<EMorsePart> sequenze;
	protected ITranslatorListener listener;

	protected long morseLengthMs;
	protected long pauseLengthMs;

	public ATranslator(List<EMorsePart> sequenze,ITranslatorListener listener,long morseLengthMs,long pauseLengthMs) {
		this.listener = listener;
		this.sequenze = sequenze;
		this.morseLengthMs = morseLengthMs;
		this.pauseLengthMs = pauseLengthMs;
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

	protected void play() {
		Log.d(TAG, "Playing " + sequenze);
		for (EMorsePart eMorsePart : sequenze) {
			if(!isCancelled()) {
				if (eMorsePart.isPause()) {
					SystemClock.sleep(pauseLengthMs*eMorsePart.getRelativeLength());
				} else {
					startTranslator(morseLengthMs*eMorsePart.getRelativeLength());
					SystemClock.sleep(morseLengthMs*eMorsePart.getRelativeLength());
					stopTranslator();
				}
			} else {
				break;
			}
		}
		Log.d(TAG,"done morse");
	}

	protected abstract void startTranslator(long length);

	protected abstract void stopTranslator();
}
