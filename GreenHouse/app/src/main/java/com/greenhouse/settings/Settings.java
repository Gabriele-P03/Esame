package com.greenhouse.settings;

import android.content.Context;
import com.greenhouse.json.JSONMap;
import com.greenhouse.json.JSONObject;
import com.greenhouse.json.JSONReader;
import com.greenhouse.json.JSONWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    public static void loadConf(Context context) throws Exception {
        file = new File(context.getFilesDir(), "conf.json");
        if(!file.exists()){
            file.createNewFile();
            saveConf(context, IP, port, username, password);
        }else{
            EncryptFunc encryptFunc = new EncryptFunc();
            JSONReader jsonReader = new JSONReader(new BufferedReader(new FileReader(file)));
            JSONObject mainObj = jsonReader.getRootObject();
            if(mainObj != null) {
                ArrayList<JSONMap> maps = mainObj.getMaps();
                IP = (maps.size() >= 1 ? maps.get(0).getValue() : "127.0.0.1");
                port = (maps.size() >= 2 ? maps.get(1).getValue() : "80");
                username = (maps.size() >= 3 ? encryptFunc.decrypt(maps.get(2).getValue()) : "root");
                password = (maps.size() >= 4 ? encryptFunc.decrypt(maps.get(3).getValue()) : "root");
                rememberLogin = (maps.size() >= 5 ? maps.get(4).getValue().equals("true") : false);
            }
        }
    }

    public static void saveConf(Context context, String addr, String prt, String usr, String psw) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
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

        /*
            Here it does not need to encrypt username and password.
            It is done by LoginActivity once user try to log-in
         */
        JSONObject mainObj = new JSONObject("", "");
        mainObj.getMaps().add(new JSONMap("ip", IP));
        mainObj.getMaps().add(new JSONMap("port", port));
        mainObj.getMaps().add(new JSONMap("username", (rememberLogin ? username : "")));
        mainObj.getMaps().add(new JSONMap("password", (rememberLogin ? password : "")));
        mainObj.getMaps().add(new JSONMap("rememberLogin", String.valueOf(rememberLogin)));

        new JSONWriter(mainObj, new FileOutputStream(file)).write();
    }

    public static void saveConf(Context context) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        saveConf(context, IP, port, username, password);
    }
}
