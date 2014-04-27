package at.favre.app.morseme.morse.translators;

import android.os.Vibrator;

import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class VibrateMorseTranslator extends ATranslator {
	private Vibrator vibrator;

	public VibrateMorseTranslator(List<EMorsePart> sequenze, ITranslatorListener listener, long morseLengthMs, long pauseLengthMs, Vibrator vibrator) {
		super(sequenze, listener, morseLengthMs, pauseLengthMs);
		this.vibrator = vibrator;
	}

	@Override
	protected void startTranslator(long length) {
		vibrator.vibrate(length);
	}

	@Override
	protected void stopTranslator() {

	}
}
