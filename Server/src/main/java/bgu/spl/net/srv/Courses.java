package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
        this.courses = new CopyOnWriteArrayList<>();
    }

    private void addCourse(Course course) {
        this.courses.add(course); // Adds the course to the courses list
        this.courseIdToIndex.put(course.getCourseNumber(), this.courses.indexOf(course));
    }

    public void createCourse(int courseNumber, int numOfMaxStudents, String name, int[] kdam) {
        Course course = new Course(courseNumber, numOfMaxStudents, name, kdam);
        this.addCourse(course); // Add the course to the list and hashmap
    }

    public boolean register(Course course, User user) {
        return course.register(user);
    }

    /**
    * Gets a Course object by its ID by pulling its location in the courses array from the HashMap.
    * @param id - The course's ID number.
     */
    public Course getCourse(int id) {
        try {
            Integer index = this.courseIdToIndex.get(id);
            if (Objects.isNull(index)) // Course not found in HashMap // TODO: test this!!!
                for (Course course : this.courses)
                    if (course.getCourseNumber() == id) return course;

            return this.courses.get(index);
        }
        catch (NoSuchElementException e) {
            throw new NoSuchElementException("Course number " + id + " does not exist.");
        }
    }

    /**
     * Validates all the courses' fields.
     */
    public boolean validateCourses() {
        for (Course course : this.courses)
            if (!course.isValid()) return false;
        return true;
    }
}





















