package states;

import core.Mediator;
/**
 * 
 * @author Evelina
 * class that keeps reference to current transfer state choice
 *
 */
public class StateManager {
	private State currentState;
	private ReceiveState receiveState;
	private SendState sendState;
	private String toUser;
	private String fromUser;
	private String file;
	private Mediator med;
	private String currentUser;
	
	public StateManager(Mediator med) {
		this.med = med;
		receiveState = new ReceiveState(med);
		sendState = new SendState(med);
	}
	public void setToValue(String to)
	{
		this.toUser = to;
	
	}
	
	public void setCurrentUser(String user)
	{
		this.currentUser = user;
	
	}	
	public String getCurrentUser()
	{
		return this.currentUser;
	}
	public void setFromValue(String from)
	{
		this.fromUser=from;
	}
	
	public void setFileValue(String file)
	{
		this.file=file;
	}

	public String getToValue()
	{
		return this.toUser;
	
	}
	public String getFromValue()
	{
		return this.fromUser;
	}
	
	public String getFileValue()
	{
		return  this.file;
	}
	
	
	/**
	 *set current state as rectangle state
	 */
	public void setReceiveState() {
		currentState = receiveState;
		System.out.println("receive state");
	}
	public State getCurrentState()
	{
		return currentState;
	}
/**
 * set current state as circle state
 */
	public void setSendState() {
		currentState = sendState;
		System.out.println("send state");
	}


}
