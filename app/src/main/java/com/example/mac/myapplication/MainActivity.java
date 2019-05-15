package com.example.mac.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = (TextView) findViewById(R.id.score);

    }
    public void btnadd1(View btn){
        showscore(1);
    }
    public void btnadd2(View btn){
        showscore(2);

    }
    public void btnadd3(View btn){
        showscore(3);

    }
    public void btnreset(View btn){
        score.setText("0");
    }
    private void showscore(int inc){
        String oldscore = (String) score.getText();
        int newscore = Integer.parseInt(oldscore)+inc;
        score.setText(""+newscore);
    }
}
