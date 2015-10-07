package test;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingWorker;

import users.User;
import core.Mediator;

/**
 * 
 * Contains a file list, and 2 user lists (online and offline). Once every 20
 * seconds connects, disconnects or updates a user's file list.
 * 
 */
public class Test extends SwingWorker<Integer, Integer> {

	private Mediator med;
	Random rand = new Random();

	private final int ADD_USER = 0;
	private final int RM_USER = 1;
	private final int UPDATE_FILELIST = 2;
	private final int ADD_FILE = 0;
	private final int RM_FILE = 1;
	private final int MAX_USERS = 5; 
	ArrayList<User> offline_users = new ArrayList<User>();
	ArrayList<User> online_users = new ArrayList<User>();
	ArrayList<String> fl = new ArrayList<>();
	int index;
	User user;

	public Test(Mediator med) {
		this.med = med;

		/**
		 * add files
		 */
		fl.add("Doombringer");
		fl.add("Foehammer");
		fl.add("Frost Blade");
		fl.add("Frost Axe");
		fl.add("The Rune Staff Stormcaller");
		fl.add("Morkai");
		fl.add("Spear of Russ");
		fl.add("Teeth of the Blizzard");
		fl.add("Rune Staff");
		fl.add("Anvil Shield");
		fl.add("Wolf Helm of Russ");
		fl.add("Great Wolf Pelt");
		fl.add("The Hood of Gnyrll");
		fl.add("Frostfang");
		fl.add("The Pelt of Wulfen");
		fl.add("The Pelt of Wulfen");
		fl.add("Raiment of the Silent Wolf");
		fl.add("Runic Armour");
		fl.add("Fenris-Pattern Wolf Helm");
		fl.add("Cup of Wulfen");
		fl.add("Fang of Morkai");
		fl.add("Wolf Amulet");
		fl.add("Wolf Tooth Necklace");
		/**
		 * add offline users
		 */
		User user = new User("Leman Russ");
		offline_users.add(user);
		user = new User("Bjorn the Fell-Handed");
		offline_users.add(user);
		user = new User("Logan Grimnar");
		offline_users.add(user);
		user = new User("Ragnar Blackmane");
		offline_users.add(user);
		user = new User("Sven Bloodhowl");
		offline_users.add(user);
		user = new User("Vaer Greyloc");
		offline_users.add(user);
	}

	@Override
	protected Integer doInBackground() throws Exception {

		for (int i = 0; i < offline_users.size(); i++) {
			int size = rand.nextInt(4) + 1;
			for (int j = 0; j < size; j++) {
				offline_users.get(i).insertFile(fl.get(rand.nextInt(fl.size() - 1)));
			}
		}

		user = offline_users.get(3);
		offline_users.remove(3);
		User u = new User(user.getName());
		ArrayList<String> st = user.getFiles();
		for (String str : st) {
			u.insertFile(str);
		}
		med.addUserToModel(u);
		med.setCurrentUser(user.getName());

		System.out.println("users: " + offline_users);

		// add user for testing
		index = rand.nextInt(offline_users.size() - 1);
		user = offline_users.get(index);
		online_users.add(user);
		offline_users.remove(index);
		u = new User(user.getName());
		for (String str : user.getFiles()) {
			u.insertFile(str);
		}
		med.addUserToModel(u);
		/**
		 * after 5 seconds, a new event is generated
		 */
		while (true) {
			int operation = rand.nextInt(3);
			System.out.println("enterred while w/ op " + operation);
			switch (operation) {
			case ADD_USER:
				if (online_users.size() == MAX_USERS) {
					System.out.println("no more users for testing");
					break;
				}
				if (offline_users.size() == 1) {
					index = 0;
				} else {
					index = rand.nextInt(offline_users.size() - 1);
				}
				user = offline_users.get(index);
				online_users.add(user);
				offline_users.remove(index);
				u = new User(user.getName());
				for (String str : user.getFiles()) {
					u.insertFile(str);
				}
				med.addUserToModel(u);
				System.out.println("add user " + user);
				break;

			case RM_USER:
				if (online_users.isEmpty()) {
					System.out.println("no user online");
					break;
				}
				if (online_users.size() < 2) {
					System.out.println("forced to keep user");
					break;
				} else {
					index = rand.nextInt(online_users.size() - 1);
				}
				user = online_users.get(index);
				offline_users.add(user);
				online_users.remove(index);
				med.removeUserFromModel(user.getName());
				System.out.println("rm user " + user);
				break;

			case UPDATE_FILELIST:
				int size = rand.nextInt(2) + 1;
				if (online_users.isEmpty()) {
					System.out.println("no user online");
					break;
				}
				if (online_users.size() == 1) {
					index = 0;
				} else {
					index = rand.nextInt(online_users.size() - 1);
				}
				for (int i = 0; i < size; i++) {
					online_users.get(index).insertFile(
							fl.get(rand.nextInt(fl.size() - 1)));
				}
				med.addFilesToUser(online_users.get(index).getName(), online_users
						.get(index).getFiles());
				System.out.println("user's " + online_users.get(index).getName()
						+ " file list updated");
				break;

			default:
				break;
			}

			Thread.sleep(5000);
		}

	}

}
