package si.uni_lj.fe.workoutlog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class FitnessActivity extends AppCompatActivity {
    private int counter = 1;
    private ArrayList<String> sets = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        EditText dateEditText = findViewById(R.id.date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sets);
        ListView setsListView = findViewById(R.id.sets);
        setsListView.setAdapter(adapter);

        final EditText kgEditText = findViewById(R.id.kg);
        final EditText repsEditText = findViewById(R.id.reps);

        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kg = kgEditText.getText().toString();
                String reps = repsEditText.getText().toString();
                String set = counter + ": " + kg + " kg " + reps + " reps";
                sets.add(set);
                adapter.notifyDataSetChanged();
                counter++;
            }
        });
        configureSaveButton();
        configureNewButton();
    }

    // Show a dialog 3 number pickers for year, month and day
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    EditText dateEditText = findViewById(R.id.date);
                    dateEditText.setText(year + "-" + (monthOfYear + 1) + "-" +dayOfMonth);
                }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();

    }

    private void configureSaveButton() {
        Button saveButton = (Button) findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dateField = (EditText) findViewById(R.id.date);
                EditText exerciseField = (EditText) findViewById(R.id.exercise);

                String token = MainActivity.token;
                final String date = dateField.getText().toString();
                final String exercise = exerciseField.getText().toString();
                final String setsString = String.join(", ", sets);
                String urlService = getResources().getString(R.string.URL_fitness);

                // Validate user's input
                if (date.isEmpty() || exercise.isEmpty() || setsString.isEmpty()) {
                    Toast.makeText(FitnessActivity.this, R.string.empty_fields_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTaskExecutor().execute(new FitnessAPIPost(token, date, exercise, setsString, urlService, FitnessActivity.this), new AsyncTaskExecutor.Callback<String>() {
                    @Override
                    public void onComplete(String result) {
                        Toast.makeText(FitnessActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void configureNewButton() {
        Button newButton = (Button) findViewById(R.id.btn_new_exercise);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText exerciseField = (EditText) findViewById(R.id.exercise);
                EditText kgField = (EditText) findViewById(R.id.kg);
                EditText repsField = (EditText) findViewById(R.id.reps);

                exerciseField.setText("");
                kgField.setText("");
                repsField.setText("");
                sets.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
