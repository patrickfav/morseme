package at.favre.app.morseme.morse;

import android.os.Parcel;
import android.os.Parcelable;

import at.favre.app.morseme.morse.translators.EMorseTranslator;

/**
 * Created by PatrickF on 27.04.2014.
 */
public class MorseConfig implements Parcelable {
	private long morseLengthMs;
	private long pauseLengthMs;

	public MorseConfig(EMorseTranslator translator, long morseLengthMs, long pauseLengthMs) {
		this.morseLengthMs = morseLengthMs;
		this.pauseLengthMs = pauseLengthMs;
	}

	public long getMorseLengthMs() {
		return morseLengthMs;
	}

	public void setMorseLengthMs(long morseLengthMs) {
		this.morseLengthMs = morseLengthMs;
	}

	public long getPauseLengthMs() {
		return pauseLengthMs;
	}

	public void setPauseLengthMs(long pauseLengthMs) {
		this.pauseLengthMs = pauseLengthMs;
	}

	protected MorseConfig(Parcel in) {
		morseLengthMs = in.readLong();
		pauseLengthMs = in.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(morseLengthMs);
		dest.writeLong(pauseLengthMs);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<MorseConfig> CREATOR = new Parcelable.Creator<MorseConfig>() {
		@Override
		public MorseConfig createFromParcel(Parcel in) {
			return new MorseConfig(in);
		}

		@Override
		public MorseConfig[] newArray(int size) {
			return new MorseConfig[size];
		}
	};
}