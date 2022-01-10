/**
 * This is a simple abstraction for creating a connection with a MySQL DB-based
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class RESTFulConnection {

    protected HttpURLConnection HttpCon;
    protected boolean isConnected = false;

    public RESTFulConnection(String URL) {
        try {
            this.HttpCon = (HttpURLConnection) new URL(URL).openConnection();
            this.isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }
}
