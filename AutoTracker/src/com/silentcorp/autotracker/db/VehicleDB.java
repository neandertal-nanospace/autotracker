package com.silentcorp.autotracker.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.silentcorp.autotracker.beans.VehicleBean;
import com.silentcorp.autotracker.controls.spinneradapter.NameIdPair;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Vehicle table database helper to contain functionality to access and modify
 * the Vehicles table.
 * 
 * @author neandertal
 */
public class VehicleDB
{
    // Table name
    protected final static String TABLE_VEHICLE = "vehicle";

    // Columns
    private final static String COL_ID = Utils.COL_ID;
    private final static String COL_NAME = "name";
    private final static String COL_COLOR = "color";
    private final static String COL_MAKE = "make";
    private final static String COL_MODEL = "model";
    private final static String COL_YEAR = "year";
    private final static String COL_LICENSE_PLATE = "license_plate";
    private final static String COL_PRIMARY_FUEL = "primary_fuel";
    private final static String COL_SECONDARY_FUEL = "secondary_fuel";
    private final static String COL_PURCHASE_DATE = "purchase_date";
    private final static String COL_PURCHASE_PRICE = "purchase_price";
    private final static String COL_PURCHASE_ODOMETER = "purchase_odometer";
    private final static String COL_PURCHASE_NOTE = "purchase_note";
    private final static String COL_SELL_FLAG = "sell_flag";
    private final static String COL_SELL_DATE = "sell_date";
    private final static String COL_SELL_PRICE = "sell_price";
    private final static String COL_SELL_ODOMETER = "sell_odometer";
    private final static String COL_SELL_NOTE = "sell_note";

    // @formatter:off
    
    private final static String CREATE_TABLE = 
            "CREATE TABLE " + TABLE_VEHICLE + " ( " + 
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
            COL_NAME + " TEXT NOT NULL, " + 
            COL_COLOR + " INTEGER NOT NULL, " + 
            COL_MAKE + " TEXT, " + 
            COL_MODEL + " TEXT, " + 
            COL_YEAR + " INTEGER, " + 
            COL_LICENSE_PLATE + " TEXT, " + 
            COL_PRIMARY_FUEL + " TEXT NOT NULL, " + 
            COL_SECONDARY_FUEL + " TEXT, " + 
            COL_PURCHASE_DATE + " INTEGER, " + 
            COL_PURCHASE_PRICE + " REAL, " + 
            COL_PURCHASE_ODOMETER + " INTEGER, " + 
            COL_PURCHASE_NOTE + " TEXT, " + 
            COL_SELL_FLAG + " INTEGER NOT NULL, " +
            COL_SELL_DATE + " INTEGER, " + 
            COL_SELL_PRICE + " REAL, " + 
            COL_SELL_ODOMETER + " INTEGER, " + 
            COL_SELL_NOTE + " TEXT )";

    private final static String DROP_TABLE = 
            "DROP TABLE IF EXISTS " + TABLE_VEHICLE;

    // Columns to be used for list entries
    public final static String[] LIST_COLUMNS = new String[] {
        COL_ID,
        COL_NAME,
        COL_COLOR,
        COL_MAKE,
        COL_MODEL,
        COL_YEAR,
        COL_LICENSE_PLATE,
        COL_SELL_FLAG
    };

    private final static String[] FUEL_COLUMNS = new String[] {
        COL_PRIMARY_FUEL,
        COL_SECONDARY_FUEL
    };
    
    private final static String[] NAME_COLUMNS = new String[] {
        COL_ID,
        COL_NAME
    };
    
    //Condition to exclude sold vehicles
    private static final String WHERE_NOT_SOLD = COL_SELL_FLAG + " != 1";
    
    //Order for vehicles names list
    private static final String ORDER_NAMES = COL_NAME + " COLLATE NOCASE ASC";
    //Order for vehicles list view
    private static final String ORDER_ALL = COL_SELL_FLAG + " ASC, " + ORDER_NAMES;
    
    
    // @formatter:on

    /**
     * Creates the vehicles table
     * 
     * @param db the DB
     */
    public static void onCreate(SQLiteDatabase db)
    {
        Log.i(VehicleDB.class.getName(), "Creating table: " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Drops and recreates the vehicles table TODO not on production environment
     * 
     * @param db the DB
     * @param oldVersion old DB version
     * @param newVersion new DB version
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(VehicleDB.class.getName(), "Upgrading DB from V:" + oldVersion + " to V:" + newVersion);

        // Drop older table if existed
        Log.i(VehicleDB.class.getName(), "Dropping old table: " + DROP_TABLE);
        db.execSQL(DROP_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Return cursor loader for vehicles list
     */
    public static Loader<Cursor> getCursorLoader(Context context)
    {
        CursorLoader cursorLoader = new CursorLoader(context, DBContentProvider.VEHICLES_URI, VehicleDB.LIST_COLUMNS,
                null, null, ORDER_ALL);
        return cursorLoader;
    }

    /**
     * Read a vehicle from the DB given its ID
     * 
     * @param contentResolver content resolver
     * @param id vehicle ID
     * @param vehicle object to fill, if NULL, new created and returned
     * @return full vehicle object
     * @throws SQLException
     */
    public static VehicleBean readVehicle(Context context, long id, VehicleBean vehicle) throws SQLException
    {
        Uri uri = Uri.parse(DBContentProvider.VEHICLES_URI + "/" + id);
        // Read all columns for the record
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0)
        {
            throw new SQLException("Unable to find vehicle with ID: " + id + " Corrupt DB?");
        }
        // more then one vehicle
        if (cursor.getCount() > 1)
        {
            throw new SQLException("More then one vehicle with ID: " + id + " Corrupt DB?");
        }

        cursor.moveToFirst();

        if (vehicle == null)
        {
            vehicle = new VehicleBean();
        }

        vehicle.setId(id);
        vehicle.setName(Utils.readString(cursor, COL_NAME, TABLE_VEHICLE));
        vehicle.setColor(Utils.readInt(cursor, COL_COLOR, TABLE_VEHICLE));
        vehicle.setMake(Utils.readString(cursor, COL_MAKE, TABLE_VEHICLE));
        vehicle.setModel(Utils.readString(cursor, COL_MODEL, TABLE_VEHICLE));
        vehicle.setYear(Utils.readInt(cursor, COL_YEAR, TABLE_VEHICLE));
        vehicle.setLicensePlate(Utils.readString(cursor, COL_LICENSE_PLATE, TABLE_VEHICLE));
        vehicle.setPrimaryFuel(Utils.readString(cursor, COL_PRIMARY_FUEL, TABLE_VEHICLE));
        vehicle.setSecondaryFuel(Utils.readString(cursor, COL_SECONDARY_FUEL, TABLE_VEHICLE));
        vehicle.setPurchaseDate(Utils.readLong(cursor, COL_PURCHASE_DATE, TABLE_VEHICLE));
        vehicle.setPurchasePrice(Utils.readDouble(cursor, COL_PURCHASE_PRICE, TABLE_VEHICLE));
        vehicle.setPurchaseOdometer(Utils.readInt(cursor, COL_PURCHASE_ODOMETER, TABLE_VEHICLE));
        vehicle.setPurchaseNote(Utils.readString(cursor, COL_PURCHASE_NOTE, TABLE_VEHICLE));
        vehicle.setIsSold(Utils.readBoolean(cursor, COL_SELL_FLAG, TABLE_VEHICLE));
        vehicle.setSellDate(Utils.readLong(cursor, COL_SELL_DATE, TABLE_VEHICLE));
        vehicle.setSellPrice(Utils.readDouble(cursor, COL_SELL_PRICE, TABLE_VEHICLE));
        vehicle.setSellOdometer(Utils.readInt(cursor, COL_SELL_ODOMETER, TABLE_VEHICLE));
        vehicle.setSellNote(Utils.readString(cursor, COL_SELL_NOTE, TABLE_VEHICLE));

        cursor.close();

        return vehicle;
    }

    /**
     * Returns vehicle bean's values as content value map
     * 
     * @param vehicle
     * @return
     */
    private static ContentValues getAsContentValues(VehicleBean vehicle)
    {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, vehicle.getName());
        values.put(COL_COLOR, vehicle.getColor());
        values.put(COL_MAKE, vehicle.getMake());
        values.put(COL_MODEL, vehicle.getModel());
        values.put(COL_YEAR, vehicle.getYear());
        values.put(COL_LICENSE_PLATE, vehicle.getLicensePlate());
        values.put(COL_PRIMARY_FUEL, vehicle.getPrimaryFuel());
        values.put(COL_SECONDARY_FUEL, vehicle.getSecondaryFuel());
        values.put(COL_PURCHASE_DATE, vehicle.getPurchaseDate());
        values.put(COL_PURCHASE_PRICE, vehicle.getPurchasePrice());
        values.put(COL_PURCHASE_ODOMETER, vehicle.getPurchaseOdometer());
        values.put(COL_PURCHASE_NOTE, vehicle.getPurchaseNote());
        values.put(COL_SELL_FLAG, (vehicle.getIsSold() != null && vehicle.getIsSold()) ? 1 : 0);
        values.put(COL_SELL_DATE, vehicle.getSellDate());
        values.put(COL_SELL_PRICE, vehicle.getSellPrice());
        values.put(COL_SELL_ODOMETER, vehicle.getSellOdometer());
        values.put(COL_SELL_NOTE, vehicle.getSellNote());

        return values;
    }

    /**
     * Delete vehicles TODO better sql query
     * 
     * @param resolver
     * @param ids
     */
    public static void deleteVehicles(Context context, Set<Long> ids)
    {
        StringBuilder sb = new StringBuilder();
        String[] args = new String[ids.size()];

        Iterator<Long> iter = ids.iterator();
        int index = 0;
        while (iter.hasNext())
        {
            Long id = iter.next();
            args[index] = Long.toString(id);
            index++;

            sb.append(COL_ID);
            sb.append(" = ?");
            if (iter.hasNext())
            {
                sb.append(" OR ");
            }
        }

        // delete a vehicles records
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(DBContentProvider.VEHICLES_URI, sb.toString(), args);
        
        //delete related events
        EventDB.deleteEventsByVehicleIDs(context, ids);
        
        //delete related notifications
        NotificationDB.deleteNotificationsByVehiclesIds(context, ids);
    }

    /**
     * Create new vehicle
     * 
     * @param contentResolver
     * @param vehicle
     */
    public static void createVehicle(Context context, VehicleBean vehicle)
    {
        ContentValues values = getAsContentValues(vehicle);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(DBContentProvider.VEHICLES_URI, values);
    }

    /**
     * Update existing vehicle
     * 
     * @param contentResolver
     * @param vehicle
     * @throws SQLException
     */
    public static void updateVehicle(Context context, VehicleBean vehicle)
    {
        ContentValues values = getAsContentValues(vehicle);

        Uri uri = Uri.parse(DBContentProvider.VEHICLES_URI + "/" + vehicle.getId());
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.update(uri, values, null, null);
    }

    /**
     * Get primary and secondary fuel of this vehicle
     * 
     * @param contentResolver
     * @param vehicleID
     */
    public static String[] getVehicleFuels(Context context, Long vehicleID)
    {
        if (vehicleID == null)
        {
            return new String[] {};
        }

        // get primary and secondary
        Uri uri = Uri.parse(DBContentProvider.VEHICLES_URI + "/" + vehicleID);
        Cursor c = context.getContentResolver().query(uri, FUEL_COLUMNS, null, null, null);

        if (!c.moveToFirst())
        {
            // This is unexpected TODO
            return new String[] {};
        }

        String primary = c.getString(c.getColumnIndex(COL_PRIMARY_FUEL));
        String secondary = null;
        int secInd = c.getColumnIndex(COL_SECONDARY_FUEL);
        if (!c.isNull(secInd))
        {
            secondary = c.getString(secInd);
        }

        // Do not forget to close
        c.close();

        if (secondary == null)
        {
            return new String[] { primary };
        }
        return new String[] { primary, secondary };
    }

    /**
     * Get vehicles name&ID as a list
     * 
     * @param context
     * @param vehicleID
     * @return
     */
    public static List<NameIdPair> getVehiclesNameIDPair(Context context)
    {
        Cursor c = context.getContentResolver().query(DBContentProvider.VEHICLES_URI, NAME_COLUMNS, WHERE_NOT_SOLD,
                null, ORDER_NAMES);

        List<NameIdPair> result = new ArrayList<NameIdPair>(c.getCount());
        while (c.moveToNext())
        {
            long id = c.getLong(c.getColumnIndex(COL_ID));
            String name = c.getString(c.getColumnIndex(COL_NAME));

            NameIdPair pair = new NameIdPair(name, id);
            result.add(pair);
        }

        // Do not forget to close
        c.close();

        return result;
    }

    /**
     * Get vehicle' name
     * 
     * @param vehicleID
     * @return
     */
    public static String getVehicleName(Context context, Long id)
    {
        if (id == null)
        {
            return null;
        }

        // get primary and secondary
        Uri uri = Uri.parse(DBContentProvider.VEHICLES_URI + "/" + id);
        Cursor c = context.getContentResolver().query(uri, NAME_COLUMNS, null, null, null);

        if (!c.moveToFirst())
        {
            // This is unexpected TODO
            return null;
        }

        String name = c.getString(c.getColumnIndexOrThrow(COL_NAME));

        // Do not forget to close
        c.close();

        return name;
    }

    /**
     * Checks if there are any not sold vehicles.
     */
    public static boolean hasActiveVehicles(Context context)
    {
        Uri uri = Uri.parse(DBContentProvider.VEHICLES_URI + "/COUNT");
        Cursor c = context.getContentResolver().query(uri, null, WHERE_NOT_SOLD, null, null);

        if (!c.moveToFirst())
        {
            // This is unexpected TODO
            return false;
        }

        int count = c.getInt(0);

        // Do not forget to close
        c.close();

        return count > 0;
    }

    /**
     * Return if the current vehicle is sold
     * 
     * @param c
     * @return
     */
    public static Boolean getSellFlag(Cursor c)
    {
        return Utils.readBoolean(c, COL_SELL_FLAG, TABLE_VEHICLE);
    }

    /**
     * Construct header text - name of vehicle
     * 
     * @param c DB cursor
     * @return header text
     */
    public static String constructHeaderText(Cursor c)
    {
        return Utils.readString(c, COL_NAME, TABLE_VEHICLE);
    }

    /**
     * Description of the vehicle is Make, Model, year, License plate (if these
     * are not NULL)
     * 
     * @param c DB cursor
     * @return description text
     */
    public static String constructDescriptionText(Cursor c)
    {
        StringBuilder sb = new StringBuilder();
        String make = Utils.readString(c, COL_MAKE, TABLE_VEHICLE);
        String model = Utils.readString(c, COL_MODEL, TABLE_VEHICLE);
        Integer year = Utils.readInt(c, COL_YEAR, TABLE_VEHICLE);
        String licensePlate = Utils.readString(c, COL_LICENSE_PLATE, TABLE_VEHICLE);

        boolean comma = false;
        if (make != null)
        {
            sb.append(make);
            comma = true;
        }

        if (model != null)
        {
            if (comma)
            {
                sb.append(", ");
            }
            sb.append(model);
            comma = true;
        }

        if (year != null)
        {
            if (comma)
            {
                sb.append(", ");
            }
            sb.append(year);
            comma = true;
        }

        if (licensePlate != null)
        {
            if (comma)
            {
                sb.append(", ");
            }
            sb.append(licensePlate);
            comma = true;
        }

        return sb.toString();
    }
}
