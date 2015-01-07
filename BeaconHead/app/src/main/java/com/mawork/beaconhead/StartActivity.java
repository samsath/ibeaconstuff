package com.mawork.beaconhead;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class StartActivity extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 2;
    static final int PICK_CONTENT_REQUEST = 5;
    public static final String CURL = "contentURL";
    static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;


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
            startService(new Intent(this, beaconSearch.class));
            ServerSync sync = new ServerSync(this);
            sync.execute();
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
                startService(new Intent(this, ServerSync.class));
                startService(new Intent(this, beaconSearch.class));
            }
        }
    }

}
