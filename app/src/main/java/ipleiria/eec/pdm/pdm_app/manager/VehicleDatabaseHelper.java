package ipleiria.eec.pdm.pdm_app.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class VehicleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vehicles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_VEHICLES = "vehicles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_PHOTO_URI = "photo_uri";

    public VehicleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VEHICLES_TABLE = "CREATE TABLE " + TABLE_VEHICLES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DETAILS + " TEXT,"
                + COLUMN_PHOTO_URI + " TEXT" + ")";
        db.execSQL(CREATE_VEHICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);
        onCreate(db);
    }

    public void addVehicle(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, vehicle.getName());
        values.put(COLUMN_DETAILS, vehicle.getDetails());
        values.put(COLUMN_PHOTO_URI, vehicle.getPhotoUri());
        db.insert(TABLE_VEHICLES, null, values);
        db.close();
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VEHICLES, null);
        if (cursor.moveToFirst()) {
            do {
                Vehicle vehicle = new Vehicle(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_URI))
                );
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vehicleList;
    }

    public void deleteVehicle(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VEHICLES, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }
}