package evaluators.icehockey;

import java.util.List;

import evaluators.AttributeEvaluator;
import evaluators.FacilityEvaluator;
import evaluators.PlayerEvaluator;
import model.icehockey.IceHockeyAttributes;

public class IceHockeyPlayerEvaluator extends PlayerEvaluator<IceHockeyAttributes>
{
	private static final double A = -0.001801, B = 0.01567, C = 1.351;
	private static final double AGE_15_RATING = 190;

	public IceHockeyPlayerEvaluator(
			List<AttributeEvaluator<IceHockeyAttributes>> attributeEvaluators,
			FacilityEvaluator facilityEvaluator)
	{
		super(A, B, C, AGE_15_RATING, attributeEvaluators, facilityEvaluator);
	}
}
