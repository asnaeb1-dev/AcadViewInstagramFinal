package com.abhigyan.user.instagramcloneacadview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    EditText signupUsername, signupPassword, signupEmail, signupPhoneNum, otpText;
    Spinner spinner;
    TextView sendOtp;
    Button signUpButton,clearButton;
    ConstraintLayout constraintLayout;
    ProgressBar progressBarx;



    int random, flag=0;
    int otpCheck=0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            flag=1;

        }
        else
            {
               askPermission();
            }
    }

    }

    public void registerFunction(View view)
    {
        if(random == Integer.parseInt(otpText.getText().toString()))
        {
            progressBarx.setVisibility(View.VISIBLE);
            final ParseUser user = new ParseUser();
            user.setUsername(signupUsername.getText().toString());
            user.setPassword(signupPassword.getText().toString());
            user.setEmail(signupEmail.getText().toString());
            user.put("phonenos", signupPhoneNum.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if(e==null)
                    {
                        Toast.makeText(RegisterActivity.this, "Registration Success!", Toast.LENGTH_SHORT).show();
                        progressBarx.setVisibility(View.INVISIBLE);
                        Intent goBack = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goBack);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
                    }
                    else
                    {
                        Log.i("error", e.getMessage());
                        Toast.makeText(RegisterActivity.this, "Registration FAILED!", Toast.LENGTH_SHORT).show();                }
                     }
            });
        }
        else
        {
            Toast.makeText(this, "Otp entered is wrong! Try again!", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearFunction(View view)
    {
        if(/*when all clear*/ otpCheck==0) {
            signupUsername.setText(null);
            signupPassword.setText(null);
            signupEmail.setText(null);
            signupPhoneNum.setText(null);
        }
        else if(otpCheck==1)
        {//when in otp fill up state
            signupUsername.setText(null);
            signupPassword.setText(null);
            signupEmail.setText(null);
            signupPhoneNum.setText(null);
            otpText.setText(null);
            spinner.animate().translationXBy(2000f).setDuration(1000);
            signupPhoneNum.animate().translationXBy(2000f).setDuration(1000);
            otpText.animate().translationXBy(1000f).setDuration(1000);
            sendOtp.animate().translationXBy(-1000f).setDuration(1000);
            otpCheck=0;
        }
    }
    public void sendOTP(View view)
    {//send otp as text message

        if(flag==1) {
            String message = "Your OTP is " + String.valueOf(random) + ". Don't share it with anyone!";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage((signupPhoneNum.getText().toString()), null, message, null, null);
            sendOtp.animate().translationXBy(1000f).setDuration(1000);
            spinner.animate().translationXBy(-2000f).setDuration(1000);
            signupPhoneNum.animate().translationXBy(-2000f).setDuration(1000);
            otpText.animate().translationXBy(-1000f).setDuration(1000);
            signUpButton.setEnabled(true);
            otpCheck=1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        constraintLayout = findViewById(R.id.constraintLayout);
        progressBarx = findViewById(R.id.progressBar2);
        progressBarx.setVisibility(View.INVISIBLE);

        signupUsername = findViewById(R.id.signupUsername);
        signupPassword = findViewById(R.id.signUpPassword);
        signupEmail = findViewById(R.id.signUpEmail);
        signupPhoneNum = findViewById(R.id.phoneNumberText);
        clearButton = findViewById(R.id.clearButton);
        otpText = findViewById(R.id.otpText);
        otpText.setTranslationX(1000f);

        sendOtp = findViewById(R.id.sendOtp);
        sendOtp.setVisibility(View.INVISIBLE);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setEnabled(false);

        Random rand = new Random();
        random = rand.nextInt((9999-1000)+1);

        spinner = findViewById(R.id.countryCodeSpinner);
        ArrayList<String> countryIDs = new ArrayList<>();
        countryIDs.add("93");
        countryIDs.add("+91");
        countryIDs.add("+61");
        countryIDs.add("+1");
        countryIDs.add("+880");
        countryIDs.add("+345");
        countryIDs.add("+54");
        countryIDs.add("+374");
        countryIDs.add("+86");
        countryIDs.add("+57");
        countryIDs.add("+385");
        ArrayAdapter callListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,countryIDs);
        spinner.setAdapter(callListAdapter);

        signupPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sendOtp.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(signupPhoneNum.getText().equals(""))
                {
                    sendOtp.setVisibility(View.INVISIBLE);
                }
                else
                {
                    sendOtp.setVisibility(View.VISIBLE);
                    signUpButton.setEnabled(false);
                }
            }
        });
        askPermission();

    }

    public void askPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},1);
        }
        else
        {
            flag=1;
        }
    }

}
