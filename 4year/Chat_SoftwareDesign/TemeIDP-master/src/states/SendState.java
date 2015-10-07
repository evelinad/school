package states;

import core.Mediator;

/**
 * 
 * After checking the SendRadioButton, the application 
 * goes into send state until the new transfer is added
 *
 */
public class SendState extends State{

	public SendState(Mediator med) {
		super(med);
		type = 1;
	}

	@Override
	public void updateTransferSelectedUser(String user) {
		med.setFromValue(med.getCurrentUser());
		med.setToValue(user);
		
	}

}
