package com.elearn.fp.db.entity;
/**
 * POJO class which represent measurement units for store products
 */
public class Unit {
    private int id;
    private String name;

    public Unit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
