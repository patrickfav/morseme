package at.favre.app.morseme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;

import at.favre.app.morseme.application.MorseApplication;
import at.favre.app.morseme.application.PreferenceHandler;
import at.favre.app.morseme.morse.MorseUtil;
import at.favre.app.morseme.morse.translators.ITranslatorListener;
import at.favre.app.morseme.morse.translators.VibrateMorseTranslator;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = SmsReceiver.class.getSimpleName();
	public static final String MORSE_PREFIX = "morse:";

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i(TAG, "Intent recieved: " + intent.getAction());
		final PreferenceHandler pref = ((MorseApplication) context.getApplicationContext()).getPreferenceHandler();
		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[])bundle.get("pdus");
				final SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				}

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (messages.length > -1) {
							for (SmsMessage message : messages) {
								if(!message.getMessageBody().isEmpty() && message.getMessageBody().startsWith(MORSE_PREFIX)) {
									String msg = message.getMessageBody().replaceFirst(MORSE_PREFIX, "");
									VibrateMorseTranslator translator = new VibrateMorseTranslator(MorseUtil.generateMorseSequenze(msg), new ITranslatorListener() {
										@Override
										public void onMorseComplete(boolean canceld) {

										}

                                        @Override
                                        public void currentPlayedLetter(int letterIndex) {

                                        }
                                    },pref.getVibratorMorseLength(),pref.getVibratorPauseLength(),(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
									translator.execute();
								}

							}

						}
					}
				},1000);


			}
		}
	}
}
