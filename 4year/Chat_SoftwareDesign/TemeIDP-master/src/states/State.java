package states;

import core.Mediator;
/**
 * 
// * Send interface implemenetd by Receive and SendState
 *
 */
public abstract class State {
	protected Mediator med;
	/**
	 * 0 - Downloading;
	 * 1 - Uploading
	 */
	protected int type;
	State(Mediator med){
		this.med = med;
	}
	
	public abstract void updateTransferSelectedUser(String user);
	public int getType()
	{
		return type;
	}
	
	
}