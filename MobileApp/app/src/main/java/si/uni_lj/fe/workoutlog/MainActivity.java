package si.uni_lj.fe.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static boolean isUserLoggedIn;
    static int didUserLogIn;
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isUserLoggedIn){
            Intent homeActivityIntent = new Intent(this, HomeActivity.class);
            startActivity(homeActivityIntent);
        }
        else {
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        }


    }


    // Functions

}