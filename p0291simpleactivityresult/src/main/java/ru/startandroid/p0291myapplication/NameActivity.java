package ru.startandroid.p0291myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Work on 2/2/2016.
 */
public class NameActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etName;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name);

        etName = (EditText) findViewById(R.id.etName);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
// Create intent
        Intent intent = new Intent();
        // put data to Intent
        intent.putExtra("name", etName.getText().toString());
        // return Intent
        setResult(RESULT_OK, intent);
        finish();

    }
}
