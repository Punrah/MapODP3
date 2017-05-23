package com.example.startup.mapodp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.ListUpdateCallback;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ODPDetailsActivity extends AppCompatActivity {

    ODP odp;
    int tag;

    TextView textViewidOdp;
    TextView textViewnamaODP;
    TextView textViewclusterCode;
    TextView textViewneqptReasue;
    TextView textViewtenossFracid;
    TextView textViewisiskaName;
    TextView textViewketerangan;
    TextView textViewalamat;
    TextView textViewidAdmin;

    RadioButton available;
    RadioButton full;

    LinearLayout buttonSetCapacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odpdetails);
        odp =  getIntent().getParcelableExtra("odp");
        tag = getIntent().getIntExtra("tag",0);

        textViewidOdp=(TextView) findViewById(R.id.id_odp);
        textViewnamaODP=(TextView) findViewById(R.id.odp_name);
        textViewclusterCode=(TextView) findViewById(R.id.cluster_code);
        textViewneqptReasue=(TextView) findViewById(R.id.neqpt_reasue);
        textViewtenossFracid=(TextView) findViewById(R.id.tenoss_fracid);
        textViewisiskaName=(TextView) findViewById(R.id.isiska_name);
        textViewketerangan=(TextView) findViewById(R.id.notes);
        textViewalamat=(TextView) findViewById(R.id.address);
        textViewidAdmin=(TextView) findViewById(R.id.id_admin);

        available=(RadioButton) findViewById(R.id.available_radio);
        full=(RadioButton) findViewById(R.id.full_radio);

        buttonSetCapacity = (LinearLayout) findViewById(R.id.button_set_capacity);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                available.setChecked(true);
                full.setChecked(false);
            }
        });
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                available.setChecked(false);
                full.setChecked(true);
            }
        });

         textViewidOdp.setText(odp.idOdp);
         textViewnamaODP.setText(odp.namaODP);
         textViewclusterCode.setText(odp.clusterCode);
         textViewneqptReasue.setText(odp.neqptReasue);
         textViewtenossFracid.setText(odp.tenossFracid);
         textViewisiskaName.setText(odp.isiskaName);
         textViewketerangan.setText(odp.keterangan);
         textViewalamat.setText(odp.alamat);
         textViewidAdmin.setText(odp.idAdmin);
            if(odp.kapasitas.contentEquals("available"))
            {
                available.setChecked(true);
                full.setChecked(false);
            }
            else if(odp.kapasitas.contentEquals("full"))
            {
                available.setChecked(false);
                full.setChecked(true);
            }

        buttonSetCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String capacity="";
                if(available.isChecked())
                {
                    capacity="available";
                }
                else if(full.isChecked())
                {
                    capacity="full";
                }
                Toast.makeText(ODPDetailsActivity.this, capacity, Toast.LENGTH_SHORT).show();
            }
        });











    }
}
