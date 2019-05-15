package com.example.mac.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RlistActivity extends ListActivity implements Runnable{
    String data[] = {"1","2","3"};
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rlist);
        List<String> list1 = new ArrayList<String>();
        for (int i = 1;i<100;i++){
            list1.add("item" + i);
        }

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(adapter);
        Thread thread = new Thread(this);
        thread.start();



        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what==6){
                    List<String>list2 = (List<String>)msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RlistActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);


                }
                super.handleMessage(msg);
            }
        };
    }

    public void run(){
        List<String> retlist = new ArrayList<String>();
        Document doc = null;

        try {
            Thread.sleep(30);
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc=Jsoup.parse(html);

            Elements tables=doc.getElementsByTag("table");


            Element table2=tables.get(1);


            //获取<td>中的元素
            Elements tds = table2.getElementsByTag("td");
            for (int i =0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);


                String val=td2.text();


                retlist.add("币种"+td1.text()+"汇率"+val);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(6);
        msg.obj = retlist;
        handler.sendMessage(msg);
    }
}
