/**
 * As already known, Android does not provide any method for querying MySQL without any API.
 * Thus this is an interface for communicating with the RESTFul server hosted on the same MySQL's server.
 *
 * It is gonna using HttpRequest
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud.HttpRest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRestConnection extends AsyncTask<String, Integer, String> {

    private TextView db_state;
    private URL url;
    private HttpURLConnection httpURLConnection;

    public HttpRestConnection(String IP, TextView db_state, Context context, String putParameter) {
        try {
            this.db_state = db_state;

            this.url = new URL("http://" + IP + ":80/Put.php?" + putParameter);
            this.db_state.setText("URL correct");

            this.httpURLConnection = (HttpURLConnection) this.url.openConnection();
            this.db_state.setText("Connected");

        } catch (MalformedURLException e) {
           db_state.setText("Malformed URL error");
            e.printStackTrace();
        } catch (IOException e) {
            db_state.setText("Connection error");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            InputStream is = this.httpURLConnection.getInputStream();
            byte[] bytes = new byte[256];
            is.read(bytes);

            return "Sent: " + new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return e.getMessage();
        }
    }


    @Override
    protected void onPostExecute(String s) {
        this.db_state.setText(s);
    }
}
