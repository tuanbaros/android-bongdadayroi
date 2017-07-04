package app.com.bongdadayroi.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nguyen Thanh Tuan on 20/05/2016.
 */
public class DatabaseAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_POSTID = "post_id";
    static final String KEY_JSON = "json";
    public static final String[] ARRAY_KEY_VALUE = new String[]{
        KEY_POSTID, KEY_JSON
    };
    public static final String[] ARRAY_KEY = new String[]{
        KEY_ROWID,
        KEY_POSTID, KEY_JSON
    };
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "videos";
    static final int DATABASE_VERSION = 2;
    static final String DATABASE_CREATE =
        "create table videos (_id integer primary key autoincrement, "
            + "post_id text not null, json text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DatabaseAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS videos");
            onCreate(db);
        }
    }

    //---opens the database---
    public DatabaseAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a row into the database---
    public long insertRow(String[] values) {
        ContentValues initialValues = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            initialValues.put(ARRAY_KEY_VALUE[i], values[i]);
            Log.i("row", "row: " + ARRAY_KEY_VALUE[i] + " - " + values[i]);
        }
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular row---
    public boolean deleteRow(String post_id) {
        return db.delete(DATABASE_TABLE, KEY_POSTID + "=" + post_id, null) > 0;
    }

    //---retrieves all the row---
    public Cursor getAllRows() {
        return db.query(DATABASE_TABLE, ARRAY_KEY, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getRow(long rowId) throws SQLException {
        Cursor mCursor =
            db.query(true, DATABASE_TABLE, ARRAY_KEY, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean checkVideoInDB(String post_id) {
        Cursor mCursor =
            db.query(true, DATABASE_TABLE, ARRAY_KEY, KEY_POSTID + "=" + post_id, null,
                null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                if (mCursor.getString(1).equals(post_id)) {
                    return true;
                }
            } while (mCursor.moveToNext());
        }
        return false;
    }
}
