package com.silentcorp.autotracker.db;

import com.silentcorp.autotracker.utils.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * DB Content provider to be used by this application
 * 
 * @author neandertal
 * 
 */
public class DBContentProvider extends ContentProvider
{
    // Vehicles base URI:
    // content://com.silentcorp.autotracker.db.DBContentProvider/vehicle"
    public static final Uri VEHICLES_URI = Uri.parse("content://" + Utils.AUTHORITY + "/" + VehicleDB.TABLE_VEHICLE);

    // Notifications base URI:
    // content://com.silentcorp.autotracker.db.DBContentProvider/notification"
    public static final Uri NOTIFICATIONS_URI = Uri.parse("content://" + Utils.AUTHORITY + "/" + NotificationDB.TABLE_NOTIFICATION);

    // Events base URI:
    // content://com.silentcorp.autotracker.db.DBContentProvider/event"
    public static final Uri EVENTS_URI = Uri.parse("content://" + Utils.AUTHORITY + "/" + EventDB.TABLE_EVENT);

    
    // URI's paths numbers
    private static final int ALL_VEHICLES = 1;
    private static final int SINGLE_VEHICLE = 2;
    private static final int COUNT_VEHICLE = 3;
    private static final int ALL_NOTIFICATIONS = 4;
    private static final int SINGLE_NOTIFICATION = 5;
    private static final int COUNT_NOTIFICATION = 6;
    private static final int ALL_EVENTS = 7;
    private static final int SINGLE_EVENT = 8;
    private static final int COUNT_EVENT = 9;

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sUriMatcher.addURI(Utils.AUTHORITY, VehicleDB.TABLE_VEHICLE, ALL_VEHICLES);
        sUriMatcher.addURI(Utils.AUTHORITY, VehicleDB.TABLE_VEHICLE + "/#", SINGLE_VEHICLE);
        sUriMatcher.addURI(Utils.AUTHORITY, VehicleDB.TABLE_VEHICLE + "/COUNT", COUNT_VEHICLE);
        sUriMatcher.addURI(Utils.AUTHORITY, NotificationDB.TABLE_NOTIFICATION, ALL_NOTIFICATIONS);
        sUriMatcher.addURI(Utils.AUTHORITY, NotificationDB.TABLE_NOTIFICATION + "/#", SINGLE_NOTIFICATION);
        sUriMatcher.addURI(Utils.AUTHORITY, NotificationDB.TABLE_NOTIFICATION + "/COUNT", COUNT_NOTIFICATION);
        sUriMatcher.addURI(Utils.AUTHORITY, EventDB.TABLE_EVENT, ALL_EVENTS);
        sUriMatcher.addURI(Utils.AUTHORITY, EventDB.TABLE_EVENT + "/#", SINGLE_EVENT);
        sUriMatcher.addURI(Utils.AUTHORITY, EventDB.TABLE_EVENT + "/COUNT", COUNT_EVENT);
    }

    // Database helper
    private DBHandler dbHandler;

    /**
     * Creates the DB handler, but does not open DB so no lengthy operations are
     * performed.
     */
    @Override
    public boolean onCreate()
    {
        /*
         * Creates a new helper object. This method always returns quickly.
         * Notice that the database itself isn't created or opened until
         * SQLiteOpenHelper.getWritableDatabase is called
         */
        dbHandler = new DBHandler(getContext());

        return true;
    }

    /**
     * Return data types
     */
    @Override
    public String getType(Uri uri)
    {
        // TODO check if correctly build
        switch (sUriMatcher.match(uri))
        {
            case ALL_VEHICLES:
                return "vnd.android.cursor.dir/vnd." + Utils.AUTHORITY + "." + VehicleDB.TABLE_VEHICLE;
            case SINGLE_VEHICLE:
                return "vnd.android.cursor.item/vnd." + Utils.AUTHORITY + "." + VehicleDB.TABLE_VEHICLE;
            case COUNT_VEHICLE:
                return "vnd.android.cursor.count/vnd." + Utils.AUTHORITY + "." + VehicleDB.TABLE_VEHICLE;
            case ALL_NOTIFICATIONS:
                return "vnd.android.cursor.dir/vnd." + Utils.AUTHORITY + "." + NotificationDB.TABLE_NOTIFICATION;
            case SINGLE_NOTIFICATION:
                return "vnd.android.cursor.item/vnd." + Utils.AUTHORITY + "." + NotificationDB.TABLE_NOTIFICATION;
            case COUNT_NOTIFICATION:
                return "vnd.android.cursor.count/vnd." + Utils.AUTHORITY + "." + NotificationDB.TABLE_NOTIFICATION;
            case ALL_EVENTS:
                return "vnd.android.cursor.dir/vnd." + Utils.AUTHORITY + "." + EventDB.TABLE_EVENT;
            case SINGLE_EVENT:
                return "vnd.android.cursor.item/vnd." + Utils.AUTHORITY + "." + EventDB.TABLE_EVENT;
            case COUNT_EVENT:
                return "vnd.android.cursor.count/vnd." + Utils.AUTHORITY + "." + EventDB.TABLE_EVENT;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /**
     * Inserts new record
     */
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        long id = 0;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        switch (sUriMatcher.match(uri))
        {
            case ALL_VEHICLES:
            {
                // Insert in Vehicles table
                id = db.insert(VehicleDB.TABLE_VEHICLE, null, values);
                if (id < 0)
                {
                    Log.w(DBContentProvider.class.getName(), "Unable to add new vehicle. Incorrect values?");
                }
                break;
            }
            case ALL_NOTIFICATIONS:
            {
                // Insert in notifications table
                id = db.insert(NotificationDB.TABLE_NOTIFICATION, null, values);
                if (id < 0)
                {
                    Log.w(DBContentProvider.class.getName(), "Unable to add new notification. Incorrect values?");
                }
                break;
            }
            case ALL_EVENTS:
            {
                // Insert in events table
                id = db.insert(EventDB.TABLE_EVENT, null, values);
                if (id < 0)
                {
                    Log.w(DBContentProvider.class.getName(), "Unable to add new events. Incorrect values?");
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("INSERT: Unsupported URI: " + uri);
            }
        }

        // notify listeners
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.buildUpon().appendPath(Long.toString(id)).build();
    }

    /**
     * Update existing record
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int count = 0;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        switch (sUriMatcher.match(uri))
        {
            case SINGLE_VEHICLE:
            {
                String id = uri.getPathSegments().get(1);
                // update existing record
                count = db.update(VehicleDB.TABLE_VEHICLE, values, "_id=?", new String[] { id });

                if (count == 0)
                {
                    Log.w(DBContentProvider.class.getName(), "No vehicle with ID: " + id + " to update. Corrupt DB?");
                }

                if (count > 1)
                {
                    Log.w(DBContentProvider.class.getName(), "More then one vehicle with ID: " + id + " updated. Corrupt DB?");
                }
                break;
            }
            case SINGLE_NOTIFICATION:
            {
                String id = uri.getPathSegments().get(1);
                // update existing record
                count = db.update(NotificationDB.TABLE_NOTIFICATION, values, "_id=?", new String[] { id });

                if (count == 0)
                {
                    Log.w(DBContentProvider.class.getName(), "No notification with ID: " + id + " to update. Corrupt DB?");
                }

                if (count > 1)
                {
                    Log.w(DBContentProvider.class.getName(), "More then one notification with ID: " + id + " updated. Corrupt DB?");
                }
                break;
            }
            case SINGLE_EVENT:
            {
                String id = uri.getPathSegments().get(1);
                // update existing record
                count = db.update(EventDB.TABLE_EVENT, values, "_id=?", new String[] { id });

                if (count == 0)
                {
                    Log.w(DBContentProvider.class.getName(), "No event with ID: " + id + " to update. Corrupt DB?");
                }

                if (count > 1)
                {
                    Log.w(DBContentProvider.class.getName(), "More then one event with ID: " + id + " updated. Corrupt DB?");
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("UPDATE: Unsupported URI: " + uri);
            }
        }

        // notify listeners
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Query rows from tables
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sUriMatcher.match(uri);
        switch (uriType)
        {
            case ALL_VEHICLES:
            {
                queryBuilder.setTables(VehicleDB.TABLE_VEHICLE);
                break;
            }
            case SINGLE_VEHICLE:
            {
                queryBuilder.setTables(VehicleDB.TABLE_VEHICLE);
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere("_id=" + id);
                break;
            }
            case COUNT_VEHICLE:
            {
                queryBuilder.setTables(VehicleDB.TABLE_VEHICLE);
                projection = new String[]{"COUNT(" + Utils.COL_ID + ")"};
                break;
            }
            case ALL_NOTIFICATIONS:
            {
                queryBuilder.setTables(NotificationDB.TABLE_NOTIFICATION);
                break;
            }
            case SINGLE_NOTIFICATION:
            {
                queryBuilder.setTables(NotificationDB.TABLE_NOTIFICATION);
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere("_id=" + id);
                break;
            }
            case COUNT_NOTIFICATION:
            {
                queryBuilder.setTables(NotificationDB.TABLE_NOTIFICATION);
                projection = new String[]{"COUNT(" + Utils.COL_ID + ")"};
                break;
            }
            case ALL_EVENTS:
            {
                queryBuilder.setTables(EventDB.TABLE_EVENT);
                break;
            }
            case SINGLE_EVENT:
            {
                queryBuilder.setTables(EventDB.TABLE_EVENT);
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere("_id=" + id);
                break;
            }
            case COUNT_EVENT:
            {
                queryBuilder.setTables(EventDB.TABLE_EVENT);
                projection = new String[]{"COUNT(" + Utils.COL_ID + ")"};
                break;
            }
            default:
            {
                throw new IllegalArgumentException("QUERY: Unsupported URI: " + uri);
            }
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    /**
     * Deletes multiple rows
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int count = 0;
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        switch (sUriMatcher.match(uri))
        {
            case ALL_VEHICLES:
            {
                count = db.delete(VehicleDB.TABLE_VEHICLE, selection, selectionArgs);
                if (count == 0)
                {
                    Log.w(DBContentProvider.class.getName(), "No vehicles deleted. Corrupt DB?");
                }

                if (count < selectionArgs.length)
                {
                    Log.w(DBContentProvider.class.getName(), "Some vehicles not deleted. Corrupt DB?");
                }
                break;
            }
            case ALL_NOTIFICATIONS:
            {
                count = db.delete(NotificationDB.TABLE_NOTIFICATION, selection, selectionArgs);
                if (count == 0)
                {
                    Log.w(DBContentProvider.class.getName(), "No notifications deleted. Corrupt DB?");
                }

                if (count < selectionArgs.length)
                {
                    Log.w(DBContentProvider.class.getName(), "Some notifications not deleted. Corrupt DB?");
                }
                break;
            }
            case ALL_EVENTS:
            {
                count = db.delete(EventDB.TABLE_EVENT, selection, selectionArgs);
                if (count == 0)
                {
                    Log.w(DBContentProvider.class.getName(), "No event deleted. Corrupt DB?");
                }

                if (count < selectionArgs.length)
                {
                    Log.w(DBContentProvider.class.getName(), "Some event not deleted. Corrupt DB?");
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("DELETE: Unsupported URI: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
