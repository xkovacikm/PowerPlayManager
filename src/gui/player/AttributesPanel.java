package gui.player;

import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;

import gui.util.SimpleFormPanel;
import model.Attributes;
import util.PropertyChangedEvent;
import util.PropertyChangedListener;

public abstract class AttributesPanel<A extends Attributes>
	extends SimpleFormPanel
	implements
		PropertyChangedListener
{
	private static final long serialVersionUID = -7993333522332535462L;

	protected static final int TEXTFIELD_COLUMNS = 6;

	protected A attributes;

	protected AttributesPanel()
	{
		setBorder(
			new CompoundBorder(
					BorderFactory.createTitledBorder("Attributes"),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	public void bind(A attributes)
	{
		if (this.attributes != null)
		{
			this.attributes.removePropertyChangedListener(this);
		}

		this.attributes = attributes;

		if (this.attributes != null)
		{
			this.attributes.addPropertyChangedListener(this);
		}

		update();
	}

	@Override
	public void propertyChanged(Object source, PropertyChangedEvent event)
	{
		update();
	}

	protected String intToString(Supplier<Integer> getValueSupplier)
	{
		return attributes != null
				? Integer.toString(getValueSupplier.get())
				: "";
	}

	protected String doubleToString(Supplier<Double> getValueSupplier)
	{
		return attributes != null
				? String.format("%.1f", getValueSupplier.get())
				: "";
	}

	protected abstract void update();
}
