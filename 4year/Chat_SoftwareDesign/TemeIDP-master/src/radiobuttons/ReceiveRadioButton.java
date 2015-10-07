package radiobuttons;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

import core.Command;
import core.Mediator;

/**
 * 
 * Receive RadioButton class for setting a receive transfer
 *
 */
public class ReceiveRadioButton extends JRadioButton implements Command{
	Mediator med;

	public ReceiveRadioButton(String text,ActionListener act, Mediator md) {
		super(text);
		addActionListener(act);
		med = md;
	}
	public ReceiveRadioButton() {
	}
	

	public ReceiveRadioButton(Icon icon) {
		super(icon);
	}

	public ReceiveRadioButton(Action a) {
		super(a);
	}

	public ReceiveRadioButton(String text) {
		super(text);
	}

	public ReceiveRadioButton(Icon icon, boolean selected) {
		super(icon, selected);
	}

	public ReceiveRadioButton(String text, boolean selected) {
		super(text, selected);
	}

	public ReceiveRadioButton(String text, Icon icon) {
		super(text, icon);
	}

	public ReceiveRadioButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
	}
	/**
	 * setup the receive transfer
	 */
	@Override
	public void execute() {
		med.initiateReceiveFile();
		
	}

}
