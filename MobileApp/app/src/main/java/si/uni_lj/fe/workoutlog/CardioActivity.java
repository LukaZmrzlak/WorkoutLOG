package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CardioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio);

        EditText dateEditText = findViewById(R.id.date);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        EditText durationEditText = findViewById(R.id.duration);
        durationEditText.setInputType(InputType.TYPE_NULL);
        durationEditText.setOnClickListener(v -> showDurationPickerDialog());

        EditText distanceEditText = findViewById(R.id.distance);
        distanceEditText.setInputType(InputType.TYPE_NULL);
        distanceEditText.setOnClickListener(v -> showDistancePickerDialog());

        configureSaveButton();
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

    // Show a dialog with 3 number pickers for hours, minutes and seconds
    private void showDurationPickerDialog() {
        LinearLayout pickersLayout = new LinearLayout(this);
        pickersLayout.setOrientation(LinearLayout.HORIZONTAL);
        pickersLayout.setGravity(Gravity.CENTER);

        final NumberPicker hoursPicker = new NumberPicker(this);
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(99);
        hoursPicker.setWrapSelectorWheel(false);
        hoursPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        hoursPicker.setValue(0);
        pickersLayout.addView(hoursPicker);

        TextView hoursText = new TextView(this);
        hoursText.setText("h");
        pickersLayout.addView(hoursText);

        final NumberPicker minutesPicker = new NumberPicker(this);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setWrapSelectorWheel(false);
        minutesPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        minutesPicker.setValue(0);
        pickersLayout.addView(minutesPicker);

        TextView minutesText = new TextView(this);
        minutesText.setText("m");
        pickersLayout.addView(minutesText);

        final NumberPicker secondsPicker = new NumberPicker(this);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setWrapSelectorWheel(false);
        secondsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        secondsPicker.setValue(0);
        pickersLayout.addView(secondsPicker);

        TextView secondsText = new TextView(this);
        secondsText.setText("s");
        pickersLayout.addView(secondsText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Duration");
        builder.setView(pickersLayout);
        builder.setPositiveButton("OK", (dialog, which) -> {
            EditText durationEditText = findViewById(R.id.duration);
            durationEditText.setText(String.format("%02d:%02d:%02d", hoursPicker.getValue(), minutesPicker.getValue(), secondsPicker.getValue()));
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Show a dialog with 2 number pickers for kilometers and meters
    private void showDistancePickerDialog() {
        LinearLayout pickersLayout = new LinearLayout(this);
        pickersLayout.setOrientation(LinearLayout.HORIZONTAL);
        pickersLayout.setGravity(Gravity.CENTER);

        final NumberPicker kilometersPicker = new NumberPicker(this);
        kilometersPicker.setMinValue(0);
        kilometersPicker.setMaxValue(999);
        kilometersPicker.setWrapSelectorWheel(false);
        kilometersPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        kilometersPicker.setValue(0);
        pickersLayout.addView(kilometersPicker);

        TextView kilometersText = new TextView(this);
        kilometersText.setText("km");
        pickersLayout.addView(kilometersText);

        final NumberPicker metersPicker = new NumberPicker(this);
        metersPicker.setMinValue(0);
        metersPicker.setMaxValue(999);
        metersPicker.setWrapSelectorWheel(false);
        metersPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        metersPicker.setValue(0);
        pickersLayout.addView(metersPicker);

        TextView metersText = new TextView(this);
        metersText.setText("m");
        pickersLayout.addView(metersText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Distance");
        builder.setView(pickersLayout);
        builder.setPositiveButton("OK", (dialog, which) -> {
            EditText distanceEditText = findViewById(R.id.distance);
            distanceEditText.setText(kilometersPicker.getValue() + "km " + metersPicker.getValue() + "m");
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void configureSaveButton() {
        Button saveButton = (Button) findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dateField = (EditText) findViewById(R.id.date);
                EditText durationField = (EditText) findViewById(R.id.duration);
                EditText distanceField = (EditText) findViewById(R.id.distance);
                EditText experienceField = (EditText) findViewById(R.id.experience);
                EditText weatherField = (EditText) findViewById(R.id.weather);
                EditText notesField = (EditText) findViewById(R.id.notes);

                String token = MainActivity.token;
                final String date = dateField.getText().toString();
                final String duration = durationField.getText().toString();
                final String distance = distanceField.getText().toString();
                final String experience = experienceField.getText().toString();
                final String weather = weatherField.getText().toString();
                final String notes = notesField.getText().toString();
                String urlService = getResources().getString(R.string.URL_cardio);

                // Validate user's input
                if (date.isEmpty() || duration.isEmpty() || distance.isEmpty()) {
                    Toast.makeText(CardioActivity.this, R.string.empty_fields_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTaskExecutor().execute(new CardioAPIPost(token, date, duration, distance, experience, weather, notes, urlService, CardioActivity.this), new AsyncTaskExecutor.Callback<String>() {
                    @Override
                    public void onComplete(String result) {
                        Toast.makeText(CardioActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}