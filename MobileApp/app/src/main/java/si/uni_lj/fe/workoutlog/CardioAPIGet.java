package si.uni_lj.fe.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class CardioAPIGet implements Callable<String> {
    private final String urlService, token, date;
    private final Activity callerActivity;

    public CardioAPIGet(String token, String date, String urlService, Activity callerActivity) {
        this.token = token;
        this.date = String.valueOf(date);
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
                return connect();

            } catch (IOException e) {
                e.printStackTrace();
                return callerActivity.getResources().getString(R.string.service_error);
            }
        } else {
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }

    private String connect() throws IOException {
        // Create the correct url
        URL url = new URL(urlService + "/" + token + "/" + date);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.connect();

        int response = conn.getResponseCode();

        InputStream inputStream = conn.getInputStream();
        String returnJson = convertStreamToString(inputStream);
        return JSONString2String(returnJson);
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

    public String JSONString2String(String JsonString){
        String Output = "";
        try {
            JSONArray jsonArray = new JSONArray(JsonString);
            for(int i=0; i<jsonArray.length(); i++){
                Output += (i+1) + ": ";
                Output += jsonArray.getJSONObject(i).getString("casovna_dolzina");
                Output += " | ";
                Output += jsonArray.getJSONObject(i).getString("dolzina");
                Output += " | ";
                Output += jsonArray.getJSONObject(i).getString("obcutek");
                Output += " | ";
                Output += jsonArray.getJSONObject(i).getString("vreme");
                Output += System.getProperty("line.separator");
                Output += " notes: ";
                Output += jsonArray.getJSONObject(i).getString("zapiski");
                Output += System.getProperty("line.separator");
                Output += System.getProperty("line.separator");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return HistoryActivity.outputTemp = Output;
    }
}
