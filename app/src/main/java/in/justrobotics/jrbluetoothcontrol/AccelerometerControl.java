package in.justrobotics.jrbluetoothcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Set;
import java.util.UUID;


public class AccelerometerControl extends AppCompatActivity implements SensorEventListener {

    private TextView xText, yText, zText;
    private Sensor mySensor;
    private SensorManager SM;
    private ProgressBar fd,bk,rt,lt;

    public void displayAboutTiltController(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About the tilt controller");
        builder.setMessage(R.string.about_accelc);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_control);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();


        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BluetoothDevice mDevice = device;
                //String macAddress = mDevice.getAddress();
                final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                TextView BluetoothID =  (TextView) findViewById(R.id.BTID);
                final String DEVICE_ADDRESS=device.getAddress();
                BluetoothID.setText(DEVICE_ADDRESS);

            }
        }
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI);
        fd = (ProgressBar) findViewById(R.id.up_magnitude);
        bk = (ProgressBar) findViewById(R.id.down_magnitude);
        rt = (ProgressBar) findViewById(R.id.right_magnitude);
        lt = (ProgressBar) findViewById(R.id.left_magnitude);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        fd.setIndeterminate(false);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.accelerometerc_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.howtouse_accel:
                displayAboutTiltController();
        }
        return true;
    }
}
