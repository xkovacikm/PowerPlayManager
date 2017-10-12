package gui.icehockey;

import java.util.List;

import builders.formation.icehockey.IceHockeyFormationTemplate;
import builders.formation.icehockey.IceHockeyPaulsFormationBuilder;
import evaluators.AttributeEvaluator;
import evaluators.FacilityEvaluator;
import evaluators.icehockey.IceHockeyPlayerEvaluator;
import gui.MainPanel;
import model.icehockey.IceHockeyAttributes;
import model.icehockey.IceHockeyFormation;
import parsers.players.PlayersParser;

public class IceHockeyMainPanel
	extends MainPanel<IceHockeyAttributes, IceHockeyFormation, IceHockeyFormationTemplate>
{
	private static final long serialVersionUID = -9170227741926378853L;

	public IceHockeyMainPanel(
			FacilityEvaluator facilityEvaluator,
			List<AttributeEvaluator<IceHockeyAttributes>> attributeEvaluators,
			List<PlayersParser<IceHockeyAttributes>> parsers)
	{
		super(
				new IceHockeyAttributesPanel(),
				new IceHockeyFormationTemplatePanelFactory(),
				new IceHockeyFormationPanelFactory(),
				new IceHockeyPaulsFormationBuilder(),
				new IceHockeyPlayerEvaluator(attributeEvaluators, facilityEvaluator),
				attributeEvaluators,
				null,
				parsers);
	}
}
