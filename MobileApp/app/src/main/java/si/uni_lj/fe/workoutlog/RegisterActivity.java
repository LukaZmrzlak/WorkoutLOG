package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        configureLoginButton();
        configureRegisterButton();
    }

    private void configureLoginButton()
    {
        Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void configureRegisterButton() {
        Button registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameField = (EditText) findViewById(R.id.username);
                EditText passwordField = (EditText) findViewById(R.id.password);
                EditText nameField = (EditText) findViewById(R.id.name);
                EditText surnameField = (EditText) findViewById(R.id.surname);
                EditText emailField = (EditText) findViewById(R.id.email);

                final String username = usernameField.getText().toString();
                final String password = passwordField.getText().toString();
                final String name = nameField.getText().toString();
                final String surname = surnameField.getText().toString();
                final String email = emailField.getText().toString();
                String urlService = getResources().getString(R.string.URL_register);

                new AsyncTaskExecutor().execute(new RegisterAPI(username, password, name, surname, email, urlService, RegisterActivity.this), new AsyncTaskExecutor.Callback<String>() {
                    @Override
                    public void onComplete(String result) {
                        Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                        if (result.equals(RegisterActivity.this.getResources().getString(R.string.register_successfully))) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }


}

