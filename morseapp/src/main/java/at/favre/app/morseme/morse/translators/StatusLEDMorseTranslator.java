package at.favre.app.morseme.morse.translators;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;
import at.favre.app.morseme.morse.MorseUtil;

/**
 * Created by PatrickF on 26.04.2014.
 */
@Deprecated
public class StatusLEDMorseTranslator extends AsyncTask<Void,Void,Void>{

	private static final String TAG = StatusLEDMorseTranslator.class.getSimpleName();
	private List<EMorsePart> sequenze;
	private NotificationManager notifManager;
	private boolean run;
	private boolean isFlashOn;

	public StatusLEDMorseTranslator(List<EMorsePart> sequenze, NotificationManager notifManager) {
		this.sequenze = sequenze;
		this.notifManager = notifManager;
		run= false;
	}

	@Override
	protected Void doInBackground(Void... voids) {
		startLED(1500);

		play();
		return null;
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
					SystemClock.sleep(lengthMs);
					stopLED();
				}
			} else {
				break;
			}
		}
		run=false;
		Log.d(TAG,"done morse");
	}

	private void startLED(long dur) {
		Notification notif = new Notification();
		Notification.Builder builder = new Notification.Builder(null);
		//Use this if you want to use the default lights for a notification
		notif.defaults |= Notification.DEFAULT_LIGHTS;
		notif.ledOnMS = (int) dur;                 // LED's on for 300 ms
		notif.ledOffMS = 100000;               // LEDs off for 1 second
		notif.flags |= Notification.FLAG_SHOW_LIGHTS;

		notifManager.notify(1234, notif);
	}

	private void stopLED() {
		//notifManager.cancelAll();
	}


}
