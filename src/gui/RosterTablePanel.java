package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.CollectionChangedEvent;
import model.CollectionChangedListeners;
import model.Player;
import model.PlayerEvaluator;
import model.PropertyChangedEvent;
import model.PropertyChangedListener;
import model.Roster;
import model.comparators.EvaluatorComparator;
import model.comparators.QualityEvaluatorComparator;
import model.comparators.RatingEvaluatorComparator;

public class RosterTablePanel extends JPanel
{
	private static final long serialVersionUID = 6702252304393306453L;

	private PlayerSelectedListener playerSelectedListener;

	private List<PlayerEvaluator> evaluators =
			new LinkedList<PlayerEvaluator>();

	private ColumnData[] columnDatas =
	{
			new ColumnData("Name", p -> p.getName()),
			new ColumnData("Side", p -> p.getSide()),
			new ColumnData("Goa", p -> p.getAttributes().getGoa()),
			new ColumnData("FiP", p -> p.getAttributes().getFip()),
			new ColumnData("Sho", p -> p.getAttributes().getSho()),
			new ColumnData("Blk", p -> p.getAttributes().getBlk()),
			new ColumnData("Pas", p -> p.getAttributes().getPas()),
			new ColumnData("Tec", p -> p.getAttributes().getTec()),
			new ColumnData("Spe", p -> p.getAttributes().getSpe()),
			new ColumnData("Agr", p -> p.getAttributes().getAgr()),
			new ColumnData("Total", p -> p.getAttributes().getTotalRating()),
			new ColumnData(
					"Position",
					(p) -> getPlayersBestPositionName(
						new RatingEvaluatorComparator(p.getAttributes()),
						evaluators)),
			new ColumnData(
					"Highest Rating",
					(p) -> getPlayersBestPositionRating(evaluators, p),
					(v) -> String.format("%.1f", v)),
			new ColumnData(
					"Training",
					(p) -> getPlayersBestPositionName(
						new QualityEvaluatorComparator(p.getAttributes()),
						evaluators)),
			new ColumnData(
					"Highest Quality",
					(p) -> getPlayersBestPositionQuality(evaluators, p),
					(v) -> String.format("%.1f", v)),
	};

	private RosterTableModel rosterTableModel;

	private JTable rosterTable;

	private Roster roster;

	public RosterTablePanel()
	{
		roster = new Roster();

		rosterTableModel = new RosterTableModel();

		rosterTable = new JTable();
		rosterTable.setAutoCreateRowSorter(true);
		rosterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rosterTable.getSelectionModel().addListSelectionListener(
			new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					if (e.getValueIsAdjusting()) return;

					if (playerSelectedListener != null)
					{
						playerSelectedListener.playerSelected(
							this,
							new PlayerSelectedEvent(this, getSelectedPlayer()));
					}
				}

				private Player getSelectedPlayer()
				{
					int selectedRow = rosterTable.getSelectedRow();

					return selectedRow >= 0
							? roster.get(
								rosterTable.convertRowIndexToModel(selectedRow))
							: null;
				}
			});
		rosterTable.setColumnModel(new RosterTableColumnModel());
		rosterTable.setModel(rosterTableModel);

		setBorder(new CompoundBorder(
				BorderFactory.createTitledBorder("Roster"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		setLayout(new BorderLayout());

		add(rosterTable.getTableHeader(), BorderLayout.PAGE_START);
		add(new JScrollPane(rosterTable));
	}

	public void bind(Roster roster)
	{
		if (this.roster != null)
		{
			for (Player player : this.roster)
			{
				player.removePropertyChangedListener(rosterTableModel);
			}

			this.roster.removeCollectionChangedListener(rosterTableModel);
		}

		this.roster = roster;

		if (this.roster != null)
		{
			for (Player player : this.roster)
			{
				player.addPropertyChangedListener(rosterTableModel);
			}

			this.roster.addCollectionChangedListener(rosterTableModel);
		}

		rosterTableModel.fireTableDataChanged();
	}

	public void setPlayerSelectedListener(PlayerSelectedListener listener)
	{
		this.playerSelectedListener = listener;
	}

	public void setPlayerEvaluators(List<PlayerEvaluator> evaluators)
	{
		this.evaluators = evaluators;

		rosterTableModel.fireAllTableCellsUpdated();
	}

	private String getPlayersBestPositionName(
			EvaluatorComparator evaluatorComparator,
			List<PlayerEvaluator> evaluators)
	{
		PlayerEvaluator playerEvaluator = evaluators
				.stream()
				.max((a, b) -> evaluatorComparator.compare(a, b))
				.get();

		return playerEvaluator != null
				? playerEvaluator.getName()
				: "No suggestion";
	}

	private Double getPlayersBestPositionRating(
			List<PlayerEvaluator> evaluators,
			Player player)
	{
		return evaluators
				.stream()
				.max((a, b) -> new RatingEvaluatorComparator(
						player.getAttributes()).compare(a, b))
				.map(e -> e.getRating(player.getAttributes()))
				.get();
	}

	private Double getPlayersBestPositionQuality(
			List<PlayerEvaluator> evaluators,
			Player player)
	{
		return evaluators
				.stream()
				.max((a, b) -> new QualityEvaluatorComparator(
						player.getAttributes()).compare(a, b))
				.map(e -> e.getQuality(player.getAttributes()))
				.get();
	}

	private class ColumnData
	{
		private String name;
		private Function<Player, Object> getValueFunction;
		private Function<Object, String> formatValueFunction;

		public ColumnData(
				String name,
				Function<Player, Object> getValueAction,
				Function<Object, String> formatValueFunction)
		{
			this.name = name;
			this.getValueFunction = getValueAction;
			this.formatValueFunction = formatValueFunction;
		}

		public ColumnData(String name, Function<Player, Object> getValueAction)
		{
			this.name = name;
			this.getValueFunction = getValueAction;
			this.formatValueFunction = (v) -> v.toString();
		}

		public String getName()
		{
			return name;
		}

		public Object getValue(Player player)
		{
			return getValueFunction.apply(player);
		}

		public String formatValue(Object value)
		{
			return formatValueFunction.apply(value);
		}
	}

	private class RosterTableModel extends DefaultTableModel
			implements
			CollectionChangedListeners,
			PropertyChangedListener
	{
		private static final long serialVersionUID = -3862251740620048034L;

		@Override
		public String getColumnName(int column)
		{
			return columnDatas[column].getName();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			return roster != null && roster.size() > 0
					? getValueAt(0, columnIndex).getClass()
					: Object.class;
		}

		@Override
		public Object getValueAt(int row, int column)
		{
			return columnDatas[column].getValue(roster.get(row));
		}

		@Override
		public int getRowCount()
		{
			return roster != null ? roster.size() : 0;
		}

		@Override
		public int getColumnCount()
		{
			return columnDatas.length;
		}

		@Override
		public void collectionChanged(
				Object source,
				CollectionChangedEvent event)
		{
			Player player = (Player) event.getObjectChanged();

			switch (event.getAction())
			{
				case CollectionChangedEvent.ADDED:
					player.addPropertyChangedListener(this);

					fireTableRowsInserted(
						event.getIndexChanged(),
						event.getIndexChanged());

					break;
				case CollectionChangedEvent.REMOVED:
					player.removePropertyChangedListener(this);

					fireTableRowsDeleted(
						event.getIndexChanged(),
						event.getIndexChanged());

					break;
			}
		}

		@Override
		public void propertyChanged(Object source, PropertyChangedEvent event)
		{
			Player player = (Player) source;

			fireTableRowsUpdated(
				roster.indexOf(player),
				roster.indexOf(player));
		}

		public void fireAllTableCellsUpdated()
		{
			for (int row = 0; row < getRowCount(); row++)
			{
				for (int column = 0; column < getColumnCount(); column++)
				{
					fireTableCellUpdated(row, column);
				}
			}
		}
	}

	private class RosterTableColumnModel extends DefaultTableColumnModel
	{
		private static final long serialVersionUID = -318707452817342668L;

		private final Color SELECTED_HIGH_QUALITY_AND_SYMMETRIC =
				new Color(0, 255, 0);
		private final Color UNSELECTED_HIGH_QUALITY_AND_SYMMETRIC =
				new Color(153, 255, 153);
		private final Color SELECTED_HIGH_QUALITY =
				new Color(255, 255, 0);
		private final Color UNSELECTED_HIGH_QUALITY =
				new Color(255, 255, 153);

		private final int BEST_POSITION_QUALITY_LIMIT = 70;

		@Override
		public TableColumn getColumn(int columnIndex)
		{
			TableColumn column = super.getColumn(columnIndex);

			column.setCellRenderer(new DefaultTableCellRenderer()
			{
				private static final long serialVersionUID =
						-871237359467912116L;

				@Override
				public Component getTableCellRendererComponent(
						JTable table,
						Object value,
						boolean isSelected,
						boolean hasFocus,
						int row,
						int column)
				{
					value = columnDatas[columnIndex].formatValue(value);

					Component component =
							super.getTableCellRendererComponent(
								table,
								value,
								isSelected,
								hasFocus,
								row,
								column);

					Player player = roster
							.get(table.convertRowIndexToModel(row));

					Color color = Color.WHITE;

					if (isHighQualityPlayer(player)
							&& isSymmetricPlayer(player))
					{
						color = isSelected
								? SELECTED_HIGH_QUALITY_AND_SYMMETRIC
								: UNSELECTED_HIGH_QUALITY_AND_SYMMETRIC;
					}
					else if (isHighQualityPlayer(player))
					{
						color = isSelected
								? SELECTED_HIGH_QUALITY
								: UNSELECTED_HIGH_QUALITY;
					}
					else if (isSelected)
					{
						color = table.getSelectionBackground();
					}

					component.setBackground(color);

					return component;
				}

				private boolean isHighQualityPlayer(Player player)
				{
					Double bestPositionQuality =
							getPlayersBestPositionQuality(
								evaluators,
								player);

					return bestPositionQuality > BEST_POSITION_QUALITY_LIMIT;
				}

				private boolean isSymmetricPlayer(Player player)
				{
					String bestRatingPosition =
							getPlayersBestPositionName(
								new RatingEvaluatorComparator(
										player.getAttributes()),
								evaluators);

					String bestQualityPosition =
							getPlayersBestPositionName(
								new QualityEvaluatorComparator(
										player.getAttributes()),
								evaluators);

					return bestRatingPosition.equals(bestQualityPosition);
				}
			});

			return column;
		}
	}
}
