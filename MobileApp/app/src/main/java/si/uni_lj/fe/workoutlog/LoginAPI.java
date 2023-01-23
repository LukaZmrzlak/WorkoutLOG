package si.uni_lj.fe.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

class Login implements Callable<String> {
    private final String email, password, urlService;
    private final Activity callerActivity;

    public Login(String password,String email, String urlService, Activity callerActivity) {
        this.email = String.valueOf(email);
        this.password = String.valueOf(password);
        this.urlService = String.valueOf(urlService);
        this.callerActivity = callerActivity;
    }

    @Override
    public String call() {
        ConnectivityManager connMgr = (ConnectivityManager) callerActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;

        try {
            networkInfo = connMgr.getActiveNetworkInfo();
        } catch (Exception e) {
            return callerActivity.getResources().getString(R.string.network_error);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                int responseCode = connect(email, password);

                if (responseCode == 200) {
                    MainActivity.didUserLogIn = 1;
                    return callerActivity.getResources().getString(R.string.login_successfully);
                }
                if (responseCode == 401) {
                    return callerActivity.getResources().getString(R.string.login_wrong_password);
                } else {
                    return callerActivity.getResources().getString(R.string.login_wrong_username);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return callerActivity.getResources().getString(R.string.service_error);
            }
        } else {
            return callerActivity.getResources().getString(R.string.network_error);
        }
    }


    private int connect(String email, String password) throws IOException {
        // Create the correct url
        URL url = new URL(urlService+email+"/"+password);

        // Open a connection to the URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);

        // Set the request method to GET
        conn.setRequestMethod("GET");

        // Set the accept header to application/json
        conn.setRequestProperty("Accept", "application/json");

        // Try to connect
        try {
            conn.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (conn.getResponseCode() == 200) {
            // If the response code is 200, get the input stream and convert it to a string
            InputStream inputStream = conn.getInputStream();
            String returnJson = convertStreamToString(inputStream);
            try {
                // Parse the JSON response
                JSONObject jsonObj = new JSONObject(returnJson);
                String status = jsonObj.get("LoginStatus").toString();

                // Get the token from the JSON response
                MainActivity.token = jsonObj.get("token").toString();

                // Check if the login was successful
                if (status.equals("Login successful!")) {
                    // If the login was successful, set the flag
                    MainActivity.didUserLogIn = 1;
                    return 200;
                } else {
                    return 401;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return 400;
            }
        } else {
            // If the response code is not 200, return the response code
            return conn.getResponseCode();
        }
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