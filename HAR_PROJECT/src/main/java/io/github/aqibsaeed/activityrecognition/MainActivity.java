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
import android.util.Log;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // gConstant 9.81
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
    private static List input_signal;
    private static List<String> input_collection;
    private int breakcount, situpcount, burpeecount, squatcount;
    private float[] mGrav;
    private float[] mGeo;
    private double p, r, y, xG, yG, zG, xA, yA, zA, xR, yR, zR;
    private boolean grav;
    private boolean acc;
    private boolean orient;
    private boolean rot;
    private Button resetprob;
    private float maxValue = 0;
    private static double accConst = 19.6133;
    private static double gravConst = 9.8;
    private static double orientConst;
    private static double M_PI = 3.14159265358979323846264338327950288;
    private static double pi_Rad = 3.14159265359/180;
    private static double[] signal_Constant = {0.5, 0.5, 0.5, 0.07007708, 0.07621951, 0.06131208, 0.31948882,  0.15923567, 0.15923567, 0.04504505, 0.03229974, 0.05347594,0.5, 0.5, 0.5, 0.49544499, 0.5007622, 0.527897, 0.49840256, 0.5, 0.5, 0.54414414, 0.55620155, 0.53475936};
    private static double maxAcc = 19.6133;
    private static double maxGrav = 19.6133;
    private static double maxGyro = 34.90656;

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
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        // Orientation Sensor
        mOrientationAngles = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        // Gravity Sensor
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //Gyroscope Sensor
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGeomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // Register sensor Listener with 0.1s delay

        mSensorManager.registerListener(this, mGeomagnetic, 100000,100000);
        mSensorManager.registerListener(this, mAccelerometer, 100000,100000);
        mSensorManager.registerListener(this, mOrientationAngles, 100000,100000);
        mSensorManager.registerListener(this, mGravity,100000,100000);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        input_signal = new ArrayList();

        //double [] input_signal  = {-0.0015263177920132875, -0.010142285376787186, -0.99994742870330811, 3.0956522095948458e-05, 0.0010785652557387948, -0.008826402947306633, 0.010142459674417134, -0.0015263969271222767, 0.39395883702886042, -0.0014953624922782183, 0.0023854372557252645, -0.003862177487462759};

        // input_signal.clear();
        //input_signal = csvReader();

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
                squatcount = 0;
                breakcount = 0;
                situpcount = 0;
                burpeecount = 0;
                squatprob.setText("");
                breakprob.setText("");
                situpprob.setText("");
                burpeeprob.setText("");
            }
        });

        activityInference = new ActivityInference(getApplicationContext());
        System.out.println(mAccelerometer.getMaximumRange()+ "  Accelerometer" +
                "");
        System.out.println(mGravity.getMaximumRange()+ "  Gravity");
        System.out.println(mOrientationAngles.getMaximumRange()+ "  Orientation");
        System.out.println(mGyroscope.getMaximumRange()+ "  Gyroscope");


    }
   //
   /* private void writeText() {
        BufferedWriter writer = null;
        try {

            File logFile = new File("result_data.csv");
            System.out.println("Il file si trova :" + logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            for (int k = 0; k < input_collection.size(); k++) {
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
    }*/

    //
    /*private List csvReader() {
        BufferedReader reader;
        List csvArray = new ArrayList<>();

        try {
            final InputStream file = getAssets().open("input_data.csv");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            int i = 0;
            while (i < 1296000) {


                line = reader.readLine();
                csvArray.add(line);


                //System.out.println("Csv Array size");
                //System.out.println(csvArray.size());

                i++;

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return csvArray;
    }*/


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
        mSensorManager.registerListener(this, mAccelerometer, 100000,100000);
        mSensorManager.registerListener(this, mOrientationAngles, 100000,100000);
        mSensorManager.registerListener(this, mGravity, 100000,100000);
        mSensorManager.registerListener(this, mGyroscope,100000,100000);

    }
    private double calculateNewValue(double currentValue,double oldValueMax,double oldValueMin,double newValueMax,double newValueMin){

        double oldRange = oldValueMax - oldValueMin;
        double newRange = newValueMax - newValueMin;
        double newValue = (((currentValue - oldValueMin)*newRange)/oldRange)+newValueMin;
        return newValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


            // Let Activity Prediction
            activityPrediction();
            // Build an order input_signal : { 3x Gravity, 3x Accelerations, 3x Orientation, 3x Rotation}
        if ((event.sensor.getType() == Sensor.TYPE_GRAVITY) && (grav == true)) {



            // Convert values from m/s^2 to G-force
          /* xG = ((event.values[0] / gConst) * 0.5) + 0.5;
           yG = ((event.values[1] / gConst) * 0.5) + 0.5;
           zG = ((event.values[2] / gConst) * 0.5) + 0.5; */

          /*Calculate the values in the new range. Convert the values and the old ranges from m/s^2 to G-force
            Max value from mGravity = 34.90656
            Max value readed from gravity sensor = 9.8 */

            xG = calculateNewValue(event.values[0]/ gConst,maxGrav/gConst,(-1*maxGrav)/gConst,1,-1);
            yG = calculateNewValue(event.values[1]/ gConst,maxGrav/gConst,(-1*maxGrav)/gConst,1,-1);
            zG = calculateNewValue(event.values[2]/ gConst,maxGrav/gConst,(-1*maxGrav)/gConst,1,-1);

            // System.out.println(-2*xG);
            //System.out.println(-2*yG);
            //System.out.println(-2*zG);
            xG = (-2*xG*signal_Constant[0])+signal_Constant[12];
            yG = (-2*yG*signal_Constant[1])+signal_Constant[13];
            zG = (-2*zG*signal_Constant[2])+signal_Constant[14];



         // System.out.println("x :  "+ xG+"              y:  "+ yG+"                 +z:   "+ zG );



            float xG1 = (float) xG;
            float yG1 = (float) yG;
            float zG1 = (float) zG;

             input_signal.add(xG1);
             input_signal.add(yG1);
             input_signal.add(zG1);




            acc = true;
            grav = false;
            mGrav = event.values;

        }
        if ((event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) && (acc == true)) {
            //Return the acceleration of the device minus G-force ( user acceleration for ios)
            // Convert values from m/s^2 to G-force
         /*
            xA = ((event.values[0] / gConst) * 0.07007708) 0.07007708
            yA = ((event.values[1] / gConst) * 0.07621951) +  0.5007622;
            zA = ((event.values[2] / gConst) * 0.06131208) +  0.527897;
*/
            xA = calculateNewValue(event.values[0]/gConst, maxAcc/gConst, (-1*maxAcc)/gConst,1,-1);
            yA = calculateNewValue(event.values[1]/gConst, maxAcc/gConst, (-1*maxAcc)/gConst,1,-1);
            zA = calculateNewValue(event.values[2]/gConst, maxAcc/gConst, (-1*maxAcc)/gConst,1,-1);
            //System.out.println(-1*yA);
           // System.out.println(-1*zA);
            //System.out.println(event.values[0]+ "    XA");
           // System.out.println(event.values[1]+ "    YA");
           // System.out.println(event.values[2]+ "    ZA");
            xA = (-1*xA*signal_Constant[3])+signal_Constant[15];
            yA = (-1*yA*signal_Constant[4])+signal_Constant[16];
            zA = (-1*zA*signal_Constant[5])+signal_Constant[17];
           // System.out.println("x :  "+ xA+"              y:  "+ yA+"                 +z:   "+ zA );

            float xA1 = (float) xA;
            float yA1 = (float) yA;
            float zA1 = (float) zA;

            input_signal.add(xA1);
            input_signal.add(yA1);
            input_signal.add(zA1);
            orient = true;
            acc = false;


        }
        if ((event.sensor.getType() == Sensor.TYPE_ORIENTATION) && (orient == true)) {
            //Convert values to rad/s

            p = calculateNewValue(event.values[1]*pi_Rad,180*pi_Rad,-180*pi_Rad,M_PI/2,-M_PI/2);
            r = calculateNewValue(event.values[2]*pi_Rad,90*pi_Rad,-90*pi_Rad,M_PI,-M_PI);
            y = calculateNewValue(event.values[0]*pi_Rad,360*pi_Rad,0*pi_Rad,M_PI,-M_PI);
            //System.out.println(p);


            p = (p*signal_Constant[6])+signal_Constant[18];
            r = (r*signal_Constant[7])+signal_Constant[19];
            y = (-1*y*signal_Constant[8])+signal_Constant[20];


           // System.out.println("pitch :  "+ p+"              roll:  "+ r+"                 +yaw:   "+ y );
/*
            p = ((event.values[1] * 0.0174533) * 0.31948882) + 0.49840256;
            r = ((event.values[2] * 0.0174533) * 0.15923567) + 0.5;
            y = ((event.values[0] * 0.0174533) * 0.15923567) + 0.5;

*/

            float p1 = (float) p;
            float r1 = (float) r;
            float y1 = (float) y;


            input_signal.add(p1);
            input_signal.add(r1);
            input_signal.add(y1);
            rot = true;
            orient = false;


        }


        if ((event.sensor.getType() == Sensor.TYPE_GYROSCOPE) && (rot == true)) {
            // Rad/s
            xR = calculateNewValue(event.values[0],maxGyro,0,pi_Rad*360,0);
            yR = calculateNewValue(event.values[1],maxGyro,0,pi_Rad*360,0);
            zR = calculateNewValue(event.values[2],maxGyro,0,pi_Rad*360,0);


            xR = (2*xR*signal_Constant[9])+signal_Constant[21];
            yR = (2*yR*signal_Constant[10])+signal_Constant[22];
            zR = (2*zR*signal_Constant[11])+signal_Constant[23];
           // System.out.println("x :  "+ xR+"              y:  "+ yR+"                 +z:   "+ zR );
           /* xR = ((event.values[0]) * 0.04504505) +  0.54414414;
            yR = ((event.values[1]) * 0.03229974) + 0.55620155;
            zR = ((event.values[2]) * 0.05347594) + 0.53475936;
*/

            float xR1 = (float) xR;
            float yR1 = (float) yR;
            float zR1 = (float) zR;

            input_signal.add(xR1);
            input_signal.add(yR1);
            input_signal.add(zR1);
            grav = true;
            rot = false;
        }

        }



    private void activityPrediction() {
        // We take 40 timesteps * 12 input = 600 elements
        if (input_signal.size() == 600) {

              /*
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
                 //   prova.add(startProva, Float.parseFloat(input_signal.get(j + startSignal)));

                    //System.out.println(prova.toString());

                    startProva++;
                }
                startSignal = endSignal;
                endSignal = endSignal + 600;
*/

            // System.out.println(prova.size());
            //float[] results = activityInference.getActivityProb(toFloatArray(prova));


            // Tensorflow Inference
            float[] results = activityInference.getActivityProb(toFloatArray(input_signal));
            // Return the predict results
                System.out.println("result");
                System.out.println(results[0] + "           Break");
                System.out.println(results[1] + "           Burpee");
                System.out.println(results[2] + "           Situp");
                System.out.println(results[3] + "           Squat");
                System.out.println(results[4] + "           Set_Break");
            input_signal.clear();


            // Take the max result


            int max = 0;
            for (int i = 0; i < 4; i++) {
                if (results[i] > results[max]) {
                    max = i;
                }

            }
            if (max == 4) {
                input_collection.add("Break");
                breakcount++;
                breakprob.setText(Integer.toString(breakcount));
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


            // for (int k = 0; k < input_collection.size(); k++) {
            //   System.out.println(input_collection.get(k) + " è la previsione numero : " + (k + 1));
            //  }


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
        public static float round ( float d, int decimalPlace){
            BigDecimal bd = new BigDecimal(Float.toString(d));
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        }


        @Override
        public void onAccuracyChanged (Sensor sensor,int i){



        }
    }
