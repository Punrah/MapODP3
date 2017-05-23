package com.example.startup.mapodp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DialogNameActivity extends AppCompatActivity {


    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_name);

        name = (EditText) findViewById(R.id.odp_name);

    }


}
