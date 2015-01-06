package com.mawork.beaconhead;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class Settings extends Activity {

    /* This activity will be for setting the url of the resource */

    static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    public static final String CURL = "contentURL";

    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = this.getSharedPreferences("MAbeacon",MODE_WORLD_READABLE);

        url = (EditText) findViewById(R.id.etURL);

        if(settings.contains(CURL)){
            url.setText(settings.getString(CURL,""));
        }
    }

    public void settingsSaveClick(View view){
        String curl = url.getText().toString();
        Log.d("MA",curl);

        editor = settings.edit();

        editor.putString(CURL,curl);

        editor.apply();

        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
}
