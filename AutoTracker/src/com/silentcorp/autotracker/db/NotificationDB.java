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

import com.silentcorp.autotracker.beans.NotificationBean;

/**
 * Notifications table database helper to contain functionality to access and
 * modify the Notifications table.
 * 
 * @author neandertal
 */
public class NotificationDB
{

    // Table name
    protected final static String TABLE_NOTIFICATION = "notification";

    // Columns
    private final static String COL_ID = Utils.COL_ID;
    private final static String COL_ACTIVITY = "activity";
    private final static String COL_VEHICLE_REF = "vehicle_ref";
    private final static String COL_ENABLED = "enabled";
    private final static String COL_NOTE = "note";
    private final static String COL_DATE_DUE = "date_due";
    private final static String COL_DATE_REPEAT = "date_repeat";
    private final static String COL_DATE_ADVANCE = "date_advance";
    private final static String COL_PERIOD_LAST = "period_last";
    private final static String COL_PERIOD_REPEAT = "period_repeat";
    private final static String COL_PERIOD_ADVANCE = "period_advance";
    private final static String COL_DISTANCE_LAST = "distance_last";
    private final static String COL_DISTANCE_REPEAT = "distance_repeat";
    private final static String COL_DISTANCE_ADVANCE = "distance_advance";

    // @formatter:off

    private final static String CREATE_TABLE = 
            "CREATE TABLE " + TABLE_NOTIFICATION + " ( " +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
            COL_ACTIVITY + " TEXT NOT NULL, " + 
            COL_VEHICLE_REF + " INTEGER, " + 
            COL_ENABLED + " INTEGER NOT NULL, " + 
            COL_NOTE + " TEXT, " + 
            COL_DATE_DUE + " INTEGER, " + 
            COL_DATE_REPEAT + " INTEGER, " + 
            COL_DATE_ADVANCE + " INTEGER, " + 
            COL_PERIOD_LAST + " INTEGER, " + 
            COL_PERIOD_REPEAT + " INTEGER, " + 
            COL_PERIOD_ADVANCE + " INTEGER, " + 
            COL_DISTANCE_LAST + " INTEGER, " + 
            COL_DISTANCE_REPEAT + " INTEGER, " + 
            COL_DISTANCE_ADVANCE + " INTEGER )";

    private final static String DROP_TABLE = 
            "DROP TABLE IF EXISTS " + TABLE_NOTIFICATION;

 // Columns to be used for list entries
    public final static String[] LIST_COLUMNS = new String[] {
        COL_ID,
        COL_ACTIVITY,
        COL_VEHICLE_REF,
        COL_DATE_DUE,
        COL_PERIOD_LAST,
        COL_PERIOD_REPEAT,
        COL_DISTANCE_LAST,
        COL_PERIOD_REPEAT };
    
    // @formatter:on

    /**
     * Creates the notification table
     * 
     * @param db the DB
     */
    public static void onCreate(SQLiteDatabase db)
    {
        Log.i(NotificationDB.class.getName(), "Creating table: " + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Drops and recreates the notifications table TODO not on production
     * environment
     * 
     * @param db the DB
     * @param oldVersion old DB version
     * @param newVersion new DB version
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(NotificationDB.class.getName(), "Upgrading DB from V:" + oldVersion + " to V:" + newVersion);

        // Drop older table if existed
        Log.i(VehicleDB.class.getName(), "Dropping old table: " + DROP_TABLE);
        db.execSQL(DROP_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Get cursor loader for notifications list activity
     * @param context
     * @return
     */
    public static Loader<Cursor> getCursorLoader(Context context)
    {
        CursorLoader cursorLoader = new CursorLoader(context, DBContentProvider.NOTIFICATIONS_URI, NotificationDB.LIST_COLUMNS, null, null, null);
        return cursorLoader;
    }
    
    /**
     * Read a notification from the DB given its ID
     * 
     * @param contentResolver content resolver
     * @param id notification ID
     * @param notification object to fill, if NULL, new created and returned
     * @return full notification object
     * @throws SQLException
     */
    public static NotificationBean readNotification(Context context, long id, NotificationBean notification) throws SQLException
    {
        Uri uri = Uri.parse(DBContentProvider.NOTIFICATIONS_URI + "/" + id);
        // Read all columns for the record
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0)
        {
            throw new SQLException("Unable to find notification with ID: " + id + " Corrupt DB?");
        }
        // more then one record
        if (cursor.getCount() > 1)
        {
            throw new SQLException("More then one notification with ID: " + id + " Corrupt DB?");
        }

        cursor.moveToFirst();

        if (notification == null)
        {
            notification = new NotificationBean();
        }

        notification.setId(id);
        notification.setActivity(Utils.readString(cursor, COL_ACTIVITY, TABLE_NOTIFICATION));
        notification.setVehicleRef(Utils.readLong(cursor, COL_VEHICLE_REF, TABLE_NOTIFICATION));
        notification.setEnabled(Utils.readBoolean(cursor, COL_ENABLED, TABLE_NOTIFICATION));
        notification.setNote(Utils.readString(cursor, COL_NOTE, TABLE_NOTIFICATION));
        notification.setDateDue(Utils.readLong(cursor, COL_DATE_DUE, TABLE_NOTIFICATION));
        notification.setDateRepeat(Utils.readInt(cursor, COL_DATE_REPEAT, TABLE_NOTIFICATION));
        notification.setDateAdvance(Utils.readInt(cursor, COL_DATE_ADVANCE, TABLE_NOTIFICATION));
        notification.setPeriodLast(Utils.readLong(cursor, COL_PERIOD_LAST, TABLE_NOTIFICATION));
        notification.setPeriodRepeat(Utils.readInt(cursor, COL_PERIOD_REPEAT, TABLE_NOTIFICATION));
        notification.setPeriodAdvance(Utils.readInt(cursor, COL_PERIOD_ADVANCE, TABLE_NOTIFICATION));
        notification.setDistanceLast(Utils.readInt(cursor, COL_DISTANCE_LAST, TABLE_NOTIFICATION));
        notification.setDistanceRepeat(Utils.readInt(cursor, COL_DISTANCE_REPEAT, TABLE_NOTIFICATION));
        notification.setDistanceAdvance(Utils.readInt(cursor, COL_DISTANCE_ADVANCE, TABLE_NOTIFICATION));

        cursor.close();

        return notification;
    }
    
    /**
     * Returns notification bean's values as content value map
     * 
     * @param vehicle
     * @return
     */
    private static ContentValues getAsContentValues(NotificationBean notification)
    {
        ContentValues values = new ContentValues();
        
        values.put(COL_ACTIVITY, notification.getActivity());
        values.put(COL_VEHICLE_REF, notification.getVehicleRef());
        values.put(COL_ENABLED, (notification.getEnabled() != null && notification.getEnabled()) ? 1: 0);
        values.put(COL_NOTE, notification.getNote());
        values.put(COL_DATE_DUE, notification.getDateDue());
        values.put(COL_DATE_REPEAT, notification.getDateRepeat());
        values.put(COL_DATE_ADVANCE, notification.getDateAdvance());
        values.put(COL_PERIOD_LAST, notification.getPeriodLast());
        values.put(COL_PERIOD_REPEAT, notification.getPeriodRepeat());
        values.put(COL_PERIOD_ADVANCE, notification.getPeriodAdvance());
        values.put(COL_DISTANCE_LAST, notification.getDistanceLast());
        values.put(COL_DISTANCE_REPEAT, notification.getDistanceRepeat());
        values.put(COL_DISTANCE_ADVANCE, notification.getDistanceAdvance());

        return values;
    }
    
    /**
     * Delete notifications TODO better sql query
     * 
     * @param resolver
     * @param ids
     */
    public static void deleteNotifications(Context context, Set<Long> ids)
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

        // delete a record
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(DBContentProvider.NOTIFICATIONS_URI, sb.toString(), args);
    }
    
    /**
     * Delete notifications which reference these vehicles TODO better sql query
     * 
     * @param resolver
     * @param ids
     */
    public static void deleteNotificationsByVehiclesIds(Context context, Set<Long> vehicleIds)
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

        // delete a record
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(DBContentProvider.NOTIFICATIONS_URI, sb.toString(), args);
    }
    
    /**
     * Create new notification
     * 
     * @param contentResolver
     * @param notification
     */
    public static void createNotification(Context context, NotificationBean notification)
    {
        ContentValues values = getAsContentValues(notification);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(DBContentProvider.NOTIFICATIONS_URI, values);
    }
    
    /**
     * Update existing notification
     * 
     * @param contentResolver
     * @param notification
     * @throws SQLException
     */
    public static void updateNotification(Context context, NotificationBean notification) throws SQLException
    {
        ContentValues values = getAsContentValues(notification);

        Uri uri = Uri.parse(DBContentProvider.NOTIFICATIONS_URI + "/" + notification.getId());
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.update(uri, values, null, null);
    }

    /**
     * Construct header text - activity of the notification
     * 
     * @param c DB cursor
     * @return header text
     */
    public static String constructHeaderText(Cursor c)
    {
        return Utils.readString(c, COL_ACTIVITY, TABLE_NOTIFICATION);
    }
    
    /**
     * Description of the notification
     * @param c DB cursor
     * @return description text
     */
    public static String constructDescriptionText(Cursor c)
    {
        StringBuilder sb = new StringBuilder();
        //TODO - ??
        // for "VEHICLE_NAME" - > by vehicle ID if not NULL
        // in "X" days - > if not NULL  min [(DATE_DUE - currentDate()), (PERIOD_LAST + PERIOD_REPEAT - currentDate())]  
        // or in "Y" km/mi - > if not NULL (DISTANCE_LAST + DISTANCE_REPEAT - currentDistance())
        
        return sb.toString();
    }
}
