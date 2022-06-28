package com.greenhouse.cloud.collector.data;

import com.greenhouse.json.JSONMap;
import com.greenhouse.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class InsideData {

    private int ID;
    private LocalDate date;
    private int plants, leaves, max_height;
    private String username_fk_employee;

    public InsideData(int ID, int plants, int leaves, int max_height, LocalDate date, String username_fk_employee) {
        this.ID = ID;
        this.date = date;
        this.plants = plants;
        this.leaves = leaves;
        this.max_height = max_height;
        this.username_fk_employee = username_fk_employee;
    }

    public InsideData(ArrayList<JSONMap> maps) throws ParseException {
        this(
                Integer.parseInt(maps.get(0).getValue()),
                Integer.parseInt(maps.get(1).getValue()),
                Integer.parseInt(maps.get(2).getValue()),
                Integer.parseInt(maps.get(3).getValue()),
                LocalDate.parse(maps.get(4).getValue()),
                maps.get(6).getValue()
        );
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPlants() {
        return plants;
    }

    public void setPlants(int plants) {
        this.plants = plants;
    }

    public int getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public int getMax_height() {
        return max_height;
    }

    public void setMax_height(int max_height) {
        this.max_height = max_height;
    }

    public String getUsername_fk_employee() {
        return username_fk_employee;
    }

    public void setUsername_fk_employee(String username_fk_employee) {
        this.username_fk_employee = username_fk_employee;
    }
}


