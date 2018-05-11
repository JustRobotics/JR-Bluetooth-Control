package in.justrobotics.jrbluetoothcontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;


public class ControllerActivity extends AppCompatActivity {

    int sendChar;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    String command;

    public void displayAboutStandardController(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About the standard controller");
        builder.setMessage("This is the standard arrow-key controller. You can simply move your robot my clicking the arrow keys.");
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
        String deviceAddress;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

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
        ImageView Up = (ImageView) findViewById(R.id.up);
        ImageView Down = (ImageView) findViewById(R.id.down);
        ImageView Left = (ImageView) findViewById(R.id.left);
        ImageView Right = (ImageView) findViewById(R.id.right);

        Up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "1";

                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "10";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }

                return false;
            }
        });

        Down.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "2";

                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "10";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {

                    }

                }
                return false;
            }
        });

        //OnTouchListener code for the forward left button (button long press)
        Left.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "3";

                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "10";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {

                    }

                }
                return false;
            }
        });

        //OnTouchListener code for the forward right button (button long press)
        Right.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    command = "4";

                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    command = "10";
                    try
                    {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });

 /*       BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device: pairedDevices) {
            TextView BluetoothID =  (TextView) findViewById(R.id.BTID);

        }
        */

/*
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
*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stdc_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.howtouse_controller:
                displayAboutStandardController();
        }
        return true;
    }
}

