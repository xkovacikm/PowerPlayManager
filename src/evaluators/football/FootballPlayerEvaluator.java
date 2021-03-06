package evaluators.football;

import java.util.List;

import evaluators.AttributeEvaluator;
import evaluators.FacilityEvaluator;
import evaluators.PlayerEvaluator;
import model.football.FootballAttributes;
import settings.Settings;

public class FootballPlayerEvaluator
	extends PlayerEvaluator<FootballAttributes>
{
	private static final double A = -0.001801, B = 0.01567, C = 1.351;

	public FootballPlayerEvaluator(
			Settings settings,
			FacilityEvaluator facilityEvaluator,
			List<AttributeEvaluator<FootballAttributes>> attributeEvaluators)
	{
		super(A, B, C, settings, facilityEvaluator, attributeEvaluators);
	}
}
