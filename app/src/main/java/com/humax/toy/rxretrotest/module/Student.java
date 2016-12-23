package com.humax.toy.rxretrotest.module;

import java.util.ArrayList;

/**
 * Created by Tony on 16/11/3
 */

public class Student {

    private String id;
    private String name;
    private int age;
    private String[] courses;

    public Student() {

    }

    public Student(String id, String name, int age, String[] courses) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.courses = courses;
    }

    public String[] getCourses() {
        return courses;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", courses=" + courses.length +
                '}';
    }
}
