package radiobuttons;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

import core.Command;
import core.Mediator;
/**
 * 
 * SendRadioButton class for setting a send transfer
 */
public class SendRadioButton extends JRadioButton implements Command{
	Mediator med;

	public SendRadioButton(String text,ActionListener act, Mediator md) {
		super(text);
		addActionListener(act);
		med = md;
	}
	public SendRadioButton() {
	}

	public SendRadioButton(Icon icon) {
		super(icon);
	}

	public SendRadioButton(Action a) {
		super(a);
	}

	public SendRadioButton(String text) {
		super(text);
	}

	public SendRadioButton(Icon icon, boolean selected) {
		super(icon, selected);
	}

	public SendRadioButton(String text, boolean selected) {
		super(text, selected);
	}

	public SendRadioButton(String text, Icon icon) {
		super(text, icon);
	}

	public SendRadioButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
	}
	/**
	 * setup the send transfer
	 */
	@Override
	public void execute() {
		med.initiateSendFile();
		
	}

}
