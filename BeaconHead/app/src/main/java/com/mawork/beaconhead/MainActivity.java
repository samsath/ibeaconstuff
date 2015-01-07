package com.mawork.beaconhead;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends ActionBarActivity {


    static SharedPreferences settings;
    public static final String BURL = "beaconURL";
    public String url;
    static final int PICK_CONTENT_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        settings = this.getSharedPreferences("MAbeacon",MODE_WORLD_READABLE);

        if(settings.contains(BURL)){
            url = settings.getString(BURL, "");
            WebViewFragment wvf = new WebViewFragment();
            wvf.init(url);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, wvf).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivityForResult(intent, PICK_CONTENT_REQUEST);
        }
        switch(id){
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(intent, PICK_CONTENT_REQUEST);
            case R.id.action_sync:
                ServerSync sync = new ServerSync(this);
                sync.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }

    /**
     * The webview fragment
     */

    public class WebViewFragment extends Fragment {

        private String currentURL;

        public void init(String url){
            currentURL = url;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            Log.d("MAbeacon","wvf on create");

            View v = inflater.inflate(R.layout.fragment_web, container, false);
            if(currentURL != null){
                Log.d("MAbeacon","Current URL = "+currentURL);

                WebView wv = (WebView) v.findViewById(R.id.webPage);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.setWebViewClient(new SwAWebClient());
                wv.loadUrl(currentURL);
            }
            return v;
        }

        public void updateUrl(String url){
            Log.d("MAbeacon","Update url");

            currentURL = url;

            WebView wv = (WebView) getView().findViewById(R.id.webPage);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl(url);

        }

        public class SwAWebClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        }


    }
}
