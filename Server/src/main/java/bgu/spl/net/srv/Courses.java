package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Courses {
    // private fields:
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

    public void createCourse(int courseNumber, int numOfMaxStudents, String name, int[] kdam) {
        Course course = new Course(courseNumber, numOfMaxStudents, name, kdam);
        this.courses.add(course); // Adds the course to the courses list
    }

    public boolean register(Course course, User user) {
        return course.register(user);
    }
}





















