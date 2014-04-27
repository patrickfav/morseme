package at.favre.app.morseme.morse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PatrickF on 26.04.2014.
 */
public enum EMorseSymbol {

	A(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG}, 'A'),
	B(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT}, 'B'),
	C(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.SHORT}, 'C'),

	D(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT}, 'D'),
	E(new EMorsePart[]{EMorsePart.SHORT}, 'E'),
	F(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.SHORT}, 'F'),

	G(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT}, 'G'),
	H(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT}, 'H'),
	I(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT}, 'I'),

	J(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG}, 'J'),
	K(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.LONG}, 'K'),
	L(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT}, 'L'),

	M(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG}, 'M'),
	N(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT}, 'N'),
	O(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG}, 'O'),

	P(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT}, 'P'),
	Q(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.LONG}, 'Q'),
	R(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.SHORT}, 'R'),

	S(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT}, 'S'),
	T(new EMorsePart[]{EMorsePart.LONG}, 'T'),
	U(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG}, 'U'),

	V(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG}, 'V'),
	W(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG}, 'W'),

	X(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG}, 'X'),
	Y(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG}, 'Y'),
	Z(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT}, 'Z'),

	DIGIT_1(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG}, '1'),
	DIGIT_2(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG}, '2'),
	DIGIT_3(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG, EMorsePart.LONG}, '3'),
	DIGIT_4(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.LONG}, '4'),
	DIGIT_5(new EMorsePart[]{EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT}, '5'),
	DIGIT_6(new EMorsePart[]{EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT}, '6'),
	DIGIT_7(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT, EMorsePart.SHORT}, '7'),
	DIGIT_8(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT, EMorsePart.SHORT}, '8'),
	DIGIT_9(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.SHORT}, '9'),
	DIGIT_0(new EMorsePart[]{EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG, EMorsePart.LONG}, '0');


	private List<EMorsePart> parts;
	private char symbol;

	private EMorseSymbol(EMorsePart[] parts, char symbol) {
		this.parts = Arrays.asList(parts);
		this.symbol = symbol;
	}

	public List<EMorsePart> getParts() {
		return parts;
	}

	public List<EMorsePart> getPartsWithPauses() {
		List<EMorsePart> partsWithPauses = new ArrayList<EMorsePart>();
		for (EMorsePart part : parts) {
			partsWithPauses.add(part);
			partsWithPauses.add(EMorsePart.INTER_LETTER_GAP);
		}
		if (!partsWithPauses.isEmpty()) {
			return partsWithPauses.subList(0, partsWithPauses.size() - 1);
		} else {
			return partsWithPauses;
		}

	}

	public char getSymbol() {
		return symbol;
	}

	public static EMorseSymbol getBySymbol(char symbol) {
		for (EMorseSymbol eMorseSymbol : values()) {
			if (symbol == eMorseSymbol.symbol) {
				return eMorseSymbol;
			}
		}

		return null;
	}
}
