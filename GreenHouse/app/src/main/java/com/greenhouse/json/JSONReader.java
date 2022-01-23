package com.greenhouse.json;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * {@link JSONReader} provides json reading
 */

public class JSONReader {

    private FileInputStream fis;

    //This string represents json with \n and space
    private String jsonAsString = "";

    //JSON file always begins with curly brackets
    private JSONObject mainObject;

    public JSONReader(String jsonAsString){
        this.jsonAsString = jsonAsString;
        this.parse();
    }

    public JSONReader(File file) throws IOException { this(new FileInputStream(file));}
    public JSONReader(FileInputStream fis) throws IOException {
        this.fis = fis;
        this.readJSON();
    }

    /**
     * Once called, it read json file and save everything inside the mainObject
     * @return
     */
    private void readJSON() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(this.fis));
        String line = "";
        while ( (line = br.readLine()) != null){
            this.jsonAsString += line;
        }
        this.parse();
    }

    private void parse() {
        String fileAsString = String.copyValueOf(this.jsonAsString.toCharArray());
        if (fileAsString.length() > 1) {
            fileAsString = fileAsString
                    .substring(1, fileAsString.length() - 1)  //Remove first and last curly brackets
                    .replaceAll("\n", "")   //Remove \n
                    .replaceAll(" ", "")    //Remove space
                    .replaceAll("\"", "");  //Remove '"'


            this.mainObject = new JSONObject(fileAsString);
        }
    }

    public void close(){
        try {
            this.fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileInputStream getFis() { return fis; }

    public JSONObject getMainObject() {
        return mainObject;
    }

    public static boolean isValidChar(char c) {return isNotBracket(c) && isNotColon(c) && isNotComma(c);}
    public static boolean isNotColon(char c) {return c != ':';}
    public static boolean isNotComma(char c) {return c != ',';}
    public static boolean isNotBracket(char c){
        return c != '[' && c != ']' && c != '{' && c != '}';
    }
}
