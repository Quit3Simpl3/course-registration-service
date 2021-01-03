package bgu.spl.net.srv;

import java.util.List;

public class User {
    // private fields:
    String username;
    String password;
    boolean isAdmin;
    boolean isLoggedIn;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLoggedIn = false;
    }

    public void login(String password) throws Exception {
        if (password.equals(this.password)) {
            this.isLoggedIn = true;
        }
        else
            throw new Exception("Wrong password \"" + password + "\" for user \"" + this.getUsername() + "\"");
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
