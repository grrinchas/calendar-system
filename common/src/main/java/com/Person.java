package com;

import java.io.Serializable;

@SuppressWarnings("all")
public class Person implements Serializable {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + '}';
    }

    public String getName() {
        return this.name;
    }
}
