package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import java.io.InputStream;
import java.util.concurrent.Callable;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configureLoginButton();
        configureRegisterButton();
    }

    private void configureLoginButton() {
        Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String intentEmail = intent.getStringExtra("email");
                String intentPassword = intent.getStringExtra("password");
                EditText emailField = (EditText) findViewById(R.id.email);
                EditText passwordField = (EditText) findViewById(R.id.password);
                if (intentEmail != null) {
                    emailField.setText(intentEmail);
                }
                if (intentPassword != null) {
                    passwordField.setText(intentPassword);
                }

                // Get user's email and password from the EditText views
                final String inputEmail = emailField.getText().toString();
                final String inputPassword = passwordField.getText().toString();
                String urlService = getResources().getString(R.string.URL_login);

                // Validate user's input
                if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.empty_fields_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                //Check if user is already logged in
                //if (MainActivity.isUserLoggedIn) {
                //    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                //    return;
                //}

                // Execute login process in a background
                new AsyncTaskExecutor().execute(new Login(inputPassword, inputEmail, urlService, LoginActivity.this), new AsyncTaskExecutor.Callback<String>() {
                    @Override
                    public void onComplete(String result) {
                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                        if (result.equals(LoginActivity.this.getResources().getString(R.string.login_successfully))) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }


    private void configureRegisterButton()
    {
        Button registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
