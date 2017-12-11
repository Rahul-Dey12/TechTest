package com.example.kiit.techtest;

/**
 * Created by KIIT on 11-12-2017.
 */

public class Results {
    String name,marks;

    public Results(String name, String marks) {
        this.name = name;
        this.marks = marks;
    }


    public Results() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
