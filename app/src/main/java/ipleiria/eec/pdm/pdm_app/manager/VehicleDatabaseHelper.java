
// VehicleDatabaseHelper.java
package ipleiria.eec.pdm.pdm_app.manager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Helper para gerenciar o banco de dados de veículos e manutensoes.
 */
public class VehicleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vehicles.db";
    private static final int DATABASE_VERSION = 3;

    // Vehicle Table
    private static final String TABLE_VEHICLES = "vehicles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_PHOTO_URI = "photo_uri";
    private static final String COLUMN_LICENSE_PLATE = "license_plate";

    // Maintenance Table
    private static final String TABLE_MAINTENANCE = "maintenance";
    private static final String COLUMN_MAINTENANCE_ID = "maintenance_id";
    private static final String COLUMN_VEHICLE_ID = "vehicle_id";
    private static final String COLUMN_SERVICE_TYPE = "service_type";
    private static final String COLUMN_SERVICE_DATE = "service_date";
    private static final String COLUMN_SERVICE_COST = "service_cost";

    /**
     * Construtor da classe VehicleDatabaseHelper.
     *
     * @param context o contexto
     */
    public VehicleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Cria as tabelas do banco de dados.
     *
     * @param db o banco de dados
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VEHICLES_TABLE = "CREATE TABLE " + TABLE_VEHICLES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DETAILS + " TEXT,"
                + COLUMN_PHOTO_URI + " TEXT,"
                + COLUMN_LICENSE_PLATE + " TEXT" + ")";
        db.execSQL(CREATE_VEHICLES_TABLE);

        String CREATE_MAINTENANCE_TABLE = "CREATE TABLE " + TABLE_MAINTENANCE + "("
                + COLUMN_MAINTENANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_VEHICLE_ID + " INTEGER,"
                + COLUMN_SERVICE_TYPE + " TEXT,"
                + COLUMN_SERVICE_DATE + " TEXT,"
                + COLUMN_SERVICE_COST + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_VEHICLE_ID + ") REFERENCES " + TABLE_VEHICLES + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_MAINTENANCE_TABLE);
    }

    /**
     * Atualiza o banco de dados.
     * @param db database
     * @param oldVersion database velha
     * @param newVersion database nova
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINTENANCE);
        onCreate(db);
    }

    /**
     * Adiciona um novo veículo ao banco de dados.
     *
     * @param vehicle o veículo a ser adicionado
     */
    public void addVehicle(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, vehicle.getName());
        values.put(COLUMN_DETAILS, vehicle.getDetails());
        values.put(COLUMN_PHOTO_URI, vehicle.getPhotoUri());
        values.put(COLUMN_LICENSE_PLATE, vehicle.getLicensePlate());

        db.insert(TABLE_VEHICLES, null, values);
        db.close();
    }

    /**
     * Retorna uma lista com todos os veículos no banco de dados.
     *
     * @return a lista de veículos
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_VEHICLES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Vehicle vehicle = new Vehicle(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_URI)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LICENSE_PLATE))
                );
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return vehicleList;
    }

    /**
     * Exclui um veículo do banco de dados pelo nome.
     *
     * @param vehicleId o id do veículo a ser excluído
     */
    public void deleteVehicle(int vehicleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VEHICLES, COLUMN_ID + " = ?", new String[]{String.valueOf(vehicleId)});
        db.close();
    }


    /**
     * Atualiza um veículo no banco de dados.
     * @param vehicle veículo a ser atualizado
     * @return true se a atualização foi bem sucedida, false caso contrário
     */
    public boolean updateVehicle(Vehicle vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, vehicle.getName());
        values.put(COLUMN_DETAILS, vehicle.getDetails());
        values.put(COLUMN_LICENSE_PLATE, vehicle.getLicensePlate());
        values.put(COLUMN_PHOTO_URI, vehicle.getPhotoUri());

        int result = db.update(TABLE_VEHICLES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(vehicle.getVehicleId())});
        db.close();
        return result > 0;
    }
    /* Maintenance */

    /**
     * Exclui um registro de manutenção do banco de dados.
     * @param maintenanceId o id da manutenção a ser excluída
     */
    public void deleteMaintenance(int maintenanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MAINTENANCE, COLUMN_MAINTENANCE_ID + " = ?", new String[]{String.valueOf(maintenanceId)});
        db.close();
    }

    /**
     * Adiciona um novo registro de manutenção ao banco de dados.
     * @param maintenance o registro de manutenção a ser adicionado
     */
    public void insertMaintenance(Maintenance maintenance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_ID, maintenance.getVehicleId());
        values.put(COLUMN_SERVICE_TYPE, maintenance.getServiceType());
        values.put(COLUMN_SERVICE_DATE, maintenance.getServiceDate());
        values.put(COLUMN_SERVICE_COST, maintenance.getServiceCost());
        db.insert(TABLE_MAINTENANCE, null, values);
        db.close();
    }

    /**
     * Atualiza um registro de manutenção no banco de dados.
     */
    @SuppressLint("Range")
    public List<Maintenance> getAllMaintenance() {
        List<Maintenance> maintenanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MAINTENANCE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Maintenance maintenance = new Maintenance(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_VEHICLE_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_TYPE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_COST))
                );
                maintenance.setMaintenanceId(cursor.getInt(cursor.getColumnIndex(COLUMN_MAINTENANCE_ID))); // Add this line
                maintenanceList.add(maintenance);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return maintenanceList;
    }

    /**
     * Retorna uma lista de manutenções para um veículo específico.
     * @param vehicleId o id do veículo
     * @return a lista de manutenções
     */
    public List<Maintenance> getMaintenanceByVehicleId(int vehicleId) {
        List<Maintenance> maintenanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MAINTENANCE, null, COLUMN_VEHICLE_ID + " = ?",
                new String[]{String.valueOf(vehicleId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Maintenance maintenance = new Maintenance(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_VEHICLE_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_TYPE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_COST))
                );
                maintenanceList.add(maintenance);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return maintenanceList;
    }
}
