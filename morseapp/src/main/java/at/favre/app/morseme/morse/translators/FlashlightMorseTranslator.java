package at.favre.app.morseme.morse.translators;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import at.favre.app.morseme.morse.EMorsePart;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class FlashlightMorseTranslator extends ATranslator{
	private Camera camera;
	private Camera.Parameters parameters;
	private boolean isFlashOn;

	public FlashlightMorseTranslator(List<EMorsePart> sequenze, ITranslatorListener listener, long morseLengthMs, long pauseLengthMs) {
		super(sequenze, listener, morseLengthMs, pauseLengthMs);
	}


	@Override
	protected Void doInBackground(Void... voids) {
		initializeFlashlight();
		play();
		closeFlashlight();
		return null;
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

	@Override
	protected void startTranslator(long length) {
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
				Log.e(TAG,"Could not set camera preview",e);
			}
			camera.startPreview();
			isFlashOn = true;
		}
	}

	@Override
	protected void stopTranslator() {
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
