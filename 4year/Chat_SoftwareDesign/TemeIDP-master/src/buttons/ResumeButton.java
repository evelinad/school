package buttons;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import core.Command;
import core.Mediator;

/*
 * class for resuimg a paused tranfsre
 */

public class ResumeButton extends JButton implements Command {
	Mediator med;

	public ResumeButton() {
	}

	public ResumeButton(String text, ActionListener act, Mediator md,
			ImageIcon icon) {
		super(text);
		addActionListener(act);
		med = md;
		setIcon(icon);
	}

	public ResumeButton(Icon icon) {
		super(icon);
	}

	public ResumeButton(String text) {
		super(text);
	}

	public ResumeButton(Action a) {
		super(a);
	}

	public ResumeButton(String text, Icon icon) {
		super(text, icon);
	}

	@Override
	public void execute() {
		med.resumeSelectedTransfer();
	}

}
