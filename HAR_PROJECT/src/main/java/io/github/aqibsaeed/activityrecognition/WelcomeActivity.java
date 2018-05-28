package io.github.aqibsaeed.activityrecognition;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    public static String WELCOME_MESSAGE = "Welcome on board";
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        TextView textView = (TextView) findViewById(R.id.welcomeView);
        SharedPreferences sp=getSharedPreferences("Login",0);
        String user = sp.getString("User","");
        textView.setText(user.toString());

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                SharedPreferences sp=getSharedPreferences("Login",0);
                String user = sp.getString("User","");
                String pass = sp.getString("Pass","");
                System.out.println(user);

                if((user.length()==0)&&(pass.length() ==0)){
                    Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else {

                Intent intent = new Intent(WelcomeActivity.this,PredictActivity.class);
                startActivity(intent);
                finish();
            }}
        }, 5000);

    }

}

