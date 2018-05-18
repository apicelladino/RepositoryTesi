package io.github.aqibsaeed.activityrecognition;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.math.BigDecimal;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.*;


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
    private static List<String> input_signal, input_collection;
    private int breakcount, situpcount, burpeecount, squatcount;
    private float[] mGrav;
    private float[] mGeo;
    private double p, r, y, xG, yG, zG, xA, yA, zA, xR, yR, zR;
    private boolean grav;
    private boolean acc;
    private boolean orient;
    private boolean rot;
    private Button resetprob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetprob = (Button) findViewById(R.id.resetprob);
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
      //  mSensorManager.registerListener(this, mAccelerometer, 100000, 100000);
       // mSensorManager.registerListener(this, mOrientationAngles, 100000, 100000);
       // mSensorManager.registerListener(this, mGravity, 100000, 100000);
       // mSensorManager.registerListener(this, mGyroscope, 100000, 100000);
        mSensorManager.registerListener(this, mGeomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mOrientationAngles, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        input_signal = new ArrayList();
        input_signal.clear();
        input_signal = csvReader();

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
        grav = true;
        acc = false;
        orient = false;
        rot = false;
        final Button button = (Button) findViewById(R.id.resetprob);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                squatcount=0;
                breakcount=0;
                situpcount=0;
                burpeecount=0;
                squatprob.setText("");
                breakprob.setText("");
                situpprob.setText("");
                burpeeprob.setText("");
            }
        });
// TEST MODEL
        input_collection.clear();

        System.out.println(input_signal.size());
        activityInference = new ActivityInference(getApplicationContext());
        activityPrediction();

    }

   private  void writeText() {
       BufferedWriter writer = null;
       try {

           File logFile = new File("result_data.csv");
           System.out.println("Il file si trova :"+ logFile.getCanonicalPath());

           writer = new BufferedWriter(new FileWriter(logFile));
           for(int k=0;k<input_collection.size();k++) {
               writer.write(input_collection.get(k));
               writer.write("\n");
           }


       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           try {

               writer.close();
           } catch (Exception e) {
           }
       }
   }







   private List csvReader(){
       BufferedReader reader;
       List csvArray = new ArrayList<>();

       try{
           final InputStream file = getAssets().open("input_data.csv");
           reader = new BufferedReader(new InputStreamReader(file));
           String line = reader.readLine();
           int i=0;
           while (i<1296000) {


                   line = reader.readLine();
                   csvArray.add(line);


                   //System.out.println("Csv Array size");
                   //System.out.println(csvArray.size());

                   i++;

           }
       } catch(IOException ioe){
           ioe.printStackTrace();
       }
       return csvArray;
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
       // mSensorManager.registerListener(this, mAccelerometer, 100000, 100000);
       // mSensorManager.registerListener(this, mOrientationAngles, 100000, 100000);
       // mSensorManager.registerListener(this, mGravity, 100000, 100000);
       // mSensorManager.registerListener(this, mGyroscope, 100000, 100000);
        mSensorManager.registerListener(this, mGeomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mOrientationAngles, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {


            //activityPrediction();
        if ((event.sensor.getType() == Sensor.TYPE_GRAVITY) && (grav == true)) {
            // xG=event.values[0]/gConst;
            // yG=event.values[1]/gConst;
            //zG=event.values[2]/gConst;
            /*xG = ((event.values[0] / gConst) * 0.5) + ((event.values[0] / gConst) * 0.5);
            yG = ((event.values[1] / gConst) * 0.5) + ((event.values[1] / gConst) * 0.5);
            zG = ((event.values[2] / gConst) * 0.5) + ((event.values[2] / gConst) * 0.5);
            float xG1 = (float) xG;
            float yG1 = (float) yG;
            float zG1 = (float) zG;

            input_signal.add(xG1);
            input_signal.add(yG1);
            input_signal.add(zG1);
            acc = true;
            grav = false;
            mGrav = event.values;*/

        }
        if ((event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) && (acc == true)) {
            // xA=event.values[0]/gConst;
            // yA=event.values[1]/gConst;
            //  zA=event.values[2]/gConst;
          /*  xA = ((event.values[0] / gConst) * 0.07007708) + ((event.values[0] / gConst) * 0.49544499);
            yA = ((event.values[1] / gConst) * 0.07621951) + ((event.values[1] / gConst) * 0.5007622);
            zA = ((event.values[2] / gConst) * 0.06131208) + ((event.values[2] / gConst) * 0.527897);
            float xA1 = (float) xA;
            float yA1 = (float) yA;
            float zA1 = (float) zA;

            input_signal.add(xA1);
            input_signal.add(yA1);
            input_signal.add(zA1);
            orient = true;
            acc = false;
*/
        }
        if ((event.sensor.getType() == Sensor.TYPE_ORIENTATION) && (orient == true)) {
            //  p = event.values[1]*0.0174533;
            //  r = event.values[2]*0.0174533;
            // y = event.values[0]*0.0174533;
/*
            p = ((event.values[1] * 0.0174533) * 0.31948882) + ((event.values[1] * 0.0174533) * 0.49840256);
            r = ((event.values[2] * 0.0174533) * 0.15923567) + ((event.values[2] * 0.0174533) * 0.5);
            y = ((event.values[0] * 0.0174533) * 0.15923567) + ((event.values[0] * 0.0174533) * 0.5);


            float p1 = (float) p;
            float r1 = (float) r;
            float y1 = (float) y;


            input_signal.add(p1);
            input_signal.add(r1);
            input_signal.add(y1);
            rot = true;
            orient = false;

        */
        }


        if ((event.sensor.getType() == Sensor.TYPE_GYROSCOPE) && (rot == true)) {
            // xR=event.values[0]/gConst;
            // yR=event.values[1]/gConst;
            // zR=event.values[2]/gConst;
/*
            xR = ((event.values[0] / gConst) * 0.04504505) + ((event.values[0] / gConst) * 0.54414414);
            yR = ((event.values[1] / gConst) * 0.03229974) + ((event.values[1] / gConst) * 0.55620155);
            zR = ((event.values[2] / gConst) * 0.05347594) + ((event.values[2] / gConst) * 0.53475936);


            float xR1 = (float) xR;
            float yR1 = (float) yR;
            float zR1 = (float) zR;

            input_signal.add(xR1);
            input_signal.add(yR1);
            input_signal.add(zR1);
            grav = true;
            rot = false;
        }
*/
        }
    }


    private void activityPrediction() {
        if (input_signal.size() == 1296000) {
            //if (xGrav.size() == N_SAMPLES && yGrav.size() == N_SAMPLES && zGrav.size() == N_SAMPLES && xAcc.size() == N_SAMPLES && yAcc.size() == N_SAMPLES && zAcc.size() == N_SAMPLES && pitch.size() == N_SAMPLES && roll.size() == N_SAMPLES && yaw.size() == N_SAMPLES && xRot.size() == N_SAMPLES && yRot.size() == N_SAMPLES && zRot.size() == N_SAMPLES) {
            // Mean normalize the signal
            //normalize();

            // Copy all x,y and z values to one array of shape N_SAMPLES*3
           /* input_signal.addAll(xGrav);
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
            input_signal.addAll(zRot); */
            //  for(int k=0;k<input_signal.size();k++){

            //    System.out.println(input_signal.toString());
            //   }
            // Perform inference using Tensorflow
            System.out.println("STR");


            int startSignal = 0;
            int endSignal = 600;
            int iterationNumber = 1;
            System.out.println(startSignal);

            while (endSignal < 1296000) {
                int startProva = 0;
                List<Float> prova = new ArrayList<>();
                for (int j = 0; j < 600; j++) {
                    prova.add(startProva, Float.parseFloat(input_signal.get(j+startSignal)));

                   //System.out.println(prova.toString());

                    startProva++;
                }
                startSignal = endSignal;
                endSignal = endSignal + 600;

                System.out.println(input_signal.size());
                System.out.println(prova.size());
                //float[] results = activityInference.getActivityProb(toFloatArray(prova));
                float[] results = activityInference.getActivityProb(toFloatArray(prova));
                System.out.println("Iterazione numero : "+iterationNumber);
                System.out.println("result");
                System.out.println(results[0]);
                System.out.println(results[1]);
                System.out.println(results[2]);
                System.out.println(results[3]);
                System.out.println(results[4]);
                iterationNumber++;
/*
            breakprob.setText(Float.toString(round(results[0], 2)));
            situpprob.setText(Float.toString(round(results[1], 2)));
            burpeeprob.setText(Float.toString(round(results[2], 2)));
            squatprob.setText(Float.toString(round(results[3], 2)));
            setbreakprob.setText(Float.toString(round(results[4], 2))); */
                int max = 0;
                for (int i = 0; i < 4; i++) {
                    if (results[i] > results[max]) {
                        max = i;
                    }

                }
                if (max == 0) {
                    input_collection.add("Break");
                    breakcount++;
                    breakprob.setText(Integer.toString(breakcount));
                }
                if (max == 2) {
                    input_collection.add("Situp");
                    situpcount++;
                    situpprob.setText(Integer.toString(situpcount));
                }
                if (max == 1) {
                    input_collection.add("Burpee");
                    burpeecount++;
                    burpeeprob.setText(Integer.toString(burpeecount));
                }
                if (max == 3) {
                    input_collection.add("Squat");
                    squatcount++;
                    squatprob.setText(Integer.toString(squatcount));
                }

            }
            for (int k=0;k<input_collection.size();k++){
                System.out.println(input_collection.get(k)+" è la previsione numero : "+ (k+1));
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
