package com.mawork.beaconhead;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class beaconAdaptor extends BaseAdapter{
    // This is the adaptor which controls the beacons on the Main activity

    public static final int LEFT = 16;
    private Context context;
    private List<Beacon> beaconList = new ArrayList<>();

    public beaconAdaptor(Context contexts, String urls){
        context = contexts;
        String url = urls;
        Log.e("MAbeacon",url);
        beaconList.addAll(getbeacons());
    }

    @Override
    public int getCount() {
        return beaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // this is the view of the beacon so be able to see what it is

        BeaconView bv;

        if(convertView == null){
            bv = new BeaconView(context);
            bv.setLayoutParams(new GridView.LayoutParams(480,410));
            bv.setPadding(LEFT,LEFT,LEFT,LEFT);
        }else{
            bv = (BeaconView) convertView;
        }

        // add the beacon information here
        bv.setBeaconTilte();
        bv.setBeaconImage();
        bv.setBeaconLink();
        bv.setId(position);

        bv.setOnClickListener(new MyOnClickListener(position));
        bv.setOnLongClickListener(new MyLongClickListener(position));

        return bv;
    }

    public List<Beacon>getbeacons(){

        return null;
    }

    private class MyOnClickListener implements View.OnClickListener {
        public MyOnClickListener(int position) {
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class MyLongClickListener implements View.OnLongClickListener {
        public MyLongClickListener(int position) {
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
