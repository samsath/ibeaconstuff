package com.mawork.beaconhead;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ServerSync extends AsyncTask<List<String>, Integer, JSONObject> {

    public Database db;
    public String curl;
    public Context context;

    public HttpClient httpClient = new DefaultHttpClient();

    public ServerSync(Context contextin) {
        context = contextin;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();


        db = new Database(context);
        db.removeAll();
    }

    public String readEntry(InputStream input) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(input),8);
        StringBuilder builder = new StringBuilder();

        String line;

        while((line = reader.readLine()) != null){
            builder.append(line = "\n");
        }
        return builder.toString();
    }


    @Override
    protected JSONObject doInBackground(List<String>... request) {
        /*
        This will get all the Beacon information from the sellected website


         */

        String url = request[0].get(0);

        curl = "https://"+url+"/beacon/all/";
        Log.d("MAbeacon",curl);

        try{
            HttpPost httpPost = new HttpPost(curl);
            Log.d("MAbeacon",httpPost.toString());
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            String result = readEntry(entity.getContent());
            Log.i("MAbeacon", result);
            JSONArray array = new JSONArray(result);
            for (int i =0 ; i < array.length(); i++){
                JSONObject row = array.getJSONObject(i);
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

        return null;
    }



}
