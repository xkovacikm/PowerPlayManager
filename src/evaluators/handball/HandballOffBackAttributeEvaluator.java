package evaluators.handball;

public class HandballOffBackAttributeEvaluator extends HandballAttributeEvaluator
{
	private static final String NAME = "Off. Back";

	private static final int GOA = 0;
	private static final int FIP = 100;
	private static final int SHO = 100;
	private static final int BLK = 0;
	private static final int PAS = 80;
	private static final int TEC = 60;
	private static final int SPE = 40;
	private static final int AGR = 40;

	public HandballOffBackAttributeEvaluator()
	{
		super(NAME, GOA, FIP, SHO, BLK, PAS, TEC, SPE, AGR);
	}
}