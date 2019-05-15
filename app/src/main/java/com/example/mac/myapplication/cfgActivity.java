package com.example.mac.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class cfgActivity extends AppCompatActivity {
    EditText dolt;
    EditText eurot;
    EditText wont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfg);
        Intent intent = getIntent();
        float dol2 = intent.getFloatExtra("dolratekey",0.0f);
        float euro2 = intent.getFloatExtra("euroratekey",0.0f);
        float won2 = intent.getFloatExtra("wonratekey",0.0f);

        dolt = findViewById(R.id.dol);
        eurot= findViewById(R.id.euro);
        wont = findViewById(R.id.won);

        dolt.setText(String.valueOf(dol2));
        eurot.setText(String.valueOf(euro2));
        wont.setText(String.valueOf(won2));




    }
        public void save(View btn){
            float newdol = Float.parseFloat(dolt.getText().toString());
            float neweuro = Float.parseFloat(eurot.getText().toString());
            float newwon = Float.parseFloat(wont.getText().toString());
            Intent intent = getIntent();
            Bundle bdl = new Bundle();
            bdl.putFloat("dolratekey",newdol);
            bdl.putFloat("euroratekey",neweuro);
            bdl.putFloat("wonratekey",newwon);
            intent.putExtras(bdl);
            setResult(3,intent);

            finish();
        }
}
