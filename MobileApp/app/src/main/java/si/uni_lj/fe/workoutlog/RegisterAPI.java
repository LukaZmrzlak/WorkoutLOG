package si.uni_lj.fe.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class RegisterAPI implements Callable<String> {
    private final String username, password, name, surname, email, urlService;
    private final Activity callerActivity;

    public RegisterAPI(String username, String password, String name, String surname, String email, String urlService, Activity callerActivity) {
        this.username = String.valueOf(username);
        this.password = String.valueOf(password);
        this.name = String.valueOf(name);
        this.surname = String.valueOf(surname);
        this.email = String.valueOf(email);
        this.urlService = String.valueOf(urlService);
        this.callerActivity = callerActivity;
    }

    @Override
    public String call() throws Exception {
        ConnectivityManager connMgr = (ConnectivityManager) callerActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        try {
            networkInfo = connMgr.getActiveNetworkInfo();
        } catch (Exception e) {
            return callerActivity.getResources().getString(R.string.network_error);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                int responseCode = connect(username, password, name, surname, email);

                if (responseCode == 201) {
                    return callerActivity.getResources().getString(R.string.register_successfully);
                }
                if (responseCode == 409) {
                    return callerActivity.getResources().getString(R.string.register_username_taken);
                } else {
                    return callerActivity.getResources().getString(R.string.login_error) + " " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return callerActivity.getResources().getString(R.string.service_error);
            }
        } else {
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }

    private int connect(String username, String password, String name, String surname, String email) throws IOException {
        // Create the correct url
        URL url = new URL(urlService);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        try {
            JSONObject json = new JSONObject();
            json.put("vzdevek", username);
            json.put("geslo", password);
            json.put("ime", name);
            json.put("priimek", surname);
            json.put("email", email);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(json.toString());
            writer.flush();
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Connection is successful
        } else {
            // Connection failed
        }
        return responseCode;
    }

}
