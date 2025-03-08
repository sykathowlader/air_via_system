package com.example.login;


/**
 * The Advisor class represents an advisor in the system.
 * It contains the advisor's ID, username, name, email, telephone number, and password.
 * @author [Sykat Howlader]
 */
public class Advisor {

    private int advisorID;
    private String username;
    private String name;
    private String email;
    private long telNumber;
    private String password;


    /**
     * Constructs an Advisor object with the given advisor ID, username, name, email, telephone number, and password.
     *
     * @param advisorID the advisor's ID
     * @param username the advisor's username
     * @param name the advisor's name
     * @param email the advisor's email address
     * @param telNumber the advisor's telephone number
     * @param password the advisor's password
     */

    public Advisor(int advisorID, String username, String name, String email, long telNumber, String password) {
        this.advisorID = advisorID;
        this.username = username;
        this.name = name;
        this.email = email;
        this.telNumber = telNumber;
        this.password = password;
    }
    public Advisor(String username, String name, String email, long telNumber, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.telNumber = telNumber;
        this.password = password;
    }

    // Getters and setters for all fields

    public int getAdvisorID() {
        return advisorID;
    }

    public void setAdvisorID(int advisorID) {
        this.advisorID = advisorID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(long telNumber) {
        this.telNumber = telNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}