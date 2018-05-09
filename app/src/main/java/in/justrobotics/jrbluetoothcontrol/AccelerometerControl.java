package in.justrobotics.jrbluetoothcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.ProgressBar;
import android.widget.TextView;


public class AccelerometerControl extends AppCompatActivity implements SensorEventListener {

    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;
    private ProgressBar fd,bk,rt,lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_control);
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI);
        xText = (TextView)findViewById(R.id.textView1);
        yText = (TextView)findViewById(R.id.textView2);
        zText = (TextView)findViewById(R.id.textView3);
        fd = (ProgressBar) findViewById(R.id.up_magnitude);
        bk = (ProgressBar) findViewById(R.id.down_magnitude);
        rt = (ProgressBar) findViewById(R.id.right_magnitude);
        lt = (ProgressBar) findViewById(R.id.left_magnitude);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        fd.setIndeterminate(false);
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);
        float x=event.values[0]*16.67f;
        float y=event.values[1]*16.67f;
        float z=event.values[2]*16.67f;
        int percentX = (int) x;
        if (percentX<0){
            rt.setProgress(Math.abs(percentX));
            lt.setProgress(0);
        }
        else if (percentX>0){
            lt.setProgress(Math.abs(percentX));
            rt.setProgress(0);
        }
        else{
            lt.setProgress(0);
            rt.setProgress(0);
        }
        int percentY = (int) y;
        if (percentY<0){
            fd.setProgress(Math.abs(percentY));
            bk.setProgress(0);
        }
        else if (percentY>0){
            bk.setProgress(Math.abs(percentY));
            fd.setProgress(0);
        }
        else{
            fd.setProgress(0);
            bk.setProgress(0);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
