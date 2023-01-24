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

public class FitnessAPIPost implements Callable<String> {
    private final String urlService, token, date, exercise, sets;
    private final Activity callerActivity;

    public FitnessAPIPost(String token, String date, String exercise, String sets, String urlService, Activity callerActivity) {
        this.date = String.valueOf(date);
        this.exercise = String.valueOf(exercise);
        this.sets = String.valueOf(sets);
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
                int responseCode = connect(token, date, exercise, sets);

                if (responseCode == 201) {
                    return callerActivity.getResources().getString(R.string.saved_successfully);
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

    private int connect(String token, String date, String exercise, String sets) throws IOException {
        // Create the correct url
        URL url = new URL(urlService + "/" + token);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        try {
            JSONObject json = new JSONObject();
            json.put("datum", date);
            json.put("vaja", exercise);
            json.put("seti", sets);

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


