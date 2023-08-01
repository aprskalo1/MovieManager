/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

/**
 *
 * @author Ante Prskalo
 */
public class User {

    private int id;
    private String username;
    private String userRole;

    public User(String username, String password, String userRole) {
        this.username = username;
        this.userRole = userRole;
    }

    public User(int id, String username, String userRole) {
        this.id = id;
        this.username = username;
        this.userRole = userRole;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserRole() {
        return userRole;
    }

}
