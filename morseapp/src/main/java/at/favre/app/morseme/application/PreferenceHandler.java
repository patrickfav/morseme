package at.favre.app.morseme.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PatrickF on 27.04.2014.
 */
public class PreferenceHandler {
	public enum SmsTranslator {VIBRATOR,SINE,TORCH;}

	private static final String SINE_MORSE_LENGTH_MS = "SINE_MORSE_LENGTH_MS";
	private static final String SINE_PAUSE_LENGTH_MS = "SINE_PAUSE_LENGTH_MS";
	private static final String VIBRATOR_MORSE_LENGTH_MS = "VIBRATOR_MORSE_LENGTH_MS";
	private static final String VIBRATOR_PAUSE_LENGTH_MS = "VIBRATOR_PAUSE_LENGTH_MS";
	private static final String TORCH_MORSE_LENGTH_MS = "TORCH_MORSE_LENGTH_MS";
	private static final String TORCH_PAUSE_LENGTH_MS = "TORCH_PAUSE_LENGTH_MS";

	private static final String SMS_TRANSLATOR = "SMS_TRANSLATOR";

	private Context context;
	private SharedPreferences prefs;

	public PreferenceHandler(final Context context) {
		this.context = context;
	}
	
	public void setSineMorseLength(long ms) {
		getPreferences().edit().putLong(SINE_MORSE_LENGTH_MS,ms).commit();
	}
	public long getSineMorseLength() {
		return getPreferences().getLong(SINE_MORSE_LENGTH_MS,75l);
	}
	public void setSinePauseLength(long ms) {
		getPreferences().edit().putLong(SINE_PAUSE_LENGTH_MS,ms).commit();
	}
	public long getSinePauseLength() {
		return getPreferences().getLong(SINE_PAUSE_LENGTH_MS,50l);
	}

	public void setVibratorMorseLength(long ms) {
		getPreferences().edit().putLong(VIBRATOR_MORSE_LENGTH_MS,ms).commit();
	}
	public long getVibratorMorseLength() {
		return getPreferences().getLong(VIBRATOR_MORSE_LENGTH_MS,200l);
	}
	public void setVibratorPauseLength(long ms) {
		getPreferences().edit().putLong(VIBRATOR_PAUSE_LENGTH_MS,ms).commit();
	}
	public long getVibratorPauseLength() {
		return getPreferences().getLong(VIBRATOR_PAUSE_LENGTH_MS,200l);
	}

	public void setTorchMorseLength(long ms) {
		getPreferences().edit().putLong(TORCH_MORSE_LENGTH_MS,ms).commit();
	}
	public long getTorchMorseLength() {
		return getPreferences().getLong(TORCH_MORSE_LENGTH_MS,200l);
	}
	public void setTorchPauseLength(long ms) {
		getPreferences().edit().putLong(TORCH_PAUSE_LENGTH_MS,ms).commit();
	}
	public long getTorchPauseLength() {
		return getPreferences().getLong(TORCH_PAUSE_LENGTH_MS,200l);
	}


	public void setSmsTranslator(SmsTranslator translator) {
		getPreferences().edit().putString(TORCH_PAUSE_LENGTH_MS,translator.toString()).commit();
	}
	public SmsTranslator getSmsTranslator() {
		return SmsTranslator.valueOf(getPreferences().getString(TORCH_PAUSE_LENGTH_MS,SmsTranslator.VIBRATOR.toString()));
	}
	private SharedPreferences getPreferences() {
		if (prefs == null) {
			prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		}
		return prefs;
	}

}
