package com.greenhouse.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * This is the object which provides json reading parsing.
 * You can even pass an already json-string read, or a file
 *
 * Since a json file can either begin as object or array, the main-object type is not already defined.
 * {@link JSONReader#compute(String)} will do it.
 *
 * You can retrieve the root element of the json file via {@link JSONReader#getRoot()},
 * just use it after have checked its type
 *
 *
 * @author Gabriele-P03
 */

public class JSONReader {

    private Object object;


    public JSONReader(BufferedReader br) throws IOException {
        String buffer = "";
        String tmp = "";
        while( (tmp = br.readLine()) != null ){
            buffer += tmp;
        }

        this.compute(buffer);
    }
    public JSONReader(String jsonAsString){
        this.compute(jsonAsString);
    }

    private void compute(String jsonAsString){

        //remove all space
        jsonAsString = jsonAsString.trim()
                .replaceAll("\"", "")
                .replaceAll("\n", "")
                .replaceAll(" ", "");

        //Let's check if it empty
        if(!jsonAsString.isEmpty()){
            char first = jsonAsString.charAt(0);
            if( first == '{' ){
                this.object = new JSONObject("", jsonAsString.substring(1, jsonAsString.length()-1));
            }else if(first == '['){
                this.object = new JSONArray("", jsonAsString.substring(1, jsonAsString.length()-1));
            }else{
                throw new RuntimeException("JSON file does not begin either with '[' or '{'");
            }
        }
    }

    /**
     * Since it is stored, either being object or array, as object, you have to
     * check its type by yourself
     *
     * @return the root element of the json file parsed
     */
    public Object getRoot() {
        return this.object;
    }

    /**
     * If you're sure that the json file's root element is an object, you can even
     * call this getter. Just make sure it hasn't returned null
     * @return JSONObject or null
     */
    public JSONObject getRootObject(){
        return this.object instanceof JSONObject ? (JSONObject) this.object : null;
    }

    /**
     * If you're sure that the json file's root element is an array, you can even
     * call this getter. Just make sure it hasn't returned null
     * @return JSONArray or null
     */
    public JSONArray getRootArray(){
        return this.object instanceof JSONArray ? (JSONArray) this.object : null;
    }

    /**
     * @return if the root element is an object
     */
    public boolean isRootObject(){
        return this.object instanceof JSONObject;
    }

    /**
     * @return if the root element is an array
     */
    public boolean isRootArray(){
        return this.object instanceof JSONArray;
    }

    /**
     * @return the root object/array. It doesn't mind what it is
     */
    public Object getObject() { return object; }
}
