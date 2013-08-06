package com.silentcorp.autotracker.list;

import android.content.Context;
import android.database.Cursor;

import com.silentcorp.autotracker.db.NotificationDB;

/**
 * DB adapter to link the Notifications ListView to the notifications table in
 * the DB
 * 
 * @author neandertal
 */
public class NotificationsDBAdapter extends AbstractDBAdapter
{

    public NotificationsDBAdapter(Context context)
    {
        super(context);
    }

    /**
     * Header text for notification.
     */
    public String getHeaderText(Cursor c)
    {
        return NotificationDB.constructHeaderText(c);
    }

    /**
     * Description of the notification
     */
    public String getDescriptionText(Cursor c)
    {
        return NotificationDB.constructDescriptionText(c);
    }
}
