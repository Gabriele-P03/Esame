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
 * Password will be saved already encrypted and loaded thus.
 * Once saved a new password the edit text, even if it has a type-password text, will be changed
 * with the encrypted one. In this way a raw-password will be present in application only
 * once written and before been saved
 *
 * @author Gabriele-P03
 */

public class Settings {

    private static File file;
    public static String IP = "", port = "", username = "", password = "";
    public static boolean rememberLogin;

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
                IP = (maps.size() >= 1 ? maps.get(0).getValue() : "127.0.0.1");
                port = (maps.size() >= 2 ? maps.get(1).getValue() : "80");
                username = (maps.size() >= 3 ? maps.get(2).getValue() : "root");
                password = (maps.size() >= 4 ? maps.get(3).getValue() : "root");
                rememberLogin = (maps.size() >= 5 ? maps.get(4).getValue().equals("true") : false);
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


        file = new File(context.getFilesDir(), "conf.json");
        if(!file.exists()){
            file.createNewFile();
        }

        JSONWriter jsonWriter = new JSONWriter(file);
        JSONObject mainObj = new JSONObject("");
        mainObj.addMap(new JSONMap("ip", IP));
        mainObj.addMap(new JSONMap("port", port));
        mainObj.addMap(new JSONMap("username", (rememberLogin ? username : "")));
        mainObj.addMap(new JSONMap("password", (rememberLogin ? EncryptFunc.encrypt(password) : "")));
        mainObj.addMap(new JSONMap("rememberLogin", String.valueOf(rememberLogin)));
        jsonWriter.write(mainObj);

        jsonWriter.close();
    }

    public static void saveConf(Context context) throws IOException {
        saveConf(context, IP, port, username, password);
    }
}
