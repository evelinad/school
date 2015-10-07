package states;

import core.Mediator;

/**
 * 
 * After checking the ReceiveRadioButton, the application 
 * goes into receive state until the new transfer is added
 *
 */
public class ReceiveState extends State{

	public ReceiveState(Mediator med) {
		super(med);
		type = 0;
	}
	
	public void updateTransferSelectedUser(String user)
	{
		med.addFilesToModel(user);
		med.setFromValue(user);
		med.setToValue(med.getCurrentUser());
		
	}
}
