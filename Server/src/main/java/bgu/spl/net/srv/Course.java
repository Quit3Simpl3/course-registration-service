package bgu.spl.net.srv;

/*
 * Object representing a specific course in the Database
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Course {
    // private fields:
    final int courseNumber;
    final String name;
    final List<Integer> kdam;
    ConcurrentHashMap<String, User> students;
    AtomicInteger freeSeats;
    final int maxStudents;
    Courses courses;

    public Course(int courseNumber, int numOfMaxStudents, String name, List<Integer> kdam) {
        this.courses = Courses.getInstance();
        this.courseNumber = courseNumber;
        this.maxStudents = numOfMaxStudents;
        this.freeSeats = new AtomicInteger(numOfMaxStudents);
        this.students = new ConcurrentHashMap<>();
        this.name = name;
        this.kdam = new ArrayList<>(kdam);
    }

    public List<Integer> getKdam() {
        return this.kdam;
    }

    public String getName() {
        return this.name;
    }

    public List<User> getStudents() {
        List<User> students = new ArrayList<>();
        for (User user : this.students.values())
            students.add(user);
        return students;
    }

    public boolean containsStudent(User user) {
        return this.students.containsKey(user.getUsername());
    }

    public boolean containsStudent(String username) {
        return this.students.containsKey(username);
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    public int getAvailableSeats() {
        return this.freeSeats.get();
    }

    public void register(User student) throws Exception {
        if (Objects.isNull(student))
            throw new NullPointerException("Student object is null");

        if (student.isAdmin()) // Check whether this user is an admin
            throw new IllegalArgumentException("Admins cannot register to courses.");

        if (this.containsStudent(student))
            throw new IllegalArgumentException("Student " + student.getUsername() + " is already registered to course " + this.getCourseNumber());

        synchronized (this) {
            // Check if a seat is available:
            if (this.freeSeats.get() <= 0) {
                throw new Exception("No available seats for this course.");
            }
            else { // Register the user to this course:
                this.freeSeats.getAndDecrement(); // Update seat count: freeSeats--
                // Check if student is already in this course:
                if (this.containsStudent(student))
                    this.freeSeats.getAndIncrement(); // Release the student's seat
                else { // Otherwise, continue with registration:
                    // Check kdam courses:
                    for (int kdam_course : this.kdam) {
                        if (!(this.courses.getCourse(kdam_course)).containsStudent(student)) {
                            this.freeSeats.getAndIncrement(); // Release the student's seat
                            throw new Exception("The student isn't registered to all the kdam courses.");
                        }
                    }
                    // Register student:
                    students.put(student.getUsername(), student);
                }
            }
        }
    }

    public synchronized boolean unregister(User student) {
        String username;
        try {
            username = (this.students.remove(student.getUsername())).getUsername();
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("The student is not registered to this course.");
        }
        this.freeSeats.getAndIncrement();
        // Make sure the correct student was removed:
        return (username.equals(student.getUsername()));
    }

    public boolean isValid() {
        return (
                this.courseNumber >= 0
                && this.name.length() > 0
                && this.maxStudents >= 5
        );
    }

    public int getCourseNumber() {
        return this.courseNumber;
    }
}