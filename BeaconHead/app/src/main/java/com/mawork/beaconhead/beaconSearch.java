package com.mawork.beaconhead;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.easibeacon.protocol.IBeacon;
import com.easibeacon.protocol.IBeaconListener;
import com.easibeacon.protocol.IBeaconProtocol;


import java.util.Timer;
import java.util.TimerTask;

public class beaconSearch extends Service implements IBeaconListener{


    public static final String BURL = "beaconURL";
    private IBeaconProtocol ibp;

    public Database db;

    public Context context;

    public beaconSearch() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        context = getApplicationContext();

        Database db = new Database(context);

        ibp = IBeaconProtocol.getInstance(context);
        ibp.setListener(this);

        //BeaconScanFor();

        scanBeacons();

        Log.d("MAbeacon","Search Service Start");
        return null;
    }

    public void BeaconScanFor(){
        TimerTask searchIbeaconTask = new TimerTask(){
            @Override
            public void run() {
                scanBeacons();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(searchIbeaconTask, 1000,2*60*1000);
    }


    private void scanBeacons(){
        // check bluetooth everytime

        ibp = IBeaconProtocol.getInstance(context);
        // can filter the UUID so only some will appear
        //ibp.setScanUUID(UUID);

        ibp.setListener(this);
        if(ibp.isScanning()){
            ibp.scanIBeacons(false);
        }
        ibp.reset();
        ibp.scanIBeacons(true);

    }


    @Override
    public void enterRegion(IBeacon ibeacon) {

        Log.i("MAbeacon","In region of "+ibeacon.toString());

        String url;

        url = db.getURL(ibeacon.getUuid().toString(),ibeacon.getMajor(),ibeacon.getMinor());

        if(!url.isEmpty()){
            // If the beacon is found then the head pops up
            Intent intent = new Intent(context, headService.class);
            intent.putExtra("URL",url);
            startService(intent);

        }
    }

    @Override
    public void exitRegion(IBeacon ibeacon) {
        scanBeacons();
    }

    @Override
    public void beaconFound(IBeacon ibeacon) {
        Log.i("MAbeacon","FOUND "+ibeacon.toString());
    }

    @Override
    public void searchState(int state) {
        Log.i("MAbeacon","state = "+state);
    }

    @Override
    public void operationError(int status) {
        Log.d("MAbeacon","Bluetoother error:"+status);
    }

    @Override
    public void onDestroy(){
        ibp.stopScan();
        Log.d("MAbeacon", "Service Destroyed");
    }
}
