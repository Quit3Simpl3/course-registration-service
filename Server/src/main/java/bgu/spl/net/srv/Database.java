package bgu.spl.net.srv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
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
	ConcurrentHashMap<Integer, Course> courses; // courses<int courseNumber, Course course>

	String input_file_path;

	private final static class SingletonHolder {
		private final static Database instance = new Database();
	}

	private String _get_input_file_path() {
		return this.input_file_path;
	}

	private Course createCourse(int numOfMaxStudents, int courseNumber, String name, int[] kdam) {
		return new Course(numOfMaxStudents, courseNumber, name, kdam);
	}

	private void generateCoursesFromFile() {
		File file = new File(_get_input_file_path());
		try (Scanner reader = new Scanner(file)) {
			while (reader.hasNextLine()) {
				//File format: number|name|[kdamnumber1, kdamnumber2]|seats
				String line = reader.nextLine();
				String[] data = line.split("|");
				// Create an array of kdam courses:
				String[] kdam = data[2].split("|");
				int[] kdam_numbers = new int[kdam.length];
				for (int i = 0; i < kdam.length; i++) kdam_numbers[i] = parseInt(kdam[i]);

				// Add the new course to the courses hashmap:
				this.courses.put(
					parseInt(data[0]),
					createCourse(
						parseInt(data[3]),
						parseInt(data[0]),
						data[1],
						kdam_numbers
					)
				);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Courses.txt file not found in path: " + _get_input_file_path());
		}
	}

	// Private to prevent user from creating new Database
	private Database() {
		this.courses = new ConcurrentHashMap<>();
		this.input_file_path = "./resources/Courses.txt";
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return SingletonHolder.instance;
	}

	private boolean validateCourses() {
		for (Integer course_number : this.courses.keySet()) {
			if (!(this.courses.get(course_number)).isValid()) return false;
		}
		return true;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		generateCoursesFromFile();
		return validateCourses();
	}
}
