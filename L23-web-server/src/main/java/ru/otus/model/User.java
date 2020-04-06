package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private long id;
    private String name;
    @JsonProperty(value = "privileged")
    private Boolean isPrivileged;
    private String login;
    private String password;

    public User() {
    }

    public User(String name, String login, String password) {
        this(name, login, password, false);
    }

    public User(String name, String login, String password, Boolean isPrivileged) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.isPrivileged = isPrivileged;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getPrivileged() {
        return isPrivileged;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPrivileged=" + isPrivileged +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
