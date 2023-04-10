package com.digdes.school;

public class Colon {
    private String name;
    private Class type;

    public Colon() {
    }

    public Colon(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Colon{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
