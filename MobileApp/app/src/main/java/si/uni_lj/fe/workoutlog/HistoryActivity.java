package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    public static String outputTemp;
    private ArrayList<String> history = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        EditText dateEditText = findViewById(R.id.date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
        ListView setsListView = findViewById(R.id.history);
        setsListView.setAdapter(adapter);

        findViewById(R.id.btn_cardio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String date = dateEditText.getText().toString();
                String token = MainActivity.token;
                String urlService = getResources().getString(R.string.URL_cardio_get);

                if(date.isEmpty()){
                    Toast.makeText(HistoryActivity.this, R.string.select_date, Toast.LENGTH_SHORT).show();
                }
                else {
                    new AsyncTaskExecutor().execute(new CardioAPIGet(token, date, urlService, HistoryActivity.this), new AsyncTaskExecutor.Callback<String>() {
                        @Override
                        public void onComplete(String result) {
                            history.clear();
                            history.add(outputTemp);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        findViewById(R.id.btn_fitness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String date = dateEditText.getText().toString();
                String token = MainActivity.token;
                String urlService = getResources().getString(R.string.URL_fitness_get);

                if(date.isEmpty()){
                    Toast.makeText(HistoryActivity.this, R.string.select_date, Toast.LENGTH_SHORT).show();
                }
                else {
                    new AsyncTaskExecutor().execute(new FitnessAPIGet(token, date, urlService, HistoryActivity.this), new AsyncTaskExecutor.Callback<String>() {
                        @Override
                        public void onComplete(String result) {
                            history.clear();
                            history.add(outputTemp);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
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
}