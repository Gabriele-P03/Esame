package com.greenhouse.json;

/**
 * A map is a corresponding between a key and a value<br>
 * A key cannot contains any array or object, so here it doesn't need to has an arraylist<br>
 *
 * e.g. "key": "value"
 */

public class JSONMap {

    public JSONMap(String mapAsString){
        this(
                mapAsString.replaceAll("\"", "").split(":")[0],
                mapAsString.replaceAll("\"", "").split(":")[1]);
    }

    public JSONMap(String key, String value){
        this.key = key.trim();
        this.value = this.separate(value);//Only here it is gonna not trimming
    }

    private String key, value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    /**
     * Often the value passed is not the exact value, it may contains objects, arrays.
     * Value finish once ',' is met
     * @param value
     * @return value contained in @value
     */
    private String separate(String value){
        int index = value.indexOf(",");
        if(index == 0)
            return "";
        else if(index == -1)
            return value;
        else
            return value.substring(0, index);
    }

    @Override
    public String toString() {
        return "\"" + this.key + "\" : " + "\"" + this.value + "\"";
    }
}
