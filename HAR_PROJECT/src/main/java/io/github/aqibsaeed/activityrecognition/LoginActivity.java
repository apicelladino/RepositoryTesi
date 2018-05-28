package io.github.aqibsaeed.activityrecognition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    public static String LOGIN_MESSAGE = "Login";
    private DatabaseReference mDatabase;

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
                if((editUser.length()==0)&&(editPass.length()==0)) {
                }else{
                    SharedPreferences sp=getSharedPreferences("Login", 0);
                    SharedPreferences.Editor Ed=sp.edit();
                    String user = editUser.getText().toString();
                    Ed.putString("User",editUser.getText().toString());
                    Ed.putString("Pass",editPass.getText().toString());
                    Ed.commit();
                    mDatabase = FirebaseDatabase.getInstance().getReference(user);
                    mDatabase.child("Situp").setValue(0);
                    mDatabase.child("Burpee").setValue(0);
                    mDatabase.child("Squat").setValue(0);





                    Intent intent = new Intent(LoginActivity.this,PredictActivity.class);
                    intent.putExtra(LOGIN_MESSAGE,"Log on");
                    startActivity(intent);
                    finish();

                }
            }
        });
    }
}
