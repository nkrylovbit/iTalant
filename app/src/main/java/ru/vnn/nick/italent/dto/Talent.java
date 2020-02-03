package ru.vnn.nick.italent.dto;

import java.util.Objects;

public class Talent {
    private long id;
    private String name;
    private int color;

    public Talent() {

    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Talent{" +
                "name='" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Talent talent = (Talent) o;
        return  Objects.equals(name, talent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
