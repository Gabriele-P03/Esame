package com.greenhouse.cloud.collector.data;

import com.greenhouse.json.JSONMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class OutsideData extends InsideData{

    private int temperature, humidity, light;

    public OutsideData(int ID,  int plants, int leaves, int max_height, LocalDate date, String username_fk_employee, int temperature, int humidity, int light) {
        super(ID, plants, leaves, max_height, date, username_fk_employee);
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
    }

    public OutsideData(ArrayList<JSONMap> maps) throws ParseException {
        this(Integer.parseInt(maps.get(0).getValue()),
                Integer.parseInt(maps.get(1).getValue()),
                Integer.parseInt(maps.get(2).getValue()),
                Integer.parseInt(maps.get(3).getValue()),
                LocalDate.parse(maps.get(4).getValue()),
                maps.get(9).getValue(),
                Integer.parseInt(maps.get(5).getValue()),
                Integer.parseInt(maps.get(6).getValue()),
                Integer.parseInt(maps.get(7).getValue()));

    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
