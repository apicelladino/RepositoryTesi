package io.github.aqibsaeed.activityrecognition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    public static String LOGIN_MESSAGE = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText editUser = (EditText) findViewById(R.id.editUser);
        final EditText editPass = (EditText) findViewById(R.id.editPass);
        final Button button = (Button) findViewById(R.id.buttonSignUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((editUser.getText() != null)&&(editPass.getText() != null)){
                    SharedPreferences sp=getSharedPreferences("Login", 0);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putString("User",editUser.getText().toString());
                    Ed.putString("Pass",editPass.getText().toString());
                    Ed.commit();
                    Intent intent = new Intent(LoginActivity.this,PredictActivity.class);
                    intent.putExtra(LOGIN_MESSAGE,"Log on");
                    startActivity(intent);
                    finish();

                }
            }
        });
    }
}
