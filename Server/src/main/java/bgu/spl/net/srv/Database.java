package bgu.spl.net.srv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	// private fields:
	ConcurrentHashMap<String, User> users; // users<String username, User user>
	Courses courses;
	Clients clients;

	String input_file_path;

	private final static class SingletonHolder {
		private final static Database instance = new Database();
	}

	public synchronized void userLogin(String clientId, String username, String password) throws Exception {
		User user = this.getUser(username);
		User current_user = this.Clients().get(clientId).getUser();
		if (!Objects.isNull(current_user)) /*{
			if (current_user != user)*/
				throw new Exception("Client already logged-in with a different user.");
			/*else { // current_user == new_user
				user.login(password);
			}
		}*/ // TODO: check if ACK when logging-in again with same user correctly
		else { // Client isn't logged-in
			if (!user.isLoggedIn()) { // Check if the user is already logged-in with a different client
				user.login(password);
				this.Clients().setUser(clientId, username); // Associate the user with this client
			}
			else {
				throw new Exception("User is already logged-in.");
			}
		}
	}

	public void logoutUser(String clientId) {
		// get(clientId) handles no client error,
		// client.getUser() handles no user error,
		// user.logout() handles user not logged-in error (though never should happen).
		if (this.Clients().get(clientId).getUser().logout()) // logout user
			this.Clients().get(clientId).removeUser(); // remove the user association to the client
	}

	public Courses Courses() {
		return this.courses;
	}

	public Clients Clients() {
		return this.clients;
	}

	public void createStudent(String username, String password) {
		createUser(username, password, false);
	}

	public User createUser(String username, String password, boolean isAdmin) {
		User user = new User(username, password, isAdmin);

		System.out.println("Adding user to DB if it doesn't exist: " + username);

		if (!Objects.isNull(this.users.putIfAbsent(username, user))) // If user doesn't exists, HashMap returns null
			throw new IllegalArgumentException("This user already exists.");

		return user;
	}

	public User getUser(String username) {
		User user = this.users.get(username);
		if (Objects.isNull(user))
			throw new IllegalArgumentException("User '" + username + "' does not exist.");

		return user;
	}

	private String _get_input_file_path() {
		return this.input_file_path;
	}

	private void generateCoursesFromFile(String coursesFilePath) throws FileNotFoundException {
		File file = new File(coursesFilePath);
		try (Scanner reader = new Scanner(file)) {
			// Read the courses file line by line so as to keep them ordered
			while (reader.hasNextLine()) {
				//File format: number|name|[kdamnumber1, kdamnumber2]|seats
				String line = reader.nextLine();
				String[] data = line.split("\\|");

				// Create an array of kdam courses:
				List<Integer> kdam_numbers = new ArrayList<>();
				if (!data[2].equals("[]")) {
					String[] kdam = data[2].substring(1, data[2].length()-1).split(",");
					for (String kdam_num : kdam)
						kdam_numbers.add(parseInt(kdam_num));
				}

				// Add the new course to the courses list (by order of appearance in the courses file):
				courses.createCourse(
					parseInt(data[0]), // courseNumber
					parseInt(data[3]), // numOfMaxStudents
					data[1], // name
					kdam_numbers // kdam courses
				);
			}
		}
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("Courses.txt file not found in path: " + _get_input_file_path());
		}
	}

	// Private to prevent user from creating new Database
	private Database() {
		this.courses = Courses.getInstance();
		this.clients = Clients.getInstance();
		this.users = new ConcurrentHashMap<>();

		// TODO
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		// TODO

		this.input_file_path = "./Courses.txt";
		this.initialize(this._get_input_file_path());
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * loads the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		try {
			generateCoursesFromFile(coursesFilePath);
		}
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return this.courses.validateCourses();
	}
}
