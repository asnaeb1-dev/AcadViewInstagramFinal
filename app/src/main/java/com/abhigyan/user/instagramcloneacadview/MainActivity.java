package com.abhigyan.user.instagramcloneacadview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/*NAME- ABHIGYAN RAHA
  COLLEGE- ITER, BBSR
  BATCH- SUM18 AND ON1
  BRANCH- CSE(COMPUTER SCIENCE AND ENGINEERING)
  DATE- 28TH MAY
 */
public class MainActivity extends AppCompatActivity {

    EditText loginPasswordText, loginUsernameText;
    ProgressBar progressBar;
    Button loginButton, signUpButton;

    public void loginFunction(View view)
    {// function for making the login button work
        progressBar.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(loginUsernameText.getText().toString(), loginPasswordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if( e==null)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "WELCOME!", Toast.LENGTH_SHORT).show();
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeActivity);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Username/Password Incorrect! Try again!", Toast.LENGTH_SHORT).show();
                    loginUsernameText.setText(null);
                    loginPasswordText.setText(null);
                }
            }
        });
    }
    public void signupFunction(View view)
    {// function for making the signup button work
        Intent signupintent = new Intent(this, RegisterActivity.class);
        startActivity(signupintent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    public void forgotFunction(View view)
    {// function for making the forgotpassword button work

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // establishing a network with the 'back4app' parse server

        loginButton= findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        loginUsernameText = findViewById(R.id.loginUsernameText);//find the usernametextview
        loginPasswordText = findViewById(R.id.loginPasswordText);//find the passwordtextview
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        if(isNetworkAvailable()==true) {
            //check if internet connection is availaible
            if (ParseUser.getCurrentUser() != null) {
                Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }
        else
        {
            loginButton.setEnabled(false);
            signUpButton.setEnabled(false);
            Toast.makeText(this, "Sorry Internet connection is not availaible! Try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        //method to check internet connection
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
