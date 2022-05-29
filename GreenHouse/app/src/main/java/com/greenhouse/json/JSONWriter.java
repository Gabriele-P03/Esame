package com.greenhouse.json;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * This is the class whose instance provides writing of json
 *
 * @author Gabriele-P03
 */

public class JSONWriter {

    private Object object;
    private OutputStream os;

    public JSONWriter(String jsonAsString, OutputStream os){
        this(new JSONReader(jsonAsString).getObject(), os);
    }

    public JSONWriter(Object object, OutputStream os) {
        this.object = object;
        this.os = os;
    }

    public void write(){

        if(this.object != null){
            try {

                this.os.write(this.object.toString().getBytes());
                this.os.flush();
                this.os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
