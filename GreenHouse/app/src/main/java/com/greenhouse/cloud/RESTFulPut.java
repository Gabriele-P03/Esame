package com.greenhouse.cloud;

import java.net.ProtocolException;

public class RESTFulPut extends RESTFulConnection{

    private static final String PATH_PUT_PHP = "Put.php";

    public RESTFulPut(String IP) {
        super(IP + "/" + PATH_PUT_PHP);
    }

    public void put(){

        try {
            this.HttpCon.setRequestMethod("PUT");

        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

}
