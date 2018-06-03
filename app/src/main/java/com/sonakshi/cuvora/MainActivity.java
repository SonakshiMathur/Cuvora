package com.sonakshi.cuvora;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public int flag=0;
    ListView ls;
    public ArrayList<HashMap<String,String>> names=new ArrayList<>();
    String detailedList;

    public class GetVehicleData extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader ir = new InputStreamReader(is);
                int data;

                String result = "";
                while ((data = ir.read()) != -1) {
                    char curr = (char) data;
                    result = result + curr;
                }
                Log.i("qq","2");
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find data", Toast.LENGTH_LONG).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String finaltext="";
            try {
                JSONObject jsonObject=new JSONObject(result);
                String data=jsonObject.getString("data");
                JSONObject dataJsonObject=new JSONObject(data);
                String list=dataJsonObject.getString("list");
                detailedList=list;
                JSONArray listOfInfo=new JSONArray(list);
                Log.i("ll",Integer.toString(listOfInfo.length()));
                for (int i=0;i<listOfInfo.length();i++) {
                    JSONObject vehicle = listOfInfo.getJSONObject(i);
                    String vehicleDetails=vehicle.getString("vehicleDetails");
                    Log.i("qq4q", vehicleDetails);
                    JSONArray vehicleDA=new JSONArray(vehicleDetails);
                    JSONObject details=vehicleDA.getJSONObject(0);
                    String displayName=details.getString("displayName");
                    HashMap mp=new HashMap();
                    mp.put("name",displayName);
                    /*
                    String carDet=vehicle.getString("carDetails");
                    Log.i("qqq",carDet);*/
                    String url=vehicle.getString("imageURL");
                    mp.put("img", url);
                    names.add(mp);
                    Log.i("ll",Integer.toString(i));

                }
                Log.i("qq","3");
                MyAdapter arr=new MyAdapter(MainActivity.this,names);
                ls.setAdapter(arr);

                flag=1;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find soln", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls=(ListView)findViewById(R.id.list);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }

        Log.i("qq","1");
        GetVehicleData ghtt=new GetVehicleData();
        ghtt.execute("https://cuvora.com/car/utils/trending/celebs");
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),DetailedList.class);
                intent.putExtra("pos",position);
                intent.putExtra("details",detailedList);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            }
        });

        Log.i("qq","4");



    }



}
