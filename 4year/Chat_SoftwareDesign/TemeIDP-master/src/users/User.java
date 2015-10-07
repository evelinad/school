package users;

import java.util.ArrayList;
/**
 * 
 * User class for keeping information related to a user
 * eg: user name, user files
 *
 */
public class User {

	String name;
	ArrayList<String> files;
	public User(String name) {
		this.name = name;
		files = new ArrayList<String>();
	}
	
	public void insertFile(String file)
	{
		files.add(file);
	}
	public String getName()
	{
		return this.name;	
	}
	public ArrayList<String> getFiles()
	{
		return files;
	}
	
	public void addFiles(ArrayList<String> files)
	{
		this.files.clear();
		for (String file : files) {
			this.files.add(file);
		}
	}

}
