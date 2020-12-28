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
        // TODO: TEST
        String role = "student";
        if (isAdmin) role = "admin";
        System.out.println("Creating User: " + username + " as " + role + " with password " + password);
        // TODO: TEST

        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLoggedIn = false;
    }

    public boolean login(String password) throws Exception {
        // TODO: TEST
        System.out.println("Login as " + this.getUsername() + " with password " + password);
        // TODO: TEST

        if (this.isLoggedIn()) // Check whether the user is already logged in
            throw new Exception("The user is already logged in.");
        else {
            // TODO: TEST
            System.out.println("Login successful.");
            // TODO: TEST

            this.isLoggedIn = (password.equals(this.password));
            return this.isLoggedIn();
        }
    }

    public boolean logout() {
        this.isLoggedIn = false;
        return !this.isLoggedIn();
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
}
