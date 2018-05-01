package in.justrobotics.jrbluetoothcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
    }
}
