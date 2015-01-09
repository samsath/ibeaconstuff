package com.mawork.beaconhead;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.easibeacon.protocol.IBeacon;
import com.easibeacon.protocol.IBeaconListener;
import com.easibeacon.protocol.IBeaconProtocol;
import com.easibeacon.protocol.Utils;


public class StartActivity extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 2;
    static final int PICK_CONTENT_REQUEST = 5;
    public static final String CURL = "contentURL";
    static SharedPreferences settings;

    public Intent listActivity;
    public Intent search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

        listActivity = new Intent(this, MainActivity.class);
        search = new Intent(context, beaconSearch.class);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            text = "This phone is not bluetooth enabled so wont be able to work";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            onDestroy();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            text = "Bluetooth not enabled";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        settings = this.getSharedPreferences("MAbeacon", MODE_WORLD_READABLE);
        if(settings.contains(CURL)){

            //Intent search = new Intent(context, beaconSearch.class);
            //startService(search);

            if(settings.contains(CURL)){
                DownloadBeaconData sync = new DownloadBeaconData();
                sync.execute(new String[] {settings.getString(CURL,"")});


                startService(search);
                startActivity(listActivity);
            }
        }else{
            Intent intent = new Intent(StartActivity.this, Settings.class);
            startActivityForResult(intent, PICK_CONTENT_REQUEST);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICK_CONTENT_REQUEST){
            if(resultCode == RESULT_OK){
                if(settings.contains(CURL)){
                    DownloadBeaconData sync = new DownloadBeaconData();
                    sync.execute(new String[] {settings.getString(CURL,"")});
                }
                startService(new Intent(this, beaconSearch.class));
                Log.d("MAbeacon","Search Start");
                startActivity(listActivity);
            }
        }
    }

    private class DownloadBeaconData extends AsyncTask<String, Void, String>{

        public Database db;



        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            db = new Database(getApplicationContext());
            db.removeAll();
        }

        @Override
        protected String doInBackground(String... urls){
            String result = "";

            for(String url : urls){
                DefaultHttpClient client = new DefaultHttpClient();

                String curl = "http://"+url+"/beacon/all/";
                Log.i("MAbeacon",curl);
                HttpGet httpGet = new HttpGet(curl);

                try{
                    HttpResponse execute = client.execute(httpGet);
                    HttpEntity entity = execute.getEntity();
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";

                    while(( s = buffer.readLine()) != null){
                        result += s;
                    }

                    JSONArray array = new JSONArray(result);
                    for (int i =0 ; i < array.length(); i++){
                        JSONObject row = array.getJSONObject(i);
                        Log.d("MAbeacon",row.toString());
                        Beacon bec = new Beacon(row.getString("uuid"), row.getInt("major"), row.getInt("minor"), row.getString("content"));
                        db.createBeacon(bec);
                    }

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return result;
        }
    }

}
