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
 * Class for adding anew transfer
 * 
 */
public class AddButton extends JButton implements Command {
	Mediator med;

	public AddButton() {
	}

	public AddButton(String text, ActionListener act, Mediator md,
			ImageIcon icon) {
		super(text);
		addActionListener(act);
		med = md;
		setIcon(icon);
	}

	public AddButton(Icon icon) {
		super(icon);
	}

	public AddButton(String text) {
		super(text);
	}

	public AddButton(Action a) {
		super(a);
	}

	public AddButton(String text, Icon icon) {
		super(text, icon);
	}

	@Override
	public void execute() {
		med.doTransfer();

	}

}
