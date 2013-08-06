package com.silentcorp.autotracker.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.beans.EventBean;
import com.silentcorp.autotracker.controls.NumberView;
import com.silentcorp.autotracker.controls.spinneradapter.VehicleSpinnerAdapter;
import com.silentcorp.autotracker.db.EventType;
import com.silentcorp.autotracker.db.Utils;

/**
 * Add/Edit service event activity. Used to add or edit an service event to DB.
 * 
 * @author neandertal
 * 
 */
public class MaintenanceEventActivity extends AbstractEventActivity
{

    @Override
    protected int getAddTitleID()
    {
        return R.string.text_add_maintenance;
    }

    @Override
    protected int getEditTitleID()
    {
        return R.string.text_edit_maintenance;
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_maintenance_event;
    }

    @Override
    protected void initViews()
    {
        String currSuff = Utils.getLocalizedCurrencySuffix(this);
        String distSuff = Utils.getLocalizedDistanceSuffix(this);

        // initialize quantity view
        NumberView costView = (NumberView) findViewById(R.id.cost_number_view);
        costView.setDialogTitle(getString(R.string.text_cost));
        costView.setStep(1.0);
        costView.setValueDecimal(true);
        costView.setSuffix(currSuff);

        // initialize odometer view
        NumberView odometerView = (NumberView) findViewById(R.id.odometer_number_view);
        odometerView.setDialogTitle(getString(R.string.text_odometer));
        odometerView.setRange(0.0, Double.MAX_VALUE);
        odometerView.setStep(1000.0);
        odometerView.setValueDecimal(false);
        odometerView.setSuffix(distSuff);
    }

    @Override
    protected void initListeners()
    {
        // Initially disable save button
        Button saveBtn = (Button) findViewById(R.id.save_button);
        saveBtn.setEnabled(false);

        // Initially fill obligatory fields map
        obligatoryFields.put(R.id.description_edit_text, Boolean.FALSE);
        obligatoryFields.put(R.id.cost_number_view, Boolean.FALSE);

        // description field
        TextView descriptionView = (TextView) findViewById(R.id.description_edit_text);
        descriptionView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void afterTextChanged(Editable s)
            {
                Boolean result = Boolean.FALSE;
                if (s.length() > 0)
                {
                    result = Boolean.TRUE;
                    obligatoryFields.put(R.id.description_edit_text, result);
                    // check state
                    checkObliagoryFieldsState();
                }
            }
        });

        // cost field
        TextView costView = (TextView) findViewById(R.id.cost_number_view);
        costView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void afterTextChanged(Editable s)
            {
                Boolean result = Boolean.FALSE;
                if (s.length() > 0)
                {
                    try
                    {
                        Double d = Double.valueOf(s.toString());
                        if (d > 0)
                        {
                            result = Boolean.TRUE;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        // Not valid number
                    }

                    obligatoryFields.put(R.id.cost_number_view, result);
                    // check state
                    checkObliagoryFieldsState();
                }
            }
        });
    }

    @Override
    protected EventBean createNewEvent()
    {
        // called to create new event
        EventBean newEvent = new EventBean();
        newEvent.setEventType(EventType.MAINTENANCE_EVENT);
        newEvent.setEventDate(Utils.getCurrentDate());
        return newEvent;
    }

    /**
     * Loads the service event to the form views
     */
    @Override
    protected void loadEventToForm()
    {
        Log.d(MaintenanceEventActivity.class.getName(), "loadEventToForm()");

        // select the vehicle
        Utils.setVehicleSpinnerSelection(this, R.id.vehicle_spinner, event.getVehicleRef());

        // set date
        Utils.setDateViewValue(this, R.id.date_edit_text, event.getEventDate());

        Utils.setViewText(this, R.id.description_edit_text, event.getDescription());

        // cost
        Utils.setNumberViewValue(this, R.id.cost_number_view, event.getCost());

        // odometer
        Utils.setNumberViewValue(this, R.id.odometer_number_view, event.getOdometer());

        Utils.setViewText(this, R.id.place_edit_text, event.getPlace());
        Utils.setViewText(this, R.id.note_edit_text, event.getNote());
    }

    @Override
    protected void saveFormToEvent()
    {
        Log.d(MaintenanceEventActivity.class.getName(), "saveFormToEvent()");

        // set vehicle reference
        event.setVehicleRef(Utils.getVehicleSpinnerValue(this, R.id.vehicle_spinner));

        // set event date
        event.setEventDate(Utils.getDateViewValue(this, R.id.date_edit_text));

        event.setDescription(Utils.getViewText(this, R.id.description_edit_text));

        // cost
        event.setCost(Utils.getNumberViewValue(this, R.id.cost_number_view));

        // odometer
        event.setOdometer(Utils.getNumberViewValueAsInt(this, R.id.odometer_number_view));

        event.setPlace(Utils.getViewText(this, R.id.place_edit_text));
        event.setNote(Utils.getViewText(this, R.id.note_edit_text));
    }

    /**
     * TODO initialize any spinners and auto complete text views
     */
    @Override
    protected void initChoices()
    {
        Log.d(FuelEventActivity.class.getName(), "initChoices()");

        // fill spinner with vehicles names
        Spinner vSpinner = (Spinner) findViewById(R.id.vehicle_spinner);
        VehicleSpinnerAdapter vsa = new VehicleSpinnerAdapter(this, false);
        vSpinner.setAdapter(vsa);

        // TODO fill service type spinner
    }

    // ////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.service_event, menu);
        return true;
    }
}
