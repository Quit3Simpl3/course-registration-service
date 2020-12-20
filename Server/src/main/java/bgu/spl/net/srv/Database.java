package bgu.spl.net.srv;

import java.util.List;

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
	// TODO: users hashtable
	// TODO: HashMap<int courseNumber, Course course>
	// Courses Class: List<Course>

	private final static class SingletonHolder {
		private final static Database instance = new Database();
	}

	// To prevent user from creating new Database
	private Database() {
		// TODO: implement
		// 1. Read Courses.txt file
		// 2. Parse into
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return SingletonHolder.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		// TODO: implement
		return false;
	}
}
