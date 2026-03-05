package com.dms.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role; // ADMIN, SALES, SERVICE
    private boolean active;

    public User(int id, String username, String password, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
}
