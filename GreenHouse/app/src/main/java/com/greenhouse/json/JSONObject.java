package com.greenhouse.json;

import java.util.ArrayList;

/**
 *
 * This is the abstraction of a JSON object.
 *
 * It can be either identified by a name or not have it.
 *
 * A JSON Object is identified only when it is inned in another one json object,
 * otherwise it has never any name as identifier.
 * When it is a no-identified object, the string name in {@link JSONObject#JSONObject(String, String)} is
 * passed as empty string
 *
 * A JSON object can contains other {@link JSONObject}, {@link JSONArray} or {@link JSONMap}
 * They're saved in an appropriate arraylist
 *
 * @author Gabriele-P03
 */

public class JSONObject {

    private String name;
    private ArrayList<JSONObject> objects;
    private ArrayList<JSONArray> arrays;
    private ArrayList<JSONMap> maps;

    public JSONObject(String name, String jsonAsString) {
        this.name = name;
        this.objects = new ArrayList<>();
        this.arrays = new ArrayList<>();
        this.maps = new ArrayList<>();
        this.compute(jsonAsString);
    }

    public void setName(String name) { this.name = name; }

    public String getName() {
        return name;
    }

    public ArrayList<JSONObject> getObjects() {
        return objects;
    }

    public ArrayList<JSONArray> getArrays() {
        return arrays;
    }

    public ArrayList<JSONMap> getMaps() {
        return maps;
    }

    /**
     * it computes the given json as string
     * @param jsonAsString
     */
    private void compute(String jsonAsString){

        int length = jsonAsString.length();
        String buffer = "";

        for(int i = 0; i < jsonAsString.length(); i++){

            char charAtI = jsonAsString.charAt(i);

            if(charAtI == '}'){
                break;
            }else if(charAtI == ']'){
                throw new RuntimeException("Square bracket not expected");
            }else if(charAtI == ':'){
                //It's beginning either a new object or array or value of map

                //Check if it is over
                if(i+1 < length){
                    char next = jsonAsString.charAt(i+1);
                    if(next == '{'){
                        String subObjectAsString = jsonAsString.substring(i+2);
                        int end = JSONParser.getIndexOfEndObject(subObjectAsString);
                        this.objects.add(new JSONObject(buffer, subObjectAsString.substring(0, end)));
                        i += 2 + end;
                    }else if( next == '['){
                        String subObjectAsString = jsonAsString.substring(i+2);
                        int end = JSONParser.getIndexOfEndArray(subObjectAsString);
                        this.arrays.add(new JSONArray(buffer, subObjectAsString.substring(0, end)));
                        i += 2 + end;
                    }else{
                        String subObjectAsString = jsonAsString.substring(i+1);
                        int end = JSONParser.getIndexOfEndMap(subObjectAsString);
                        this.maps.add(new JSONMap(buffer, subObjectAsString.substring(0, end)));
                        i += 1 + end;
                    }

                    buffer = "";
                }else{
                    throw new RuntimeException("JSON file finishes with a strange char...");
                }

            }else if(charAtI == ','){
                continue;
            }else{
                buffer += charAtI;
            }
        }
    }

    @Override
    public String toString() {
        String buffer = "";

        if(this.name != null) {
            if (this.name.length() > 0)
                buffer += this.name + ":";
        }

        buffer += "{";

        for (int i = 0; i < this.objects.size(); i++){

            buffer += this.objects.get(i).toString();

            if(i < this.objects.size() - 1 || !this.arrays.isEmpty() || !this.maps.isEmpty()){
                buffer += ",";
            }
        }

        for (int i = 0; i < this.arrays.size(); i++){

            buffer += this.arrays.get(i).toString();

            if(i < this.arrays.size() - 1 || !this.maps.isEmpty()){
                buffer += ",";
            }
        }

        for (int i = 0; i < this.maps.size(); i++){

            buffer += this.maps.get(i).toString();

            if(i < this.maps.size() - 1 ){
                buffer += ",";
            }
        }

        return buffer + "}";
    }
}
