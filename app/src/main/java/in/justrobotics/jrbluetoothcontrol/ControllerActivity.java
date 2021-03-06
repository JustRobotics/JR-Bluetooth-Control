package in.justrobotics.jrbluetoothcontrol;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class ControllerActivity extends AppCompatActivity {

    BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private ImageView Up,Down,Left,Right;
    private int commandFromForward,commandFromReverse,commandFromLeft,commandFromRight;
    private final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    public String rBTaddress;
    public String rBTname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        Up = (ImageView) findViewById(R.id.upfwd);
        Down = (ImageView) findViewById(R.id.downbk);
        Left = (ImageView) findViewById(R.id.leftlt);
        Right = (ImageView) findViewById(R.id.rightrt);
        mBTAdapter=BluetoothAdapter.getDefaultAdapter();
        commandFromForward=0;
        commandFromReverse=0;
        commandFromLeft=0;
        commandFromRight=0;
        rBTaddress = getIntent().getStringExtra("REMOTE_DEVICE_ADDRESS");
        Toast.makeText(getApplicationContext(),rBTaddress,Toast.LENGTH_SHORT).show();
        rBTname = getIntent().getStringExtra("REMOTE_DEVICE_NAME");
        Toast.makeText(getApplicationContext(),rBTname,Toast.LENGTH_SHORT).show();
        //mConnectedThread = (ConnectedThread) getIntent().getSerializableExtra("BLUETOOTH_CONNECTED_THREAD");
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
//                    mReadBuffer.setText(readMessage);
                }

                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1){}
                    else{}
                }
            }
        };

        startConnectedThread();

        Up.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction()==MotionEvent.ACTION_DOWN){
                        commandFromForward=1;
                    }
                    if (event.getAction()==MotionEvent.ACTION_UP){
                        commandFromForward=0;
                    }
                    return false;
                }
            });
        Down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    commandFromReverse=2;
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    commandFromReverse=0;
                }
                return false;
            }
        });
        Left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    commandFromLeft=4;
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    commandFromLeft=0;
                }
                return false;
            }
        });
        Right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    commandFromRight=8;
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    commandFromRight=0;
                }
                return false;
            }
        });

        if (mConnectedThread != null) {
            mConnectedThread.write("stdc_init");
            toastMe("Start","short");
        }
        else if (mConnectedThread==null) {
            toastMe("It seems your bluetooth module is disconnected. Please connect it and restart.","long");
        }

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mConnectedThread != null) {
                    sendCumulativeCommand(commandFromForward,commandFromReverse,commandFromLeft,commandFromRight);
                }
                ha.postDelayed(this, 100);
            }
        }, 100);

    }

    public void toastMe(String toastMessage, String type){
        if (type=="long") {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
        }
        else if (type=="short") {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }
/*        try {
            synchronized (this) {
                this.wait(1000);
            }
        }
        catch (InterruptedException ie){
            System.out.println("Interrupted");
        }*/
    }

    public void sendCumulativeCommand(int fd, int bk, int rt, int lt){
        int cumulativeCommand=fd+bk+rt+lt;
        mConnectedThread.write(Integer.toString(cumulativeCommand));
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.stdc_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.howtouse_controller:
                displayAboutStandardController();
        }
        return true;
    }

    public void displayAboutStandardController() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About the standard controller");
        builder.setMessage(R.string.about_stdc);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void bluetoothOn(){
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            mBluetoothStatus.setText("Bluetooth enabled");
            Toast.makeText(getApplicationContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
//                mBluetoothStatus.setText("Enabled");
            }
            else{}
//                mBluetoothStatus.setText("Disabled");
        }
    }

    private void bluetoothOff(View view){
        mBTAdapter.disable(); // turn off
//        mBluetoothStatus.setText("Bluetooth disabled");
        Toast.makeText(getApplicationContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }

    private void discover(View view){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void listPairedDevices(){
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }


    public void startConnectedThread(){
        if(!mBTAdapter.isEnabled()) {
            Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            return;
        }

        // Spawn a new thread to avoid blocking the GUI one
        new Thread()
        {
            public void run() {
                boolean fail = false;

                BluetoothDevice device = mBTAdapter.getRemoteDevice(rBTaddress);

                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
//                        toastMe("fail e","short");
                        mBTSocket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
//                        toastMe("fail e2","short");
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
                if(fail == false) {
                    toastMe("Create ConnectedThread","short");
                    mConnectedThread = new ControllerActivity.ConnectedThread(mBTSocket);
                    mConnectedThread.start();

                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, rBTname)
                            .sendToTarget();
                }
            }
        }.start();

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }










}
