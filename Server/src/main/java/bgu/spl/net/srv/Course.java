package bgu.spl.net.srv;

/*
 * Object representing a specific course in the Database
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Course {
    // private fields:
    final int courseNumber;
    final String name;
    final int[] kdam;
    ConcurrentHashMap<String, User> students;
    AtomicInteger freeSeats;
    final int maxStudents;

    public Course(int courseNumber, int numOfMaxStudents, String name, int[] kdam) {
        this.courseNumber = courseNumber;
        this.maxStudents = numOfMaxStudents;
        this.freeSeats = new AtomicInteger(numOfMaxStudents);
        this.students = new ConcurrentHashMap<>();
        this.name = name;
        this.kdam = Arrays.copyOf(kdam, kdam.length); // Deep-copy (clone) the argument
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    public int getAvailableSeats() {
        return this.freeSeats.get();
    }

    public synchronized boolean register(User student) {
        // Check if a seat is available:
        if (this.freeSeats.get() > 0) {
            // Update seat count:
            this.freeSeats.getAndDecrement();
            // Check kdam:
            // TODO ...
            // Register user:
            students.put(student.getUsername(), student);
        }
        return false;
    }

    public boolean unregister(User student) {
        String username;
        try {
            username = this.students.remove(student.getUsername()).getUsername();
        }
        catch (NullPointerException e) {
            return false;
        }
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