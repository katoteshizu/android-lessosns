package ru.startandroid.p0281intentextras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Work on 2/2/2016.
 */
public class ViewActivity extends AppCompatActivity {
    TextView tvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        tvView = (TextView) findViewById(R.id.tvView);

        Intent intent = getIntent();

        String fName = intent.getStringExtra("fname");
        String lName = intent.getStringExtra("lname");

        tvView.setText("Your name is: " + fName + " " + lName);
    }
}
