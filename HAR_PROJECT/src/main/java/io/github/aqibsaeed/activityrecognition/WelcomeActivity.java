package io.github.aqibsaeed.activityrecognition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    public static String WELCOME_MESSAGE = "Welcome on board";
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp=getSharedPreferences("Login",0);
                String user = sp.getString("User","");
                String pass = sp.getString("Pass","");
                System.out.println(user);
                if((user=="Dino")&&(pass =="ciao")){
                    Intent intent = new Intent(WelcomeActivity.this,PredictActivity.class);
                    startActivity(intent);
                    finish();
                }else {


                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }}
        }, 5000);

    }

}

