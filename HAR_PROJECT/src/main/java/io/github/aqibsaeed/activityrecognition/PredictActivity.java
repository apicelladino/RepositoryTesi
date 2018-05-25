package io.github.aqibsaeed.activityrecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PredictActivity extends AppCompatActivity {
public static String START_MESSAGE = "Start_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

    }
    public void sendMessage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        FirebaseConnection connection = new FirebaseConnection();
        connection.writeDatabase();

        intent.putExtra(START_MESSAGE, "Start");
        startActivity(intent);
    }
}
