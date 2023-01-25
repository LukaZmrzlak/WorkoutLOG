package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    public static String genderTemp, ageTemp, weightTemp, heightTemp;
    private EditText etGender, etAge, etWeight, etHeight;
    private TextView bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        genderTemp = "";
        ageTemp = "";
        weightTemp = "";
        heightTemp = "";

        etGender = findViewById(R.id.gender);
        etAge = findViewById(R.id.age);
        etWeight = findViewById(R.id.weight);
        etHeight = findViewById(R.id.height);
        bmi = findViewById(R.id.BMI);

        String token = MainActivity.token;
        String urlService = getResources().getString(R.string.URL_settings);
        SettingsAPIGet settingsAPIGet = new SettingsAPIGet(token,urlService, this);
        AsyncTaskExecutor executor = new AsyncTaskExecutor();
        executor.execute(settingsAPIGet, new AsyncTaskExecutor.Callback<String>() {
            @Override
            public void onComplete(String result) {
                etGender.setText(genderTemp);
                etAge.setText(ageTemp);
                etWeight.setText(weightTemp);
                etHeight.setText(heightTemp);
                String weight = etWeight.getText().toString();
                String height = etHeight.getText().toString();
                if(!weight.isEmpty() && !height.isEmpty()) {
                    double weightValue = Double.parseDouble(weight);
                    double heightValue = Double.parseDouble(height);
                    double bmiValue = weightValue / (0.01*heightValue*0.01*heightValue);
                    bmi.setText("BMI: " + bmiValue);
                }
            }
        });

        // Add a save button
        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = etGender.getText().toString();
                String age = etAge.getText().toString();
                String weight = etWeight.getText().toString();
                String height = etHeight.getText().toString();
                if(!weight.isEmpty() && !height.isEmpty()) {
                    double weightValue = Double.parseDouble(weight);
                    double heightValue = Double.parseDouble(height);
                    double bmiValue = weightValue / (0.01*heightValue*0.01*heightValue);
                    bmi.setText("BMI: " + bmiValue);
                }
                // Create new instance of SettingsAPIPostPut
                SettingsAPIPostPut settingsAPIPostPut = new SettingsAPIPostPut(token, gender, age, weight, height, urlService, SettingsActivity.this);
                AsyncTaskExecutor executor = new AsyncTaskExecutor();
                executor.execute(settingsAPIPostPut, new AsyncTaskExecutor.Callback<String>() {
                    @Override
                    public void onComplete(String result) {
                        Toast.makeText(SettingsActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

