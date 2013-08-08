package com.silentcorp.autotracker.db;

import java.util.Iterator;
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

import com.silentcorp.autotracker.beans.EventBean;
import com.silentcorp.autotracker.utils.EventType;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Event table database helper to contain functionality to access and modify the
 * Event table.
 * 
 * @author neandertal
 */
public class EventDB
{
    // Table name
    protected final static String TABLE_EVENT = "event";

    // Columns
    private final static String COL_ID = Utils.COL_ID;
    private final static String COL_VEHICLE_REF = "vehicle_ref";
    private final static String COL_EVENT_DATE = "event_date";
    private final static String COL_EVENT_TYPE = "event_type";
    private final static String COL_COST = "cost";
    private final static String COL_ODOMETER = "odometer";
    private final static String COL_NOTE = "note";
    private final static String COL_FUEL = "fuel";
    private final static String COL_QUANTITY = "quantity";
    private final static String COL_PRICE = "price";
    private final static String COL_DESCRIPRION = "description";
    private final static String COL_PLACE = "place";

    // @formatter:off
    
    private final static String CREATE_TABLE = 
            "CREATE TABLE " + TABLE_EVENT + " ( " + 
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
            COL_VEHICLE_REF + " INTEGER, " + 
            COL_EVENT_DATE + " INTEGER NOT NULL, " + 
            COL_EVENT_TYPE + " TEXT NOT NULL, " + 
            COL_COST + " REAL NOT NULL, " + 
            COL_ODOMETER + " INTEGER, " + 
            COL_NOTE + " TEXT, " + 
            COL_FUEL + " TEXT, " + 
            COL_QUANTITY + " REAL, " + 
            COL_PRICE + " REAL, " + 
            COL_DESCRIPRION + " TEXT, " + 
            COL_PLACE + " TEXT )";

    private final static String DROP_TABLE = 
            "DROP TABLE IF EXISTS " + TABLE_EVENT;

    // Columns to be used for list entries
    public final static String[] LIST_COLUMNS = new String[] {
        COL_ID,
        COL_VEHICLE_REF,
        COL_EVENT_DATE,
        COL_EVENT_TYPE,
        COL_COST,
        COL_FUEL,
        COL_QUANTITY,
        COL_PRICE,
        COL_DESCRIPRION };

    //Order by date - most recent on top
    private static final String ORDER = COL_EVENT_DATE + " DESC";
    
    // @formatter:on

    /**
     * Creates the event table
     * 
     * @param db the DB
     */
    public static void onCreate(SQLiteDatabase db)
    {
        Log.i(VehicleDB.class.getName(), "Creating table: " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Drops and recreates the event table TODO not on production environment
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
     * Get cursor loader for events list activity
     * 
     * @param context
     * @return
     */
    public static Loader<Cursor> getCursorLoader(Context context)
    {
        CursorLoader cursorLoader = new CursorLoader(context, DBContentProvider.EVENTS_URI, EventDB.LIST_COLUMNS, null,
                null, ORDER);
        return cursorLoader;
    }

    /**
     * Get cursor to filter Fuel, Maintenance, Repair, Payment events for list
     * adapter
     * 
     * @param contentResolver
     * @param hideFE if true hide Fuel events
     * @param hideME if true hide Maintenance events
     * @param hideRE if true hide Repair events
     * @param hidePE if true hide Payment events
     * @return new Cursor
     */
    public static Cursor getFilteredCursor(Context context, boolean hideFE, boolean hideME, boolean hideRE,
            boolean hidePE)
    {
        // TODO better sql ?
        StringBuilder whereQ = new StringBuilder();
        boolean hasPrevious;
        if (hideFE && hideME && hideRE && hidePE)
        {
            whereQ.append(COL_EVENT_TYPE);
            whereQ.append(" IS NULL");
        }
        else
        {
            hasPrevious = false;
            if (!hideFE)
            {
                whereQ.append(COL_EVENT_TYPE);
                whereQ.append("='");
                whereQ.append(EventType.FUEL_EVENT.toString());
                whereQ.append("'");
                hasPrevious = true;
            }
            if (!hideME)
            {
                if (hasPrevious)
                {
                    whereQ.append(" OR ");
                }
                whereQ.append(COL_EVENT_TYPE);
                whereQ.append("='");
                whereQ.append(EventType.MAINTENANCE_EVENT.toString());
                whereQ.append("'");
                hasPrevious = true;
            }
            if (!hideRE)
            {
                if (hasPrevious)
                {
                    whereQ.append(" OR ");
                }
                whereQ.append(COL_EVENT_TYPE);
                whereQ.append("='");
                whereQ.append(EventType.REPAIR_EVENT.toString());
                whereQ.append("'");
                hasPrevious = true;
            }
            if (!hidePE)
            {
                if (hasPrevious)
                {
                    whereQ.append(" OR ");
                }
                whereQ.append(COL_EVENT_TYPE);
                whereQ.append("='");
                whereQ.append(EventType.PAYMENT_EVENT.toString());
                whereQ.append("'");
                hasPrevious = true;
            }
        }
        // Get filtered cursor
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(DBContentProvider.EVENTS_URI, LIST_COLUMNS, whereQ.toString(), null,
                ORDER);
        return cursor;
    }

    /**
     * Read an event from the DB given its ID
     * 
     * @param contentResolver content resolver
     * @param id event ID
     * @param event object to fill, if NULL, new created and returned
     * @return full event object
     * @throws SQLException
     */
    public static EventBean readEvent(Context context, long id, EventBean event) throws SQLException
    {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse(DBContentProvider.EVENTS_URI + "/" + id);
        // Read all columns for the record
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0)
        {
            throw new SQLException("Unable to find event with ID: " + id + " Corrupt DB?");
        }
        // more then one event
        if (cursor.getCount() > 1)
        {
            throw new SQLException("More then one event with ID: " + id + " Corrupt DB?");
        }

        cursor.moveToFirst();

        if (event == null)
        {
            event = new EventBean();
        }

        event.setId(id);
        event.setVehicleRef(Utils.readLong(cursor, COL_VEHICLE_REF, TABLE_EVENT));
        event.setEventDate(Utils.readLong(cursor, COL_EVENT_DATE, TABLE_EVENT));
        event.setEventType(EventType.parse(Utils.readString(cursor, COL_EVENT_TYPE, TABLE_EVENT)));
        event.setCost(Utils.readDouble(cursor, COL_COST, TABLE_EVENT));
        event.setOdometer(Utils.readInt(cursor, COL_ODOMETER, TABLE_EVENT));
        event.setNote(Utils.readString(cursor, COL_NOTE, TABLE_EVENT));
        event.setFuel(Utils.readString(cursor, COL_FUEL, TABLE_EVENT));
        event.setQuantity(Utils.readDouble(cursor, COL_QUANTITY, TABLE_EVENT));
        event.setPrice(Utils.readDouble(cursor, COL_PRICE, TABLE_EVENT));
        event.setDescription(Utils.readString(cursor, COL_DESCRIPRION, TABLE_EVENT));
        event.setPlace(Utils.readString(cursor, COL_PLACE, TABLE_EVENT));

        cursor.close();

        return event;
    }

    /**
     * Returns event bean's values as content value map
     * 
     * @param event
     * @return
     */
    private static ContentValues getAsContentValues(EventBean event)
    {
        ContentValues values = new ContentValues();
        values.put(COL_VEHICLE_REF, event.getVehicleRef());
        values.put(COL_EVENT_DATE, event.getEventDate());
        values.put(COL_EVENT_TYPE, event.getEventType().toString());
        values.put(COL_COST, event.getCost());
        values.put(COL_ODOMETER, event.getOdometer());
        values.put(COL_NOTE, event.getNote());
        values.put(COL_FUEL, event.getFuel());
        values.put(COL_QUANTITY, event.getQuantity());
        values.put(COL_PRICE, event.getPrice());
        values.put(COL_DESCRIPRION, event.getDescription());
        values.put(COL_PLACE, event.getPlace());

        return values;
    }

    /**
     * Delete events TODO better sql query
     * 
     * @param resolver
     * @param ids
     */
    public static void deleteEvents(Context context, Set<Long> ids)
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

        // delete records
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(DBContentProvider.EVENTS_URI, sb.toString(), args);
    }

    /**
     * Delete events which use these vehicles IDs TODO better sql query
     * 
     * @param resolver
     * @param ids
     */
    public static void deleteEventsByVehicleIDs(Context context, Set<Long> vehicleIds)
    {
        StringBuilder sb = new StringBuilder();
        String[] args = new String[vehicleIds.size()];

        Iterator<Long> iter = vehicleIds.iterator();
        int index = 0;
        while (iter.hasNext())
        {
            Long id = iter.next();
            args[index] = Long.toString(id);
            index++;

            sb.append(COL_VEHICLE_REF);
            sb.append(" = ?");
            if (iter.hasNext())
            {
                sb.append(" OR ");
            }
        }

        // delete records
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(DBContentProvider.EVENTS_URI, sb.toString(), args);
    }

    /**
     * Create new event
     * 
     * @param contentResolver
     * @param event
     */
    public static void createEvent(Context context, EventBean event)
    {
        ContentValues values = getAsContentValues(event);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(DBContentProvider.EVENTS_URI, values);
    }

    /**
     * Update existing events
     * 
     * @param contentResolver
     * @param event
     * @throws SQLException
     */
    public static void updateEvent(Context context, EventBean event) throws SQLException
    {
        ContentValues values = getAsContentValues(event);

        Uri uri = Uri.parse(DBContentProvider.EVENTS_URI + "/" + event.getId());

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.update(uri, values, null, null);
    }

    /**
     * Return event type of the current record in the cursor
     * 
     * @param c
     * @return
     */
    public static EventType getEventType(Cursor c)
    {
        String type = c.getString(c.getColumnIndex(COL_EVENT_TYPE));
        return EventType.parse(type);
    }

    /**
     * Return event date of the current record in the cursor
     * 
     * @param c
     * @return
     */
    public static Long getEventDate(Cursor c)
    {
        Long eventDate = Utils.readLong(c, COL_EVENT_DATE, TABLE_EVENT);
        return eventDate;
    }

    /**
     * Construct header text
     * 
     * @param c DB cursor
     * @return header text
     */
    public static String constructHeaderText(Cursor c, Context context)
    {
        // / event type
        String sType = c.getString(c.getColumnIndex(COL_EVENT_TYPE));
        EventType type = EventType.parse(sType);

        if (EventType.FUEL_EVENT.equals(type))
        {
            String sFuel = c.getString(c.getColumnIndexOrThrow(COL_FUEL));
            return Utils.getLocalizedFuelTypeLabel(sFuel, context);
        }
        else
        {
            return c.getString(c.getColumnIndexOrThrow(COL_DESCRIPRION));
        }
    }

    /**
     * Description of the event
     * 
     * @param c DB cursor
     * @return description text
     */
    public static String constructDescriptionText(Cursor c, Context context)
    {
        // event type
        String sType = c.getString(c.getColumnIndex(COL_EVENT_TYPE));
        EventType type = EventType.parse(sType);

        StringBuilder sb = new StringBuilder();

        // Vehicle Name
        Long vRef = Utils.readLong(c, COL_VEHICLE_REF, TABLE_EVENT);
        String vName = VehicleDB.getVehicleName(context, vRef);
        if (vName != null)
        {
            sb.append(vName);
        }

        if (EventType.FUEL_EVENT.equals(type))
        {
            String fuelType = null;

            // Quantity
            Double quantity = Utils.readDouble(c, COL_QUANTITY, TABLE_EVENT);
            if (quantity != null)
            {
                fuelType = Utils.readString(c, COL_FUEL, TABLE_EVENT);
                String sQuantity = Utils.formatQuantity(quantity, context, fuelType);
                if (sb.length() > 0)
                {
                    sb.append(", ");
                }
                sb.append(sQuantity);
            }

            // Price per unit
            Double price = Utils.readDouble(c, COL_PRICE, TABLE_EVENT);
            if (price != null)
            {
                if (fuelType == null)
                {
                    fuelType = Utils.readString(c, COL_FUEL, TABLE_EVENT);
                }
                String sPrice = Utils.formatPricePerUnit(price, context, fuelType);
                if (sb.length() > 0)
                {
                    sb.append(", ");
                }
                sb.append(sPrice);
            }
        }

        return sb.toString();
    }

    /**
     * Value for the event = cost
     * 
     * @param c DB cursor
     * @return description text
     */
    public static String constructValueText(Cursor c, Context context)
    {
        // cost
        Double cost = Utils.readDouble(c, COL_COST, TABLE_EVENT);
        if (cost == null)
        {
            // Should never happen
            return null;
        }

        return Utils.formatCost(cost, context);
    }

}
