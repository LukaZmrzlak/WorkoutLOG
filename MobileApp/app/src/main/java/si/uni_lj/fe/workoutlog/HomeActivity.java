package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        configureLogoutImageButton();
        configureCardioButton();
        configureFitnessButton();
        configureSettingsImageButton();
    }

    private void configureFitnessButton()
    {
        Button FitnessButton = (Button) findViewById(R.id.fitness);
        FitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, FitnessActivity.class));
            }
        });
    }

    private void configureCardioButton()
    {
        Button CardioButton = (Button) findViewById(R.id.cardio);
        CardioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, CardioActivity.class));
            }
        });
    }

    private void configureSettingsImageButton()
    {
        ImageButton SettingsButton = (ImageButton) findViewById(R.id.settings);
        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });
    }

    private void configureLogoutImageButton()
    {
        ImageButton LogoutButton = (ImageButton) findViewById(R.id.logout);
        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }
}