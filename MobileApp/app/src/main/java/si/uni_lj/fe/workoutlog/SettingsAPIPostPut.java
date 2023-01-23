package si.uni_lj.fe.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class SettingsAPIPostPut implements Callable<String> {
    private final String urlService, token;
    private final Activity callerActivity;
    private final String gender, age, weight, height;

    public SettingsAPIPostPut(String token, String gender, String age, String weight, String height, String urlService, Activity callerActivity) {
        this.gender = String.valueOf(gender);
        this.age = String.valueOf(age);
        this.weight = String.valueOf(weight);
        this.height = String.valueOf(height);
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
                int responseCodePost = connect(token, gender, age, weight, height, "POST");

                if (responseCodePost == 201) {
                    return callerActivity.getResources().getString(R.string.saved_successfully);
                }
                else if (responseCodePost == 500) {
                    int responseCodePut = connect(token, gender, age, weight, height, "PUT");
                    if (responseCodePut == 201) {
                        return callerActivity.getResources().getString(R.string.saved_successfully);
                    }
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

    private int connect(String token, String gender, String age, String weight, String height, String method) throws IOException {
        // Create the correct url
        URL url = new URL(urlService +"/" + token);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        try {
            JSONObject json = new JSONObject();
            json.put("spol", gender);
            json.put("starost", age);
            json.put("teza", weight);
            json.put("visina", height);

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
