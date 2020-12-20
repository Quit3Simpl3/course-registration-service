package bgu.spl.net.srv;

import java.util.HashMap;

public class Courses {
    // private fields:
    HashMap<Integer, Course> courses; // courses<courseNum, courseObject>

    private final static class SingletonHolder {
        private final static Courses instance = new Courses();
    }

    public static Courses getInstance() {
        return SingletonHolder.instance;
    }

    private Courses() {
        this.courses = new HashMap<>();
    }

    public boolean createCourses() {
        // TODO
        return false;
    }

    private boolean createCourse(int maxStudents, int courseNumber) {
        // TODO
        Course course = new Course(maxStudents, courseNumber);
        return false;
    }

    public boolean register(Course course, User user) {
        return course.register(user);
    }
}
