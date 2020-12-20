package bgu.spl.net.srv;

import java.util.List;

public class User {
    // private fields:
    String username;
    String password;
    boolean isStudent;
    boolean isAdmin;
    boolean isLoggedIn;
    List<Course> courses; // TODO: maybe use a resource manager Courses that handles which students are registered to which course

    public User(String username, String password, boolean isAdmin, boolean isStudent) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isStudent = isStudent;
    }

    public boolean login(String password) {
        isLoggedIn = (password.equals(this.password));
        return isLoggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public boolean isStudent() {
        return this.isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public boolean registerCourse(Course course) {
        // TODO:
        // 1. check seats
        // 2. check kdam
        // 3. register (DANGER!!! must be synchronized to prevent two students registering at once)
        return false;
    }
}
