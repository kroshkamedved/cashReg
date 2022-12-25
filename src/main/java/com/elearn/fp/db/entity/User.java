package com.elearn.fp.db.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 * POJO class which represent users in the store with different roles
 */
public class User implements Serializable {

    private int id;

    private String login;

    private String password;

    private UserRole role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && login.equals(user.login) && password.equals(user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role);
    }
}
