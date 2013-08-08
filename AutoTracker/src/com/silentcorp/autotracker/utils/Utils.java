package com.silentcorp.autotracker.utils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.controls.DateView;
import com.silentcorp.autotracker.controls.NumberView;
import com.silentcorp.autotracker.controls.SuffixView;
import com.silentcorp.autotracker.controls.spinneradapter.FuelSpinnerAdapter;
import com.silentcorp.autotracker.controls.spinneradapter.ISpinnerAdapter;
import com.silentcorp.autotracker.controls.spinneradapter.LabelValuePair;
import com.silentcorp.autotracker.controls.spinneradapter.NameIdPair;
import com.silentcorp.autotracker.controls.spinneradapter.VehicleSpinnerAdapter;

/**
 * Helper class for DB operations and other repeatable operations
 * 
 * @author neandertal
 */
public class Utils
{
    /** Authority for DBContentProvider */
    public static final String AUTHORITY = "com.silentcorp.autotracker.db.DBContentProvider";

    /** Use to send item ID to activity */
    public static final String KEY_SELECTED_ID = "SELECTED_ID";

    /** COLUMN ID - universal for all tables */
    public final static String COL_ID = "_id";

    /** Preferences key for date format */
    public final static String PREF_DATE_KEY = "com.silentcorp.autotracker.pref_date_format";

    /** Preferences key for currency */
    public final static String PREF_CURRENCY_KEY = "com.silentcorp.autotracker.pref_currency";

    /** Preferences key for metric system */
    public final static String PREF_METRIC_KEY = "com.silentcorp.autotracker.pref_metrics";

    // Price and quantity formatter
    public static DecimalFormat df = new DecimalFormat("#.##");

    // ///////////////////////////////
    // Maps with values to labels mapping

    private static Map<String, String> mapFuelTypeLabels = new HashMap<String, String>();
    private static Map<String, String> mapFuelTypeUnits = new HashMap<String, String>();
    private static Map<String, String> mapCurrency = new HashMap<String, String>();
    private static Map<String, String> mapDistance = new HashMap<String, String>();
    private static Map<String, String> mapVolume = new HashMap<String, String>();
    private static Map<String, String> mapWeight = new HashMap<String, String>();

    // //////////////////////////
    // Suffixes

    /**
     * Return fuel label - human readable
     * 
     * @param fuelType as used internally
     * @return
     */
    public static String getLocalizedFuelTypeLabel(String fuelType, Context context)
    {
        synchronized (mapFuelTypeLabels)
        {
            // fill map if not created yet
            if (mapFuelTypeLabels.isEmpty())
            {
                String[] labels = context.getResources().getStringArray(R.array.fuel_type_labels);
                String[] values = context.getResources().getStringArray(R.array.fuel_type_values);

                for (int i = 0; i < values.length; i++)
                {
                    mapFuelTypeLabels.put(values[i], labels[i]);
                }
            }
            return mapFuelTypeLabels.get(fuelType);
        }
    }

    /**
     * Return human readable suffix for this currency
     * 
     * @param context
     * @return
     */
    public static String getLocalizedCurrencySuffix(Context context)
    {
        synchronized (mapCurrency)
        {
            if (mapCurrency.isEmpty())
            {
                String[] labels = context.getResources().getStringArray(R.array.list_currency_suffix);
                String[] values = context.getResources().getStringArray(R.array.pref_list_currency_values);

                for (int i = 0; i < values.length; i++)
                {
                    mapCurrency.put(values[i], labels[i]);
                }
            }

            String currSel = getSelectedCurrency(context);
            return mapCurrency.get(currSel);
        }
    }

    /**
     * Return human readable suffix for distance given the currently selected
     * metric system
     * 
     * @param context
     * @return
     */
    public static String getLocalizedDistanceSuffix(Context context)
    {
        synchronized (mapDistance)
        {
            if (mapDistance.isEmpty())
            {
                String[] labels = context.getResources().getStringArray(R.array.distance_metric_labels);
                String[] metricSystems = context.getResources().getStringArray(R.array.pref_list_metrics_values);

                for (int i = 0; i < metricSystems.length; i++)
                {
                    mapDistance.put(metricSystems[i], labels[i]);
                }
            }

            String metricSytem = getSelectedMetric(context);
            return mapDistance.get(metricSytem);
        }
    }

    /**
     * Return human readable suffix for volume given the selected metric system
     * 
     * @param context
     * @return
     */
    private static String getLocalizedVolumeSuffix(Context context)
    {
        synchronized (mapVolume)
        {
            if (mapVolume.isEmpty())
            {
                String[] labels = context.getResources().getStringArray(R.array.volume_metric_labels);
                String[] metricSystems = context.getResources().getStringArray(R.array.pref_list_metrics_values);

                for (int i = 0; i < metricSystems.length; i++)
                {
                    mapVolume.put(metricSystems[i], labels[i]);
                }
            }

            String metricSytem = getSelectedMetric(context);
            return mapVolume.get(metricSytem);
        }
    }

    /**
     * Return human readable suffix for weight given the selected metric system
     * 
     * @param context
     * @return
     */
    private static String getLocalizedWeightSuffix(Context context)
    {
        synchronized (mapWeight)
        {
            if (mapWeight.isEmpty())
            {
                String[] labels = context.getResources().getStringArray(R.array.weight_metric_labels);
                String[] metricSystems = context.getResources().getStringArray(R.array.pref_list_metrics_values);

                for (int i = 0; i < metricSystems.length; i++)
                {
                    mapWeight.put(metricSystems[i], labels[i]);
                }
            }

            String metricSytem = getSelectedMetric(context);
            return mapWeight.get(metricSytem);
        }
    }

    /**
     * Return human readable suffix for electricity given the selected metric
     * system
     * 
     * @param context
     * @return
     */
    private static String getLocalizedElectricitySuffix(Context context)
    {
        return context.getResources().getString(R.string.electricity_metric_label);
    }

    /**
     * Return human readable suffix for quantity given the selected metric
     * system and given fuelType
     * 
     * @param context
     * @return
     */
    public static String getLocalizedQuantitySuffix(Context context, String fuelType)
    {
        synchronized (mapFuelTypeUnits)
        {
            // fill map if not created yet
            if (mapFuelTypeUnits.isEmpty())
            {
                String[] units = context.getResources().getStringArray(R.array.fuel_type_units);
                String[] values = context.getResources().getStringArray(R.array.fuel_type_values);

                for (int i = 0; i < values.length; i++)
                {
                    mapFuelTypeUnits.put(values[i], units[i]);
                }
            }
        }

        String unitType = mapFuelTypeUnits.get(fuelType);
        if ("WEIGHT".equals(unitType))//TODO use ENUM?
        {
            return getLocalizedWeightSuffix(context);
        }
        else if ("VOLUME".equals(unitType))
        {
            return getLocalizedVolumeSuffix(context);
        }
        else if ("ELECTRICITY".equals(unitType))
        {
            return getLocalizedElectricitySuffix(context);
        }

        return null;
    }

    // //////////////////////////////
    // View operations

    /**
     * Sets the given text to the view with the given ID in the current activity
     * 
     * @param activity current activity
     * @param viewID view id - the view must be a TextView
     * @param textToSet text to set. Called toString if not null.
     */
    public static void setViewText(Activity activity, int viewID, Object textToSet)
    {
        TextView v = (TextView) activity.findViewById(viewID);
        String text = null;
        if (textToSet != null)
        {
            text = textToSet.toString();
        }
        v.setText(text);
    }

    /**
     * Set the time(date) to the view. The date is formatted to current selected
     * pattern in preferences. If timeInMillis is NULL then the view is cleared.
     * 
     * @param activity
     * @param viewID
     * @param timeMillis
     */
    public static void setDateViewValue(Activity activity, int viewID, Long timeMillis)
    {
        DateView dv = (DateView) activity.findViewById(viewID);
        dv.setDate(timeMillis);
    }

    /**
     * Get date from date view
     * 
     * @param activity
     * @param viewID
     * @return
     */
    public static Long getDateViewValue(Activity activity, int viewID)
    {
        DateView dv = (DateView) activity.findViewById(viewID);
        return dv.getDate();
    }

    /**
     * Set the number to the view.
     * 
     * @param activity
     * @param viewID
     * @param timeMillis
     */
    public static void setNumberViewValue(Activity activity, int viewID, Number value)
    {
        NumberView nv = (NumberView) activity.findViewById(viewID);
        nv.setValue(value);
    }

    /**
     * Set the suffix to the view.
     * 
     * @param activity
     * @param viewID
     * @param timeMillis
     */
    public static void setSuffixViewValue(Activity activity, int viewID, Number value)
    {
        SuffixView nv = (SuffixView) activity.findViewById(viewID);
        nv.setValue(value);
    }    
    
    /**
     * Get value from number view
     * 
     * @param activity
     * @param viewID
     * @return
     */
    public static Double getNumberViewValue(Activity activity, int viewID)
    {
        NumberView nv = (NumberView) activity.findViewById(viewID);
        return nv.getValue();
    }


    /**
     * Get value from suffix view
     * 
     * @param activity
     * @param viewID
     * @return
     */
    public static Double getSuffixViewValue(Activity activity, int viewID)
    {
        SuffixView nv = (SuffixView) activity.findViewById(viewID);
        return nv.getValue();
    }
    
    /**
     * Get value from number view
     * 
     * @param activity
     * @param viewID
     * @return
     */
    public static Integer getNumberViewValueAsInt(Activity activity, int viewID)
    {
        NumberView nv = (NumberView) activity.findViewById(viewID);
        return nv.getValueAsInteger();
    }

    /**
     * Get checkbox view value
     */
    public static Boolean getCheckboxValue(Activity activity, int viewID)
    {
        CheckBox cb = (CheckBox) activity.findViewById(viewID);
        return cb.isChecked();
    }
    
    /**
     * Returns the text in the given text view. If nothing in the view, returns
     * NULL.
     * 
     * @param activity current activity
     * @param viewID view id
     * @return text or NULL if empty view
     */
    public static String getViewText(Activity activity, int viewID)
    {
        TextView v = (TextView) activity.findViewById(viewID);
        CharSequence cs = v.getText();
        if (cs.length() == 0)
        {
            return null;
        }

        return cs.toString();
    }

    /**
     * Returns the selected spinner value. It must contain objects of type
     * LabelValuePair.
     * 
     * @param activity current activity
     * @param spinnerID spinner id
     */
    public static String getSpinnerValueAsStr(Activity activity, int spinnerID)
    {
        Spinner spinner = (Spinner) activity.findViewById(spinnerID);
        LabelValuePair pair = (LabelValuePair) spinner.getSelectedItem();
        if (pair != null)
        {
            return pair.getValue();
        }

        return null;
    }

    /**
     * Returns the selected spinner value. It must contain objects of type
     * {@link NameIdPair}.
     * 
     * @param activity current activity
     * @param spinnerID spinner id
     */
    public static Long getSpinnerValueAsLong(Activity activity, int spinnerID)
    {
        Spinner spinner = (Spinner) activity.findViewById(spinnerID);
        NameIdPair pair = (NameIdPair) spinner.getSelectedItem();
        if (pair != null)
        {
            return pair.getID();
        }

        return null;
    }

    /**
     * Finds the position of given value in spinner list and selects it. If not
     * found or value is NULL, uses the first position.
     * 
     * @param activity current activity
     * @param spinnerID spinner id
     * @param value value to set, can be NULL
     */
    public static void setSpinnerSelection(Activity activity, int spinnerID, String value)
    {
        int pos = 0;
        Spinner spinner = (Spinner) activity.findViewById(spinnerID);
        if (value != null)
        {
            ISpinnerAdapter adapter = (ISpinnerAdapter) spinner.getAdapter();
            pos = adapter.getPosition(value);
            if (pos < 0)
            {
                pos = 0;
            }
        }
        spinner.setSelection(pos);
    }

    /**
     * Sets these values as choices and select given one
     * 
     * @param activity activity
     * @param spinnerID component id
     * @param allValues all possible values
     * @param selected value to select
     */
    public static void setSpinnerSelection(Activity activity, int spinnerID, String[] allValues, String selected)
    {
        Spinner spinner = (Spinner) activity.findViewById(spinnerID);
        FuelSpinnerAdapter adapter = (FuelSpinnerAdapter) spinner.getAdapter();
        adapter.setNewChoices(allValues);

        int pos = adapter.getPosition(selected);
        if (pos < 0)
        {
            pos = 0;
        }
        spinner.setSelection(pos);
    }

    /**
     * Set vehicle selection in vehicles spinner
     * 
     * @param activity
     * @param spinnerID
     * @param vehicleRef
     */
    public static void setVehicleSpinnerSelection(Activity activity, int spinnerID, Long vehicleRef)
    {
        Spinner spinner = (Spinner) activity.findViewById(spinnerID);
        VehicleSpinnerAdapter adapter = (VehicleSpinnerAdapter) spinner.getAdapter();

        int pos = adapter.getPosition(vehicleRef);
        if (pos < 0)
        {
            pos = 0;
        }
        spinner.setSelection(pos);
    }

    /**
     * Set checkbox checked/ unchecked
     * 
     * @param activity
     * @param checkboxID
     * @param checked - Can be NULL, then checkbox is unchecked
     */
    public static void setCheckboxSelection(Activity activity, int checkboxID, Boolean checked)
    {
        CheckBox chkbox = (CheckBox) activity.findViewById(checkboxID);
        chkbox.setChecked((checked == null) ? false : checked);
    }

    // ///////////////////////////////
    // DB READ

    /**
     * Reads a string from cursor. If value is null in DB, NULL is returned.
     * 
     * @param cursor DB cursor
     * @param column column to read
     * @param table Table name
     * @return string value or NULL if null in DB
     * @throws SQLException if no such column or value not same type
     */
    public static String readString(Cursor cursor, String column, String table) throws SQLException
    {
        int index = cursor.getColumnIndex(column);
        if (index < 0)
        {
            throw new SQLException("No such column:" + column + " in table:" + table + ". Corrupt DB?");
        }

        if (cursor.isNull(index))
        {
            return null;
        }

        return cursor.getString(index);
    }

    /**
     * Reads an integer form the column. If value is NULL in DB, null is
     * returned.
     * 
     * @param cursor DB cursor
     * @param column column to read
     * @param table table to read from
     * @return Integer or NULL
     * @throws SQLException if no such column
     */
    public static Integer readInt(Cursor cursor, String column, String table) throws SQLException
    {
        int index = cursor.getColumnIndex(column);
        if (index < 0)
        {
            throw new SQLException("No such column:" + column + " in table:" + table + ". Corrupt DB?");
        }

        if (cursor.isNull(index))
        {
            return null;
        }

        return cursor.getInt(index);
    }

    /**
     * Read a long value from cursor. If value is NULL in DB, null is returned.
     * 
     * @param cursor DB cursor
     * @param column column to read
     * @param table table to read from
     * @return Long or NULL if DB value is null.
     * @throws SQLException if no such column
     */
    public static Long readLong(Cursor cursor, String column, String table) throws SQLException
    {
        int index = cursor.getColumnIndex(column);
        if (index < 0)
        {
            throw new SQLException("No such column:" + column + " in table:" + table + ". Corrupt DB?");
        }

        if (cursor.isNull(index))
        {
            return null;
        }

        return cursor.getLong(index);
    }

    /**
     * Read double from cursor. Return null if NULL in DB.
     * 
     * @param cursor DB cursor
     * @param column column to read
     * @param table table to read from
     * @return Double or NULL if null in DB
     * @throws SQLException if no such column
     */
    public static Double readDouble(Cursor cursor, String column, String table) throws SQLException
    {
        int index = cursor.getColumnIndex(column);
        if (index < 0)
        {
            throw new SQLException("No such column:" + column + " in table:" + table + ". Corrupt DB?");
        }

        if (cursor.isNull(index))
        {
            return null;
        }

        return cursor.getDouble(index);
    }

    /**
     * Read a Boolean from DB. If NULL, null is returned.
     * 
     * @param cursor DB cursor
     * @param column column to read
     * @param table table to read from
     * @return Boolean or NULL if null in DB
     * @throws SQLException if no such column
     */
    public static Boolean readBoolean(Cursor cursor, String column, String table) throws SQLException
    {
        int index = cursor.getColumnIndex(column);
        if (index < 0)
        {
            throw new SQLException("No such column:" + column + " in table:" + table + ". Corrupt DB?");
        }

        if (cursor.isNull(index))
        {
            return Boolean.FALSE;
        }

        int i = cursor.getInt(index);
        if (i == 1)
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }

    // ////////////////////////////
    // USER PREFS

    /**
     * Get selected metric system
     * 
     * @param context
     * @return
     */
    private static String getSelectedMetric(Context context)
    {
        // get metrics preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String metrSel = prefs.getString(PREF_METRIC_KEY, null);
        // In case the unlikely event we can't read the preferences, use default
        if (metrSel == null)
        {
            metrSel = context.getResources().getString(R.string.pref_metrics_default);
        }

        return metrSel;
    }

    /**
     * User preferences selected currency
     * 
     * @param context
     * @return
     */
    private static String getSelectedCurrency(Context context)
    {
        // get currency preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String curSel = prefs.getString(PREF_CURRENCY_KEY, null);
        // In case the unlikely event we can't read the preferences, use default
        if (curSel == null)
        {
            curSel = context.getResources().getString(R.string.pref_currency_default);
        }

        return curSel;
    }

    /**
     * Get selected date format
     * 
     * @param context
     * @return
     */
    public static String getSelectedDateFormat(Context context)
    {
        // get preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String dateFormat = prefs.getString(Utils.PREF_DATE_KEY, null);
        // In case the unlikely event we can't read the preferences, use default
        if (dateFormat == null)
        {
            dateFormat = context.getResources().getString(R.string.pref_date_format_default);
        }

        return dateFormat;
    }

    // ////////////////////////////////
    // DATE

    /**
     * Returns the current date in milliseconds (hour, minute, seconds are 0)
     * 
     * @return
     */
    public static final long getCurrentDate()
    {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    // ///////////////////
    // FORMAT

    /**
     * Formats distance: ex. 300234 km
     * 
     * @param distance
     * @param context
     * @return
     */
    public static String formatDistance(int distance, Context context)
    {
        return Integer.toString(distance) + " " + getLocalizedDistanceSuffix(context);
    }

    /**
     * Format quantity regarding the fuel type and user selected metrics Ex.
     * 12,34 L
     * 
     * @param quantity
     * @param context
     * @return
     */
    public static String formatQuantity(double quantity, Context context, String fuelType)
    {
        return df.format(quantity) + " " + getLocalizedQuantitySuffix(context, fuelType);
    }

    /**
     * Format price per unit regarding the current metric system. Ex. 2,34 $/L
     * 
     * @param cost
     * @param currency
     * @param context
     * @return
     */
    public static String formatPricePerUnit(double cost, Context context, String fuelType)
    {
        return df.format(cost) + " " + getLocalizedCurrencySuffix(context) + "/"
                + getLocalizedQuantitySuffix(context, fuelType);
    }

    /**
     * Format cost: 12.07 $ for example
     * 
     * @param cost
     * @param suffix
     * @return
     */
    public static String formatCost(double cost, Context context)
    {
        return df.format(cost) + " " + getLocalizedCurrencySuffix(context);
    }
}
