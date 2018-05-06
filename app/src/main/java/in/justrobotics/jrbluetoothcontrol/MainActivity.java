package in.justrobotics.jrbluetoothcontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void composeEmail(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "shlokj@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Would like to get in touch");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void sendEmail () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send a message: ");
        // Set up the input
        final EditText input = new EditText(this);
        //Editable Message = input.getText();
        //final String message = Message.toString();
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String Message = input.getText().toString();
                composeEmail(Message);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openController = (Button) findViewById(R.id.open_controller);
        openController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startController = new Intent(MainActivity.this,ControllerActivity.class);
                startActivity(startController);
            }
        });
        Button openAccelController = (Button) findViewById(R.id.open_accel_controller);
        openAccelController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startControllerA = new Intent(MainActivity.this,AccelerometerControl.class);
                startActivity(startControllerA);
            }
        });
        Button contactUs = (Button) findViewById(R.id.send_email);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }
}
