package com.greenhouse.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class JSONWriter {

    private FileOutputStream fos;


    public JSONWriter(String path) throws IOException {this(new File(path));}
    public JSONWriter(File file) throws IOException { this(new FileOutputStream(file));}
    public JSONWriter(FileOutputStream fos) throws IOException {
        this.fos = fos;
    }

    public boolean write(JSONObject mainObject){
        try {
            this.fos.write(mainObject.toString().getBytes());
            this.fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean close(){
        try {
            this.fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
