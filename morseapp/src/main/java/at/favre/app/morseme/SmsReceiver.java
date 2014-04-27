package at.favre.app.morseme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;

import at.favre.app.morseme.morse.MorseUtil;
import at.favre.app.morseme.morse.translators.TranslatorListener;
import at.favre.app.morseme.morse.translators.VibrateMorseTranslator;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = SmsReceiver.class.getSimpleName();
	private static final String MORSE_PREFIX = "morse:";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Intent recieved: " + intent.getAction());

		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[])bundle.get("pdus");
				final SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				}
				if (messages.length > -1) {
					for (SmsMessage message : messages) {
						if(!message.getMessageBody().isEmpty() && message.getMessageBody().startsWith(MORSE_PREFIX)) {
							String msg = message.getMessageBody().replaceFirst(MORSE_PREFIX, "");
							VibrateMorseTranslator translator = new VibrateMorseTranslator(MorseUtil.generateMorseSequenze(msg),(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE), new TranslatorListener() {
								@Override
								public void onMorseComplete(boolean canceld) {

								}
							});
							translator.execute();
						}

					}

				}
			}
		}
	}
}
