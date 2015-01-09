package com.mawork.beaconhead;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easibeacon.protocol.IBeacon;
import com.easibeacon.protocol.IBeaconListener;
import com.easibeacon.protocol.IBeaconProtocol;
import com.easibeacon.protocol.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements IBeaconListener {


    private static final int REQUEST_BLUETOOTH_ENABLE = 1;

    private static ArrayList<IBeacon> _beacons;
    private ArrayAdapter<IBeacon> _beaconsAdapter;
    private static IBeaconProtocol _ibp;

    public String url;
    static final int PICK_CONTENT_REQUEST = 5;
    public List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        if(_beacons == null)
            _beacons = new ArrayList<IBeacon>();
        _beaconsAdapter = new ArrayAdapter<IBeacon>(this, android.R.layout.simple_list_item_2, android.R.id.text1, _beacons){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                IBeacon beacon = _beacons.get(position);

                text1.setText(beacon.getUuidHexStringDashed());
                text2.setText("Major: " + beacon.getMajor() + " Minor: " + beacon.getMinor() + " Distance: " + beacon.getProximity() + "m.");
                return view;
            }


            public void onTouch(View currentView, MotionEvent event){
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        };



        _ibp = IBeaconProtocol.getInstance(this);
        scanBeacons();
        _ibp.setListener(this);

    }

    @Override
    protected void onStop() {
        _ibp.stopScan();
        super.onStop();
    }

    private void scanBeacons(){
        // Check Bluetooth every time
        Log.i(Utils.LOG_TAG,"Scanning");

        if(!IBeaconProtocol.configureBluetoothAdapter(this)){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_ENABLE );
        }else{
            if(_ibp.isScanning())
                _ibp.stopScan();
            _ibp.reset();
            _ibp.startScan();
        }
    }



    // The following methods implement the IBeaconListener interface

    @Override
    public void beaconFound(IBeacon ibeacon) {
        _beacons.add(ibeacon);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _beaconsAdapter.notifyDataSetChanged();
            }
        });
        // here add the service so when it click it will load the item

    }

    @Override
    public void enterRegion(IBeacon ibeacon) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitRegion(IBeacon ibeacon) {
        // TODO Auto-generated method stub

    }

    @Override
    public void operationError(int status) {
        Log.i(Utils.LOG_TAG, "Bluetooth error: " + status);

    }

    @Override
    public void searchState(int state) {
        if(state == IBeaconProtocol.SEARCH_STARTED){
            //startRefreshAnimation();

        }else if (state == IBeaconProtocol.SEARCH_END_EMPTY || state == IBeaconProtocol.SEARCH_END_SUCCESS){
            //stopRefreshAnimation();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_BLUETOOTH_ENABLE){
            if(resultCode == Activity.RESULT_OK){
                scanBeacons();
            }
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
    public static class WebViewFragment extends Fragment {

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
