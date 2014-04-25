package at.favre.app.morseme.morse;

/**
 * Created by PatrickF on 26.04.2014.
 */
public enum EMorsePart {
	SHORT(1), LONG(3), PAUSE(1), SPACE(3), WORDEND(7);

	private final int relativeLength;

	EMorsePart(int relativeLength) {
		this.relativeLength = relativeLength;
	}

	public int getRelativeLength() {
		return relativeLength;
	}
}
