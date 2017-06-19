package model.evaluators;

import model.PlayerEvaluator;

public class DefensivePivotPlayerEvaluator extends PlayerEvaluator
{
	private static final String NAME = "Def. Pivot";

	private static final int GOA = 0;
	private static final int FIP = 100;
	private static final int SHO = 40;
	private static final int BLK = 80;
	private static final int PAS = 40;
	private static final int TEC = 60;
	private static final int SPE = 40;
	private static final int AGR = 80;

	public DefensivePivotPlayerEvaluator()
	{
		super(NAME, GOA, FIP, SHO, BLK, PAS, TEC, SPE, AGR);
	}
}
