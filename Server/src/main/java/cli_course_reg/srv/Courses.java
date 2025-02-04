package cli_course_reg.srv;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Courses {
    // private fields:
    ConcurrentHashMap<Integer, Integer> courseIdToIndex;
    CopyOnWriteArrayList<Course> courses; // Using List instead of HashMap so that the order is kept.

    private final static class SingletonHolder {
        private final static Courses instance = new Courses();
    }

    public static Courses getInstance() {
        return SingletonHolder.instance;
    }

    private Courses() {
        this.courseIdToIndex = new ConcurrentHashMap<>();
        this.courses = new CopyOnWriteArrayList<>();
    }

    private void addCourse(Course course) {
        if (this.courseIdToIndex.containsKey(course.getCourseNumber()))
            throw new IllegalArgumentException("This course already exists.");

        this.courses.add(course); // Adds the course to the courses list
        this.courseIdToIndex.put(course.getCourseNumber(), this.courses.indexOf(course));
    }

    public void createCourse(int courseNumber, int numOfMaxStudents, String name, List<Integer> kdam) {
        Course course = new Course(courseNumber, numOfMaxStudents, name, kdam);
        this.addCourse(course); // Add the course to the list and hashmap
    }

    public void register(Course course, User user) throws Exception {
        course.register(user);
    }

    public boolean unregister(Course course, User user) {
        return course.unregister(user);
    }

    /**
    * Gets a Course object by its ID by pulling its location in the courses array from the HashMap.
    * @param id - The course's ID number.
     */
    public Course getCourse(int id) {
        for (Course course : this.courses) {
            if (course.getCourseNumber() == id) {
                return course;
            }
        }
        return null;
    }

    /**
     * Validates all the courses' fields.
     */
    public boolean validateCourses() {
        for (Course course : this.courses)
            if (!course.isValid()) return false;
        return true;
    }

    /**
     * Return a List of all courses the student is signed up to.
     */
    public List<Course> getStudentCourses(User student) {
        List<Course> userCourses = new ArrayList<>();
        // Add the courses to the userCourses list by order of appearance in Courses.txt:
        for (Course course : this.courses)
            if (course.containsStudent(student))
                userCourses.add(course);

        return userCourses;
    }

    /**
     * Returns whether the student is registered to the course.
     * @param student - The student's user object.
     * @param course - The course object.
     * @return true iff the student is registered to the course.
     */
    public boolean isStudentRegistered(User student, Course course) {
        return course.containsStudent(student);
    }
}