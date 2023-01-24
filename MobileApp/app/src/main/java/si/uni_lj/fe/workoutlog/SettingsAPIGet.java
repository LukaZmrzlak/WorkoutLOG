package si.uni_lj.fe.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class SettingsAPIGet implements Callable<String> {
    private final String urlService, token;
    private final Activity callerActivity;

    public SettingsAPIGet(String token, String urlService, Activity callerActivity) {
        this.urlService = String.valueOf(urlService);
        this.callerActivity = callerActivity;
        this.token = token;
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
                int responseCodeGet = connect(token);

                if (responseCodeGet == 201) {
                    return callerActivity.getResources().getString(R.string.fetch_successfully);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return callerActivity.getResources().getString(R.string.service_error);
            }
        } else {
            return callerActivity.getResources().getString(R.string.network_error);
        }
        return callerActivity.getResources().getString(R.string.unexpected_error);
    }

    private int connect(String token) throws IOException {
        // Create the correct url
        URL url = new URL(urlService +"/" + token);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        try {
            conn.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            String returnJson = convertStreamToString(inputStream);
            try {
                // Parse the JSON response
                JSONObject jsonObj = new JSONObject(returnJson);
                SettingsActivity.genderTemp = jsonObj.get("spol").toString();
                SettingsActivity.ageTemp = jsonObj.get("starost").toString();
                SettingsActivity.weightTemp = jsonObj.get("teza").toString();
                SettingsActivity.heightTemp = jsonObj.get("visina").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return conn.getResponseCode();
    }

    private static String convertStreamToString(InputStream is) {
        // Create a BufferedReader to read the input stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // Create a StringBuilder to store the read input
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            // Read each line of the input stream
            while ((line = reader.readLine()) != null) {
                // Append the line to the StringBuilder
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            // Print the stack trace if an IOException occurs
            e.printStackTrace();
        } finally {
            try {
                // Close the input stream
                is.close();
            } catch (IOException e) {
                // Print the stack trace if an IOException occurs
                e.printStackTrace();
            }
        }
        // Return the string representation of the StringBuilder
        return sb.toString();
    }
}
