package com.mawork.androidbeacon;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

    public static final String LOG = "MA";

    //database info
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MAbeacon";

    //table name
    public static final String TABLE_BEACON = "beacon";

    //common columns
    public static final String KEY_ID = "id";

    //beacon table
    public static final String KEY_UUID = "uuid";
    public static final String KEY_MAJOR = "major";
    public static final String KEY_MINOR = "minor";


    //beacon table creator

    private static final String CREATE_TABLE_BEACON = "CREATE TABLE "+ TABLE_BEACON +
                                                      " ( "+KEY_ID+" INTEGER PRIMARY KEY, " +
                                                      KEY_UUID+" VARCHAR(32), "+
                                                      KEY_MAJOR+" INTEGER, "+
                                                      KEY_MINOR+" INTEGER" +
                                                      ")";

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG, "Database started");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, "Database created");
        db.execSQL(CREATE_TABLE_BEACON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_BEACON);
        onCreate(db);
    }
}
