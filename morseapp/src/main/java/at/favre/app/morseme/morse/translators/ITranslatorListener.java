package at.favre.app.morseme.morse.translators;

/**
 * Created by PatrickF on 27.04.2014.
 */
public interface ITranslatorListener {
	public void onMorseComplete(boolean canceld);
    public void currentPlayedLetter(int letterIndex);
}
