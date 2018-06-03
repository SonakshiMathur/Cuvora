package com.sonakshi.cuvora;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedList extends AppCompatActivity {
    ImageView im;

    public class Download extends AsyncTask<String,Void,Void>{
        Bitmap bm;


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(String... strings) {
            try{

                URL aURL = new URL(strings[0]);

                HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        im.setImageBitmap(bm);

                    }
                });


            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);

        ArrayList<Map<String,String>> friends=new ArrayList<>();
        String shareT="";
        String list=getIntent().getStringExtra("details");
        int pos=getIntent().getIntExtra("pos",0);
        im=(ImageView)findViewById(R.id.CarPic);
        im.setImageResource(R.drawable.a);
        Download  dw=new Download();

        try{
            JSONArray listArr=new JSONArray(list);
            JSONObject thisObj=listArr.getJSONObject(pos);
            String url=thisObj.getString("imageURL");
            dw.execute(url);
            String vehicleDetails=thisObj.getString("vehicleDetails");
            JSONArray vehicleDA=new JSONArray(vehicleDetails);
            JSONObject details=vehicleDA.getJSONObject(0);
            shareT=details.getString("shareText");
            String info=details.getString("info");
            JSONArray inf=new JSONArray(info);
            for(int i=0; i<inf.length(); i++){
                JSONObject curr=inf.getJSONObject(i);
                Map<String,String> mp=new HashMap();
                mp.put("head",curr.getString("key"));
                mp.put("content", curr.getString("value"));
                if(curr.getString("key").equals("Registration No")){
                    String regno=curr.getString("value");
                    android.support.v7.widget.Toolbar tb=findViewById(R.id.heading);
                    if(android.os.Build.VERSION.SDK_INT >=21)
                    tb.setTitle(regno);
                    else
                    {
                        tb.setVisibility(View.INVISIBLE);
                    }
                }
                friends.add(mp);
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }


        ListView ls=findViewById(R.id.dynamicList);
        String[] from={"head","content"};
        int[] to={android.R.id.text1,android.R.id.text2};
        SimpleAdapter arr=new SimpleAdapter(this,friends,android.R.layout.simple_list_item_2,from,to);
        ls.setAdapter(arr);
        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.shareButton);
        final String shareText=shareT;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, shareText);

                startActivity(Intent.createChooser(share, "Share Car Details"));
            }
        });

    }
}