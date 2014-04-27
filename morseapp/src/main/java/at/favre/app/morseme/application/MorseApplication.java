package at.favre.app.morseme.application;

import android.app.Application;

/**
 * Created by PatrickF on 27.04.2014.
 */
public class MorseApplication extends Application {

	private PreferenceHandler preferenceHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		preferenceHandler = new PreferenceHandler(this);
	}

	public PreferenceHandler getPreferenceHandler() {
		return preferenceHandler;
	}
}
