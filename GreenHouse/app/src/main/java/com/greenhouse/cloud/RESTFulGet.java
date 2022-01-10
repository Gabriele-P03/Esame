package com.greenhouse.cloud;

public class RESTFulGet extends RESTFulConnection{

    private static final String PATH_GET_PHP = "Get.php";

    public RESTFulGet(String IP) {
        super(IP + "/" + PATH_GET_PHP);
    }
}
