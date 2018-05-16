package io.github.aqibsaeed.activityrecognition;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.math.BigDecimal;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final double gConst = 9.81;
    private final int N_SAMPLES = 50;
    private TextView breakprob;
    private TextView situpprob;
    private TextView burpeeprob;
    private TextView squatprob;
    private TextView setbreakprob;
    private TextView resultprob;
    private ActivityInference activityInference;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mOrientationAngles;
    private Sensor mGyroscope;
    private Sensor mGravity;
    private Sensor mGeomagnetic;
    //private float xGrav,yGrav,zGrav,xAcc,yAcc,zAcc,pitch,roll,yaw,xRot,yRot,zRot;
    private static List xGrav, yGrav, zGrav, xAcc, yAcc, zAcc, pitch, roll, yaw, xRot, yRot, zRot;
    private static List input_signal, input_collection;
    private int breakcount, situpcount, burpeecount, squatcount;
    private float[] mGrav;
    private float[] mGeo;
    private double p, r, y, xG, yG, zG, xA, yA, zA, xR, yR, zR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        breakprob = (TextView) findViewById(R.id.breakprob);
        situpprob = (TextView) findViewById(R.id.situpprob);
        burpeeprob = (TextView) findViewById(R.id.burpeeprob);
        squatprob = (TextView) findViewById(R.id.squatprob);
        setbreakprob = (TextView) findViewById(R.id.setbreakprob);
        // Create Sensor Manager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // AccelerometerSensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Orientation Sensor
        mOrientationAngles = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        // Gravity Sensor
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //Gyroscope Sensor
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGeomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // Register sensor Listener
        mSensorManager.registerListener(this, mAccelerometer, 100000, 100000);
        mSensorManager.registerListener(this, mOrientationAngles, 100000, 100000);
        mSensorManager.registerListener(this, mGravity, 100000, 100000);
        mSensorManager.registerListener(this, mGyroscope, 100000, 100000);
        mSensorManager.registerListener(this, mGeomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        input_signal = new ArrayList();
        xGrav = new ArrayList();
        yGrav = new ArrayList();
        zGrav = new ArrayList();
        xAcc = new ArrayList();
        yAcc = new ArrayList();
        zAcc = new ArrayList();
        pitch = new ArrayList();
        roll = new ArrayList();
        yaw = new ArrayList();
        xRot = new ArrayList();
        yRot = new ArrayList();
        zRot = new ArrayList();
        input_collection = new ArrayList<>();
        breakcount = 0;
        situpcount = 0;
        burpeecount = 0;
        squatcount = 0;
        xGrav.clear();
        yGrav.clear();
        zGrav.clear();
        xAcc.clear();
        yAcc.clear();
        zAcc.clear();
        pitch.clear();
        roll.clear();
        yaw.clear();
        xRot.clear();
        yRot.clear();
        zRot.clear();
// TEST MODEL





        input_signal.clear();
        activityInference = new ActivityInference(getApplicationContext());




    }

    private float[] toFloatArray(List<Float> list) {
        int i = 0;
        float[] array = new float[list.size()];

        for (Float f : list) {
            array[i++] = (f != null ? f : Float.NaN);
        }
        return array;
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, 100000, 100000);
        mSensorManager.registerListener(this, mOrientationAngles, 100000, 100000);
        mSensorManager.registerListener(this, mGravity, 100000, 100000);
        mSensorManager.registerListener(this, mGyroscope, 100000, 100000);
        mSensorManager.registerListener(this, mGeomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //if (j < 40) {
        /*xGrav.add((float)0.27);
        yGrav.add((float)-0.96);
        zGrav.add((float)0.06);
        xAcc.add((float)0.04);
        yAcc.add((float)0.03);
        zAcc.add((float)-0.06);
        pitch.add((float)1.29);
        roll.add((float)1.79);
        yaw.add((float)2.26);
        xRot.add((float)0.16);
        yRot.add((float)-0.76);
        zRot.add((float)-0.29);
*/
        activityPrediction();
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
           /* xG=event.values[0]/gConst;
            yG=event.values[1]/gConst;
            zG=event.values[2]/gConst;
            float xG1=(float)xG;
            float yG1=(float)yG;
            float zG1=(float)zG;*/
            xGrav.add(event.values[0]);
            yGrav.add(event.values[1]);
            zGrav.add(event.values[2]);
            mGrav = event.values;

        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            /*xA=event.values[0]/gConst;
            yA=event.values[1]/gConst;
            zA=event.values[2]/gConst;
            float xA1=(float)xA;
            float yA1=(float)yA;
            float zA1=(float)zA;*/
            xAcc.add(event.values[0]);
            yAcc.add(event.values[1]);
            zAcc.add(event.values[2]);

        }
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            /* p = event.values[1]*0.0174533;
             r = event.values[2]*0.0174533;
             y = event.values[0]*0.0174533;
             float p1 = (float)p;
             float r1 = (float)r;
             float y1 = (float)y;*/


            pitch.add(event.values[1]);
            roll.add(event.values[2]);
            yaw.add(event.values[0]);

        }

/*

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeo = event.values;
        }
        if (mGrav != null && mGeo != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGrav, mGeo);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);   //azimut,pitch,roll
                pitch.add(orientation[1]);
                roll.add(orientation[2]);
                yaw.add(orientation[0]);
            }
        }
*/
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            /* xR=event.values[0]/gConst;
             yR=event.values[1]/gConst;
             zR=event.values[2]/gConst;
             float xR1=(float)xR;
            float yR1=(float)yR;
            float zR1=(float)zR;*/
            xRot.add(event.values[0]);
            yRot.add(event.values[1]);
            zRot.add(event.values[2]);

        }

    }




    private void activityPrediction() {
        if (xGrav.size() == N_SAMPLES && yGrav.size() == N_SAMPLES && zGrav.size() == N_SAMPLES && xAcc.size() == N_SAMPLES && yAcc.size() == N_SAMPLES && zAcc.size() == N_SAMPLES && pitch.size() == N_SAMPLES && roll.size() == N_SAMPLES && yaw.size() == N_SAMPLES && xRot.size() == N_SAMPLES && yRot.size() == N_SAMPLES && zRot.size() == N_SAMPLES) {
            // Mean normalize the signal
            //normalize();

            // Copy all x,y and z values to one array of shape N_SAMPLES*3
            input_signal.addAll(xGrav);
            input_signal.addAll(yGrav);
            input_signal.addAll(zGrav);
            input_signal.addAll(xAcc);
            input_signal.addAll(yAcc);
            input_signal.addAll(zAcc);
            input_signal.addAll(pitch);
            input_signal.addAll(roll);
            input_signal.addAll(yaw);
            input_signal.addAll(xRot);
            input_signal.addAll(yRot);
            input_signal.addAll(zRot);

            // Perform inference using Tensorflow
            float[] results = activityInference.getActivityProb(toFloatArray(input_signal));
            System.out.println("result");
            System.out.println(results[0]);
            System.out.println(results[1]);
            System.out.println(results[2]);
            System.out.println(results[3]);
            System.out.println(results[4]);

            breakprob.setText(Float.toString(round(results[0], 2)));
            situpprob.setText(Float.toString(round(results[1], 2)));
            burpeeprob.setText(Float.toString(round(results[2], 2)));
            squatprob.setText(Float.toString(round(results[3], 2)));
            setbreakprob.setText(Float.toString(round(results[4], 2)));
            int max = 0;
            for (int i = 0; i < 5; i++) {
                if (results[i] > results[max]) {
                    max = i;
                }

            }
            if (max == 0) {
                breakcount++;
                breakprob.setText(Integer.toString(breakcount));
            }
            if (max == 2) {
                situpcount++;
                situpprob.setText(Integer.toString(situpcount));
            }
            if (max == 1) {
                burpeecount++;
                burpeeprob.setText(Integer.toString(burpeecount));
            }
            if (max == 3) {
                squatcount++;
                squatprob.setText(Integer.toString(squatcount));
            }
            // Clear all the values
            xGrav.clear();
            yGrav.clear();
            zGrav.clear();
            xAcc.clear();
            yAcc.clear();
            zAcc.clear();
            pitch.clear();
            roll.clear();
            yaw.clear();
            xRot.clear();
            yRot.clear();
            zRot.clear();
            input_signal.clear();
        }
    }/*
    }

    private float[] toFloatArray(List<Float> list) {
        int i = 0;
        float[] array = new float[list.size()];

        for (Float f : list) {
            array[i++] = (f != null ? f : Float.NaN);
        }
        return array;
    }

    /*private void normalize()
    {
        float x_m = 0.662868f; float y_m = 7.255639f; float z_m = 0.411062f;
        float x_s = 6.849058f; float y_s = 6.746204f; float z_s = 4.754109f;

        for(int i = 0; i < N_SAMPLES; i++)
        {
            x.set(i,((x.get(i) - x_m)/x_s));
            y.set(i,((y.get(i) - y_m)/y_s));
            z.set(i,((z.get(i) - z_m)/z_s));
        }
    }
*/
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
