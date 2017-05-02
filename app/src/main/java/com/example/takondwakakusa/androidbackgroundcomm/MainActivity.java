package com.example.takondwakakusa.androidbackgroundcomm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click_intro (View view) {
        Intent i = new Intent(getBaseContext(), MessageActivity.class);

        //Set the Data to pass
        i.putExtra("title", "ECE 2504: Intro to CPE");
        startActivity(i);
        //startActivity(new Intent(getApplicationContext(), IntroActivity.class));
    }

    public void click_circuit (View view) {
        Intent i = new Intent(getBaseContext(), MessageActivity.class);

        //Set the Data to pass
        i.putExtra("title", "ECE 2004: Circuit Analysis");
        startActivity(i);
        //startActivity(new Intent(getApplicationContext(), CircuitActivity.class));
    }

    public void click_micro (View view) {
        Intent i = new Intent(getBaseContext(), MessageActivity.class);

        //Set the Data to pass
        i.putExtra("title", "ECE 2534: Microcontroller");
        startActivity(i);
        //startActivity(new Intent(getApplicationContext(), MicroActivity.class));
    }

    public void click_dd1 (View view) {
        Intent i = new Intent(getBaseContext(), MessageActivity.class);

        //Set the Data to pass
        i.putExtra("title", "ECE 3544: Digital Design I");
        startActivity(i);
        //startActivity(new Intent(getApplicationContext(), DD1Activity.class));
    }

    public void click_embedded (View view) {
        Intent i = new Intent(getBaseContext(), MessageActivity.class);

        //Set the Data to pass
        i.putExtra("title", "ECE 4534: Embedded System");
        startActivity(i);
        //startActivity(new Intent(getApplicationContext(), EmbeddedActivity.class));
    }
}
