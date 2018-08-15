package in.justrobotics.jrbluetoothcontrol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class TerminalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        TextView connectedDevices=(TextView) findViewById(R.id.connected_devices);
        BluetoothManager manager = (BluetoothManager) getApplicationContext().getSystemService(BLUETOOTH_SERVICE);
        List<BluetoothDevice> connected = manager.getConnectedDevices(BluetoothProfile.GATT);
//        if (connected.size()>=1) {
        Log.i("Connected Devices: ", connected.get(0).toString() + "");
        connectedDevices.setText(connected.get(0).toString());
//        }
    }

}
