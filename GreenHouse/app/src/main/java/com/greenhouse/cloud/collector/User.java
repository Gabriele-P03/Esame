package com.greenhouse.cloud.collector;

import com.greenhouse.cloud.jobs.GRADE;
import com.greenhouse.json.JSONMap;
import com.greenhouse.json.JSONObject;

/**
 * This is a simple abstraction of data about an employee
 *
 * @author Gabriele-P03
 */

public class User {

    private int ID_employee;
    private String first_name, last_name, birthday, username, password, CF;
    private GRADE grade;

    public User(int ID_employee, String first_name, String last_name, String birthday, String username, String password, String CF) {
        this.ID_employee = ID_employee;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.username = username;
        this.password = password;
        this.CF = CF;
    }

    public User(JSONObject object){

    }

    public int getID_employee() {
        return ID_employee;
    }

    public void setID_employee(int ID_employee) {
        this.ID_employee = ID_employee;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public GRADE getGrade() {
        return grade;
    }

    public void setGrade(GRADE grade) {
        this.grade = grade;
    }
}
