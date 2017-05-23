package com.example.startup.mapodp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListOdpActivity extends AppCompatActivity {

    ImageView back;
    LinearLayout linearLayoutODP;
    ArrayList<ODP> odpList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_odp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        odpList = getIntent().getParcelableArrayListExtra("odp_list");


        linearLayoutODP = (LinearLayout) findViewById(R.id.list_ODP);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListOdpActivity.super.onBackPressed();
            }
        });
        fetchItem();


    }


    private void fetchItem() {
        linearLayoutODP.removeAllViews();

        if (odpList.size() > 0) {

            for (int i = 0; i < odpList.size(); i++) {
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout convertView = (LinearLayout) inflater.inflate(R.layout.list_odp, linearLayoutODP, false);
                TextView name = (TextView) convertView.findViewById(R.id.odp_name);
                name.setText(String.valueOf(odpList.get(i).namaODP));

                final int j=i;
                convertView.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        // Start an alpha animation for clicked item
                        Animation animation1 = new AlphaAnimation(0.3f, 5.0f);
                        animation1.setDuration(800);
                        view.startAnimation(animation1);
                        setResultODP(j);
                    }

                });
                linearLayoutODP.addView(convertView);

            }
        }

    }

    private void setResultODP(int index)
    {
        Intent intent = new Intent();
        intent.putExtra("index",index);
        setResult(RESULT_OK,intent);
        finish();
    }
}
