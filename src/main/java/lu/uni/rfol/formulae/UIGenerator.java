package lu.uni.rfol.formulae;

public final class UIGenerator {
	public static int UI = 0;

	/**
	 * returns the uniqueIdentifier of the formula
	 * 
	 * @return the unique Identifier of the formula
	 */
	public static int generateUI() {
		UI = UI + 1;
		return UI - 1;
	}
}
