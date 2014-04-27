package at.favre.app.morseme.morse.translators;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;
import at.favre.app.morseme.morse.MorseUtil;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class FlashlightMorseTranslator extends AsyncTask<Void,Void,Void>{

	private static final String TAG = FlashlightMorseTranslator.class.getSimpleName();
	private List<EMorsePart> sequenze;
	private Camera camera;
	private Camera.Parameters parameters;
	private TranslatorListener listener;
	private boolean run;
	private boolean isFlashOn;

	public FlashlightMorseTranslator(List<EMorsePart> sequenze,TranslatorListener listener) {
		this.listener = listener;
		this.sequenze = sequenze;
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
		initializeFlashlight();
		for (EMorsePart eMorsePart : sequenze) {
			if(run) {
				long lengthMs = MorseUtil.getAtomicMorseLength()*eMorsePart.getRelativeLength();
				if (eMorsePart.isPause()) {
					SystemClock.sleep(lengthMs);
				} else {
					turnOnFlash();
					SystemClock.sleep(lengthMs);
					turnOffFlash();
				}
			} else {
				break;
			}
		}
		closeFlashlight();
		run=false;
		Log.d(TAG,"done morse");
	}


	private void initializeFlashlight() {
		if (camera == null) {
			try {
				camera = Camera.open();
				parameters = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	/*
	 * Turning On flash
	 */
	private void turnOnFlash() {
		if (!isFlashOn) {
			if (camera == null || parameters == null) {
				return;
			}

			parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
			try {
				camera.setPreviewTexture(new SurfaceTexture(0));
			} catch (IOException e) {
				Log.e(TAG,"Could not set camer preview",e);
			}
			camera.startPreview();
			isFlashOn = true;
		}
	}
	/*
	 * Turning Off flash
	 */
	private void turnOffFlash() {
		if (isFlashOn) {
			if (camera == null || parameters == null) {
				return;
			}
			parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			camera.stopPreview();
			isFlashOn = false;
		}
	}
	private void closeFlashlight() {
		if (camera != null) {
			camera.release();
		}
	}
}
