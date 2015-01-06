package com.mawork.beaconhead;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IbeaconManager";

    public static final String TABLE_BEACON = "beacon";

    public static final String KEY_ID = "id";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_MAJOR = "major";
    public static final String KEY_MINOR = "minor";
    public static final String KEY_URL = "url";

    private static final String CREATE_TABLE_BEACON = "CREATE TABLE " + TABLE_BEACON +
            "(" + KEY_ID +" INTEGER PRIMARY KEY," +
            KEY_UUID + " BLOB," +
            KEY_MAJOR +" INTEGER," +
            KEY_MINOR + " INTEGER, " +
            KEY_URL + " TEXT" +
            ")";

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("MA","Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MA","Database Create");
        db.execSQL(CREATE_TABLE_BEACON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("MA","Database update");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACON);

        onCreate(db);
    }

    public long createBeacon(Beacon beck){

        SQLiteDatabase db = this.getWritableDatabase();
        long beacon_id;

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_BEACON+" WHERE "+KEY_URL+" = '"+beck.url+"';",null);
        if(c.moveToFirst()){
            beacon_id = c.getInt(c.getColumnIndex(KEY_ID));
        }else{
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, beck.uuid);
            values.put(KEY_MAJOR, beck.major);
            values.put(KEY_MINOR, beck.minor);
            values.put(KEY_URL, beck.url);

            beacon_id = db.insert(TABLE_BEACON, null, values);
        }

        return beacon_id;
    }

    public String getURL(byte[] uuid, int major, int minor){
        SQLiteDatabase db = this.getWritableDatabase();

        String url;

        String selectQuery = "SELECT"+ KEY_URL +" FROM " + TABLE_BEACON + " WHERE " + KEY_UUID + " = '"+ uuid +
                "' AND WHERE " + KEY_MAJOR + " = '"+major+"' AND WHERE "+
                KEY_MINOR +" = '"+minor+"'";

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()>0){
            c.moveToFirst();
            url = c.getString(c.getColumnIndex(KEY_URL));
        } else {
            return null;
        }

        return url;
    }

    public List<Beacon> getAllBeacon(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Beacon> beacons = new ArrayList<Beacon>();
        String selectQuery = "SELECT * FROM " + TABLE_BEACON;

        Cursor c = db.rawQuery(selectQuery,null);

        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Beacon bec = new Beacon();
                    bec.id = c.getInt(c.getColumnIndex(KEY_ID));
                    bec.uuid = c.getBlob(c.getColumnIndex(KEY_UUID));
                    bec.major = c.getInt(c.getColumnIndex(KEY_MAJOR));
                    bec.minor = c.getInt(c.getColumnIndex(KEY_MINOR));
                    bec.url = c.getString(c.getColumnIndex(KEY_URL));
                } while(c.moveToNext());
            }
        }

        return beacons;
    }
}
