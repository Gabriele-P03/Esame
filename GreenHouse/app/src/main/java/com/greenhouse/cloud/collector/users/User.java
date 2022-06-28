package com.greenhouse.cloud.collector.users;

import com.greenhouse.cloud.jobs.GRADE;
import com.greenhouse.json.JSONObject;

import java.util.Arrays;

/**
 * This is a simple abstraction of data about an employee
 *
 * @author Gabriele-P03
 */

public class User {

    private int ID_employee;
    private String first_name, last_name, birthday, username, CF;
    private GRADE grade;
    private int ID_fk_ceo;    //The CEO's id who hired this employee

    public User(int ID_employee, String first_name, String last_name, String birthday, String username,
                String CF, GRADE grade, String ID_fk_ceo) {
        this.ID_employee = ID_employee;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.username = username;
        this.CF = CF;
        this.grade = grade;
        try{
            this.ID_fk_ceo = Integer.parseInt(ID_fk_ceo);
        }catch (Exception e){}
    }

    public User(JSONObject object){
        this(Integer.parseInt(object.getMaps().get(0).getValue()),
                object.getMaps().get(1).getValue(),
                object.getMaps().get(2).getValue(),
                object.getMaps().get(3).getValue(),
                object.getMaps().get(5).getValue(),
                object.getMaps().get(4).getValue(),
                Arrays.stream(GRADE.values()).filter( grade1 -> grade1.getIndex() == Integer.parseInt(object.getMaps().get(6).getValue())).findFirst().get(),
                object.getMaps().get(7).getValue()
                );
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

    public int getID_fk_ceo() {
        return ID_fk_ceo;
    }

    public void setID_fk_ceo(int ID_fk_ceo) {
        this.ID_fk_ceo = ID_fk_ceo;
    }
}
