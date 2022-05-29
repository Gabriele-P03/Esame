package com.greenhouse.json;

import java.util.ArrayList;

public class JSONArray {

    private String name;
    private ArrayList<JSONObject> objects;
    private ArrayList<JSONArray> arrays;
    private ArrayList<String> values;

    public JSONArray(String name, String jsonAsString) {
        this.name = name;
        this.objects = new ArrayList<>();
        this.arrays = new ArrayList<>();
        this.values = new ArrayList<>();
        this.compute(jsonAsString);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<JSONObject> getObjects() {
        return objects;
    }

    public ArrayList<JSONArray> getArrays() {
        return arrays;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    /**
     * it computes the given json as string
     * @param jsonAsString
     */
    private void compute(String jsonAsString){

        String buffer = "";

        for(int i = 0; i < jsonAsString.length(); i++){

            if(i > 230){
                buffer = buffer;
            }

            char charAtI = jsonAsString.charAt(i);

            if(charAtI == ']'){
                break;
            }else if(charAtI == '}'){
                throw new RuntimeException("Square bracket not expected");
            }if(charAtI == '{') {
                String subObjectAsString = jsonAsString.substring(i + 1);
                int end = JSONParser.getIndexOfEndObject(subObjectAsString);
                this.objects.add(new JSONObject("", subObjectAsString.substring(0, end)));
                i += 1 + end;
            }else if(charAtI == '['){
                String subObjectAsString = jsonAsString.substring(i+1);
                int end = JSONParser.getIndexOfEndArray(subObjectAsString);
                this.arrays.add(new JSONArray("", subObjectAsString.substring(0, end)));
                i += 1 + end;

                //String#length() prevents it from add an empty value.
                //It can happen when a comma is just after a closing-curly bracket
            }else if(charAtI == ','){
                if(buffer.length() > 0){
                    this.values.add(buffer);
                    buffer = "";
                }else{
                    continue;
                }
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

        buffer += "[";

        for (int i = 0; i < this.objects.size(); i++){

            buffer += this.objects.get(i).toString();

            if(i < this.objects.size() - 1 || !this.arrays.isEmpty() || !this.values.isEmpty()){
                buffer += ",";
            }
        }

        for (int i = 0; i < this.arrays.size(); i++){

            buffer += this.arrays.get(i).toString();

            if(i < this.arrays.size() - 1 || !this.values.isEmpty()){
                buffer += ",";
            }
        }

        for (int i = 0; i < this.values.size(); i++){

            buffer += this.values.get(i);

            if(i < this.values.size() - 1 ){
                buffer += ",";
            }
        }

        return buffer + "]";
    }
}
