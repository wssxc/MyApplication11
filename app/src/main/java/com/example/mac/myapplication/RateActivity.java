package com.example.mac.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class RateActivity extends AppCompatActivity implements Runnable{
    private float dolrate = 0.1f;
    private float eurorate = 0.2f;
    private float wonrate = 0.3f;
    TextView show;
    EditText rmb;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = findViewById(R.id.Edt);
        show = findViewById(R.id.txt);

        SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        dolrate = sharedPreferences.getFloat("dol",0.0f);
        eurorate = sharedPreferences.getFloat("euro",0.0f);
        wonrate = sharedPreferences.getFloat("won",0.0f);

        Thread t = new Thread(this);
        t.start();

         handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==1){
                    Bundle bundle=(Bundle) msg.obj;
                    dolrate=bundle.getFloat("dolrate");
                    eurorate=bundle.getFloat("eurorate");
                    wonrate=bundle.getFloat("wonrate");
                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();


                }
                super.handleMessage(msg);
            }
        };

    }



    public void onClick(View btn){
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }
        if(btn.getId()==R.id.buttondol){
            float x = r * dolrate;
            show.setText(String.valueOf(x));
        }
        if(btn.getId()==R.id.buttoneuro){
            float x = r * eurorate;
            show.setText(String.valueOf(x));
        }if(btn.getId()==R.id.buttonwon){
            float x = r * wonrate;
            show.setText(String.valueOf(x));
        }


    }
    public void open(View btn){
       //Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bilibili.com/video/av46504301?from=search&seid=8518176577312471272"));
       // startActivity(web);
        Intent cfg = new Intent(this,cfgActivity.class);
        cfg.putExtra("dolratekey",dolrate);
        cfg.putExtra("euroratekey",eurorate);
        cfg.putExtra("wonratekey",wonrate);

        startActivityForResult(cfg,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==1 && resultCode ==3){
            Bundle bd = data.getExtras();
            dolrate = bd.getFloat("dolratekey",0.2f);
            eurorate = bd.getFloat("euroratekey",0.1f);
            wonrate = bd.getFloat("wonratekey",0.1f);

            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dol",dolrate);
            editor.putFloat("euro",eurorate);
            editor.putFloat("won",wonrate);
            editor.commit();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() ==R.id.list){
            Intent list = new Intent(this,RlistActivity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    @Override
    public void run() {




        //用于保存网络数据
        Bundle bundle=new Bundle();


        //获取网络数据
        /*URL rateurl= null;
        try {
            rateurl = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) rateurl.openConnection();
            InputStream in =http.getInputStream();

            String html=inputStream2String(in);
            Log.i("run getintertmsg", "run: html="+html);

            Document doc=Jsoup.parse(html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Document doc = null;

        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc=Jsoup.parse(html);

            Elements tables=doc.getElementsByTag("table");
//            for(Element table : tables){
//                Log.i("Document activity", "run: table["+i+"]="+table);
//                i++;
//            }

            Element table2=tables.get(0);


            //获取<td>中的元素
            Elements tds = table2.getElementsByTag("td");
            for (int i =0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                //Log.i("Document activity", "run: ="+td1.text()+"--->"+td2.text());

                String val=td2.text();


                if ("美元".equals(td1.text())){
                    bundle.putFloat("dolrate",100f/Float.parseFloat(val));
                } else if ("欧元".equals(td1.text())){
                    bundle.putFloat("eurorate",100f/Float.parseFloat(val));
                } else if ("韩元".equals(td1.text())){
                    bundle.putFloat("wonrate",100f/Float.parseFloat(val));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取Msg对象  用于返回主线程
        Message msg=handler.obtainMessage();
        msg.what=1;//--设置信标
        //Message msg=handler.obtainMessage(1);
        //msg.obj="this message from run()  what=1";
        msg.obj=bundle;
        handler.sendMessage(msg);//--放回msg堆栈中



    }





}
