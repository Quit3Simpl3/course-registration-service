package bgu.spl.net.srv;

import java.util.List;

public class User {
    // private fields:
    String username;
    String password;
    boolean isAdmin;
    boolean isLoggedIn;

    public User(String username, String password) {
        new User(username, password, false);
    }

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLoggedIn = false;
    }

    public boolean login(String password) throws Exception {
        if (this.isLoggedIn()) // Check whether the user is already logged in
            throw new Exception("The user is already logged in.");
        else {
            this.isLoggedIn = (password.equals(this.password));
            return this.isLoggedIn();
        }
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public List<Course> getCourses() {
        return Courses.getInstance().getStudentCourses(this);
    }

    public void registerCourse(Course course) throws Exception {
        Courses.getInstance().register(course, this);
    }
}
