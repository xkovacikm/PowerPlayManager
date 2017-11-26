package gui.util;

import java.util.List;

import javax.swing.JComboBox;

public class PpmComboBox<E>
	extends JComboBox<E>
{
	private static final long serialVersionUID = -5722936153488559656L;

	public PpmComboBox()
	{
		super();
	}

	public PpmComboBox(List<E> items)
	{
		items.forEach(this::addItem);
	}

	public E getSelection()
	{
		return getItemAt(getSelectedIndex());
	}
}