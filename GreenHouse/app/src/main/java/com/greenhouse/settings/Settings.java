package com.greenhouse.settings;

import android.content.Context;
import com.greenhouse.json.JSONMap;
import com.greenhouse.json.JSONObject;
import com.greenhouse.json.JSONReader;
import com.greenhouse.json.JSONWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Here are stored as static fields configuration about IP, Username and Password
 * for connecting to MySQL database. Notice that even here are contained these credentials,
 * they'll be encrypted before have been sent to RESTFul Server, in order to prevent any hacking
 *
 * Encrypting algorithm is not saved in repository
 *
 * If someone even tries to send credentials as not-encrypted, they'll not be taken as valid due
 * to decryption algorithm which will cause a strange sorting result
 *
 * @author Gabriele-P03
 */

public class Settings {

    private static File file;
    public static String IP = "", port = "", username = "", password = "";

    public static void loadConf(Context context) throws IOException {
        file = new File(context.getFilesDir(), "conf.json");
        if(!file.exists()){
            file.createNewFile();
            saveConf(context, IP, port, username, password);
        }else{
            JSONReader jsonReader = new JSONReader(file);
            JSONObject mainObj = jsonReader.getMainObject();
            if(mainObj != null) {
                ArrayList<JSONMap> maps = mainObj.getMaps();
                if (maps.size() == 4) {
                    IP = maps.get(0).getValue();
                    port = maps.get(1).getValue();
                    username = maps.get(2).getValue();
                    password = maps.get(3).getValue();
                } else {
                    throw new RuntimeException("Error configuration file composition...");
                }
            }
        }
    }

    public static void saveConf(Context context, String addr, String prt, String usr, String psw) throws IOException {
        IP = addr;
        port = prt;
        username = usr;
        password = psw;

        //Checking IP
        String[] IPs = IP.split("\\.");
        if(IPs.length == 4){
            for(String ip : IPs){
                try{
                    Integer.parseInt(ip);
                }catch (Exception e){
                    return;
                }
            }
        }else{
            return;
        }

        //Checking port
        try{
            Integer.parseInt(port);
        }catch (Exception e){
            return;
        }

        //Checking username
        if(username.length() <= 0)
            return;

        //Checking password
        if(password.length() <= 0)
            return;

        file = new File(context.getFilesDir(), "conf.json");
        if(!file.exists()){
            file.createNewFile();
        }

        JSONWriter jsonWriter = new JSONWriter(file);
        JSONObject mainObj = new JSONObject("");
        mainObj.addMap(new JSONMap("ip", IP).toString());
        mainObj.addMap(new JSONMap("port", port).toString());
        mainObj.addMap(new JSONMap("username", username).toString());
        mainObj.addMap(new JSONMap("password", password).toString());
        jsonWriter.write(mainObj);

        jsonWriter.close();
    }
}
