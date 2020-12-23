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

	String input_file_path;

	private final static class SingletonHolder {
		private final static Database instance = new Database();
	}

	public Course getCourse(int id) {
		return this.courses.getCourse(id);
	}

	public User getUser(String username) {
		User user = this.users.get(username);
		if (Objects.isNull(user))
			throw new IllegalArgumentException("User " + username + " does not exist.");

		return user;
	}

	private String _get_input_file_path() {
		return this.input_file_path;
	}

	private void generateCoursesFromFile(String coursesFilePath) {
		File file = new File(coursesFilePath);
		try (Scanner reader = new Scanner(file)) {
			// Read the courses file line by line so as to keep them ordered
			while (reader.hasNextLine()) {
				//File format: number|name|[kdamnumber1, kdamnumber2]|seats
				String line = reader.nextLine();
				String[] data = line.split("\\|");

				// Create an array of kdam courses:
				String[] kdam = data[2].split("\\|");
				int[] kdam_numbers = new int[kdam.length];
				for (int i = 0; i < kdam.length; i++) kdam_numbers[i] = parseInt(kdam[i]);

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
			System.out.println("Courses.txt file not found in path: " + _get_input_file_path());
		}
	}

	// Private to prevent user from creating new Database
	private Database() {
		this.courses = Courses.getInstance();
		this.input_file_path = "./resources/Courses.txt";
		this.initialize(this._get_input_file_path());
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
		generateCoursesFromFile(coursesFilePath);
		return this.courses.validateCourses();
	}
}
