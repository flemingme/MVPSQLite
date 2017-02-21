package com.fleming.exmaple.mvpsqlite.local;

/**
 * User
 * Created by Fleming on 2016/11/30.
 */

public class User {

    private int _id;
    private String name;
    private int age;

    public User() {
    }

//    public User(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }

    public User(int _id, String name, int age) {
        this._id = _id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
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
        return "User{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
