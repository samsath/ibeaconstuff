package com.mawork.androidbeacon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class serverSync extends AsyncTask<String, Integer, JSONObject> {

    private Database db;
    private String curl;
    private Context context;


    private HttpClient httpclient = new DefaultHttpClient();

    public serverSync(Context contextin){ context = contextin; }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        SharedPreferences settings = context.getSharedPreferences("MAbeacon",1);

        String url = settings.getString("contentURL","");

        curl = "https://"+url+"/beacon/";

        db = new Database(context);
    }

    public String readEntry(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input),8);
        StringBuilder builder = new StringBuilder();

        String line;

        while((line = reader.readLine()) != null){
            builder.append(line = "\n");
        }
        return builder.toString();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        /*
        This will get all the Beacon information from the sellected website
         */

        try{
            HttpPost httpPost = new HttpPost(curl+"all/");
            HttpResponse response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            String result = readEntry(entity.getContent());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
