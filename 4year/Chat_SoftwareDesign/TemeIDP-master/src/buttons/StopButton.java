package buttons;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import core.Command;
import core.Mediator;
/**
 * 
 * @author Evelina
 * class for stopping a transfer
 *
 */
public class StopButton extends JButton implements Command {
	Mediator med;
	public StopButton() {
	}

	public StopButton(String text,ActionListener act, Mediator md,ImageIcon icon) {
		super(text);
		addActionListener(act);
		med = md;
		setIcon(icon);
	}
	public StopButton(Icon icon) {
		super(icon);
	}

	public StopButton(String text) {
		super(text);
	}

	public StopButton(Action a) {
		super(a);
	}

	public StopButton(String text, Icon icon) {
		super(text, icon);
	}

	@Override
	public void execute() {
		med.stopSelectedTransfer();
	}

}
