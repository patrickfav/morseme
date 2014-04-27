package at.favre.app.morseme.morse;

/**
 * Created by PatrickF on 26.04.2014.
 *
 * International Morse code is composed of five elements:
 *
 * short mark, dot or "dit" (·) — "dot duration" is one time unit long
 * longer mark, dash or "dah" (–) — three time units long
 * inter-element gap between the dots and dashes within a character — one dot duration or one unit long
 * short gap (between letters) — three time units long
 * medium gap (between words) — seven time units long[1]
 *
 */
public enum EMorsePart {
	SHORT(1, false), LONG(3, false), INTER_LETTER_GAP(1, true), LETTER_SPACE(3, true), WORD_SPACE(7, true);

	private final int relativeLength;
	private final boolean isPause;

	EMorsePart(int relativeLength, boolean isPause) {
		this.relativeLength = relativeLength;
		this.isPause = isPause;
	}

	public int getRelativeLength() {
		return relativeLength;
	}

	public boolean isPause() {
		return isPause;
	}
}
