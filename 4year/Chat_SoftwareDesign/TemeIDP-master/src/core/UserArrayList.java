package core;

import java.util.ArrayList;

import users.User;
/**
 * 
 * class extends ArrayList for easily working with a list of users
 */
@SuppressWarnings("serial")
public class UserArrayList extends ArrayList<User> {
	public User getUser(String user)
	{
		for(User u:this)
		{
			if(u.getName().equals(user)) 
				return u;
		}
		return null;
	}
	public void removeUser(String user) {
		for(User u: this)
		{
			if(u.getName().equals(user))
			{
				this.remove(u);
				return;
			}
		}
	}
}
