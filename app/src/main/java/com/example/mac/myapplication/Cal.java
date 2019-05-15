package com.example.mac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Cal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        float rate=0f;
        String title="";

        EditText inp2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rate_calc);
            Intent intent=getIntent();
            rate=intent.getFloatExtra("rate",0f);
            title=intent.getStringExtra("title");

            ((TextView)findViewById(R.id.Cal_title)).setText(title);

            inp2=(EditText)findViewById(R.id.Cal_inp);
            inp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    TextView show=(TextView)Cal.this.findViewById(R.id.Cal_oup);
                    if(s.length()>0){
                        float val=Float.parseFloat(s.toString());
                        show.setText(val+"RMB="+(100/rate*val));
                    }else {
                        show.setText("");
                    }
                }
            });
        }
    }

