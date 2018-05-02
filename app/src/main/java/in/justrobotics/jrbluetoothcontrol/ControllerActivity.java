package in.justrobotics.jrbluetoothcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
public class ControllerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
        // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BluetoothDevice mDevice = device;
            }
        }


        ImageView Up = (ImageView) findViewById(R.id.up);
        Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //what to do when up is clicked
            }
        });
        ImageView Left = (ImageView) findViewById(R.id.left);
        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //what to do when up is clicked
            }
        });
        ImageView Right = (ImageView) findViewById(R.id.right);
        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //what to do when  is clicked
            }
        });
        ImageView Down = (ImageView) findViewById(R.id.down);
        Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //what to do when up is clicked
            }
        });

    }

}

