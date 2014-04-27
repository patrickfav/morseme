package at.favre.app.morseme.morse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PatrickF on 26.04.2014.
 */
public class MorseUtil {

	public static List<EMorsePart> generateMorseSequenze(String content) {
		List<EMorsePart> sequenze = new ArrayList<EMorsePart>();
		String contentUppercase = content.toUpperCase();
		for (int i = 0; i < content.length(); i++) {
			EMorseSymbol symbol = EMorseSymbol.getBySymbol(contentUppercase.charAt(i));
			if (symbol != null) {
				sequenze.addAll(symbol.getPartsWithPauses());
				sequenze.add(EMorsePart.LETTER_SPACE);
			}
		}

		return sequenze;
	}

	public static long getAtomicMorseLength() {
		return 75l;
	}
}