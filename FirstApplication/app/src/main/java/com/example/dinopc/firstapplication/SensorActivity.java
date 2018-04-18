package com.example.dinopc.firstapplication;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.widget.TextView;


public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mOrientationAngles;
    private Sensor mGyroscope;
    private Sensor mGravity;
    private TextView xText,yText,zText,azimText,pitchText,rollText,xGravText,yGravText,zGravText,xRotationText,yRotationText,zRotationText;


    protected void onCreate(Bundle savedInstanceState){
        float[] orientation = new float[3];
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_sensor));
        // Create Sensor Manager
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // AccelerometerSensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Orientation Sensor
        mOrientationAngles= mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        // Gravity Sensor
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //Gyroscope Sensor
        mGyroscope= mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // Register sensor Listener
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mOrientationAngles,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mGravity,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mGyroscope,SensorManager.SENSOR_DELAY_NORMAL);
        // Assign TextView
        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);
        azimText = (TextView)findViewById(R.id.azimText);
        pitchText = (TextView)findViewById(R.id.pitchText);
        rollText = (TextView)findViewById(R.id.rollText);
        xGravText = (TextView)findViewById(R.id.xGravText);
        yGravText = (TextView)findViewById(R.id.yGravText);
        zGravText = (TextView)findViewById(R.id.zGravText);
        xRotationText = (TextView)findViewById(R.id.xRotationText);
        yRotationText = (TextView)findViewById(R.id.yRotationText);
        zRotationText = (TextView)findViewById(R.id.zRotationText);


    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
         float x,y,z;



        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xText.setText("X: " + event.values[0]);
            yText.setText("Y: " + event.values[1]);
            zText.setText("Z: " + event.values[2]);
        }
        /*
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            azimText.setText("azimuth: " + event.values[0]);
            pitchText.setText("pitch: " + event.values[1]);
            rollText.setText("roll: " + event.values[2]);
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
            x=(event.values[0]*(-1))/10;
            y=(event.values[1]*(-1))/10;
            z=(event.values[2]*(-1))/10;
            xGravText.setText("Gforce x :" + x);
            yGravText.setText("Gforce y :" + y);
            zGravText.setText("Gforce z :" + z);
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            xRotationText.setText("Rate_R x :"+ event.values[0]);
            yRotationText.setText("Rate_R y :"+ event.values[1]);
            zRotationText.setText("Rate_R z :"+ event.values[2]);

        }
    */}

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_sensor, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
