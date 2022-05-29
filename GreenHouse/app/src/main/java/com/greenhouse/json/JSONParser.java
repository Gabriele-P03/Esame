package com.greenhouse.json;

class JSONParser {

    /**
     * @param jsonAsString
     * @return index of the end of json object passed as string
     */
    public static int getIndexOfEndObject(String jsonAsString){

        int c = 0;
        for(int i = 0; i < jsonAsString.length(); i++){
            char buffer = jsonAsString.charAt(i);
            if( buffer == '}')
                c--;
            else if(buffer == '{')
                c++;

            if(c < 0)
                return i;
        }

        throw new RuntimeException("Could not found a curly bracket for ending the object");
    }

    /**
     * @param jsonAsString
     * @return index of the end of json object passed as string
     */
    public static int getIndexOfEndArray(String jsonAsString){

        int c = 0;
        for(int i = 0; i < jsonAsString.length(); i++){
            char buffer = jsonAsString.charAt(i);
            if( buffer == ']')
                c--;
            else if(buffer == '[')
                c++;

            if(c < 0)
                return i;
        }
        throw new RuntimeException("Could not found a square bracket for ending the array");
    }

    public static int getIndexOfEndMap(String jsonAsString){
        int index = jsonAsString.indexOf(",");
        return index > -1 ? index : jsonAsString.length();
    }
}
