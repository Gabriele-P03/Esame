package com.greenhouse.json;

import java.util.ArrayList;

/**
 * An array is represented by square brackets, they can contains values, arrays and objects;
 * these last two cannot be declared with a name.<br><br>
 * e.g.<br>
 * "array" : [<br>
 *  "valueString", 1, [anotherArray], {Object}<br>
 * ]
 */

public class JSONArray {

    //to a key name not available for objects/arrays declared inside themselves, it can be null
    private String name;
    private int length = 0;

    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<JSONArray> arrays = new ArrayList<>();
    private ArrayList<JSONObject> objects = new ArrayList<>();

    public JSONArray(String arrayAsString){
        this("", arrayAsString);
    }

    public JSONArray(String name, String arrayAsString) {

        this.name = name;
        String tmp = "";

        for(int index = 0; index < arrayAsString.length(); index++){

            char c = arrayAsString.charAt(index);

            //Length takes count of commas, brackets and colons, too
            this.length++;

            if(JSONReader.isValidChar(c)){
                tmp += c;
            }else {

                if (c == ':') {

                    String nextChar = this.getNextChar(arrayAsString, index);
                    if(nextChar != "" && nextChar != null) {

                        if (nextChar.equals("{")) {
                            JSONObject object = new JSONObject(tmp, arrayAsString.substring(index+2));
                            this.objects.add(object);
                            this.length += object.getLength();
                            index += object.getLength();
                        } else if (nextChar.equals("[")) {
                            JSONArray array = new JSONArray(tmp, arrayAsString.substring(index+2));
                            this.arrays.add(array);
                            this.length += array.getLength();
                            index += array.getLength();
                        } else if (nextChar.equals("]")) {
                            break;
                        } else if (nextChar.equals("}")) {
                            continue;
                        }else if(nextChar.equals(',')) {
                            continue;
                        }
                    }
                }else if(c == ',' || (tmp != "" && c == ']')){
                    this.values.add(tmp);
                }
                tmp = "";
            }

            if(c == ']')
                break;
        }
    }

    private String getNextChar(String string, int index){
        return string.length() > index+1 ? String.valueOf(string.charAt(index+1)) : null;
    }


    public void addObject(String objectAsString){ this.objects.add(new JSONObject(objectAsString)); }
    public void addArray(String arrayAsString){ this.arrays.add(new JSONArray(arrayAsString)); }
    public void addValue(String value){this.values.add(value);}


    public JSONObject[] getObjectByName(String name){ return this.objects.stream().filter(object -> object.getName().equals(name)).toArray(JSONObject[]::new); }
    public JSONArray[] getArrayByName(String name){ return this.arrays.stream().filter(array -> array.getName().equals(name)).toArray(JSONArray[]::new); }
    public String getName() {
        return name;
    }
    public ArrayList<String> getValues() {
        return values;
    }
    public ArrayList<JSONArray> getArrays() {
        return arrays;
    }
    public ArrayList<JSONObject> getObjects() {
        return objects;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        String asArray = "";

        if(this.name != null && this.name != ""){
            asArray += "\"" + this.name + "\": ";
        }

        asArray += "[\n";

        for(int i = 0; i < this.objects.size(); i++){
            asArray += this.objects.get(i).toString();

            if( i+1 < this.objects.size() || !this.arrays.isEmpty() || !this.values.isEmpty()){
                asArray += ",";
            }

        }

        for(int i = 0; i < this.arrays.size(); i++){
            asArray += this.arrays.get(i).toString();

            if( i+1 < this.arrays.size() || !this.values.isEmpty()){
                asArray += ",";
            }

        }

        for(int i = 0; i < this.values.size(); i++){


            asArray += "\"" + this.values.get(i) + "\"";

            if( i+1 < this.values.size()){
                asArray += ",";
            }

        }

        asArray += "\n]";

        return asArray;
    }
}
