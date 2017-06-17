package gui;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

import model.Player;
import model.PlayerEvaluator;

public abstract class SuggestionPanel extends JPanel
{
	private static final long serialVersionUID = 33853554129162390L;

	private static final int DISPLAYED_POSITIONS_LIMIT = 3;

	public SuggestionPanel(String title)
	{
		setBorder(new CompoundBorder(
				BorderFactory.createTitledBorder(title + " Suggestions"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		setLayout(new GridLayout(DISPLAYED_POSITIONS_LIMIT, 2));
	}

	public void showSuggestions(Player player, PlayerEvaluator... evaluators)
	{
		clear();

		for (PlayerEvaluator evaluator : Arrays
				.stream(evaluators)
				.sorted((a, b) -> Double.compare(
					getValue(player, b),
					getValue(player, a)))
				.limit(DISPLAYED_POSITIONS_LIMIT)
				.toArray(PlayerEvaluator[]::new))
		{
			add(new JLabel(evaluator.getName()));
			add(new JLabel(
					String.format(
						"%.1f",
						getValue(player, evaluator))));
		}

		updateUI();
	}

	protected abstract double getValue(
			Player player,
			PlayerEvaluator evaluator);

	public void clear()
	{
		removeAll();
		updateUI();
	}
}