package com.silentcorp.autotracker.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.beans.EventBean;
import com.silentcorp.autotracker.controls.NumberView;
import com.silentcorp.autotracker.controls.spinneradapter.VehicleSpinnerAdapter;
import com.silentcorp.autotracker.utils.DoubleNumber;
import com.silentcorp.autotracker.utils.EventType;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Add/Edit payment activity. Used to add or edit a payment event to DB.
 * 
 * @author neandertal
 * 
 */
public class PaymentEventActivity extends AbstractEventActivity
{
    @Override
    protected int getAddTitleID()
    {
        return R.string.text_add_payment;
    }

    @Override
    protected int getEditTitleID()
    {
        return R.string.text_edit_payment;
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_payment_event;
    }

    @Override
    protected void initViews()
    {
        String currSuff = Utils.getLocalizedCurrencySuffix(this);
        String distSuff = Utils.getLocalizedDistanceSuffix(this);

        // initialize quantity view
        NumberView costView = (NumberView) findViewById(R.id.cost_number_view);
        costView.setSuffix(currSuff);

        // initialize odometer view
        NumberView odometerView = (NumberView) findViewById(R.id.odometer_number_view);
        odometerView.setSuffix(distSuff);
    }

    @Override
    protected void initListeners()
    {
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
                obligatoryFields.put(R.id.description_edit_text, s.length() > 0);
                // check state
                checkObliagoryFieldsState();
            }
        });

        // cost field
        NumberView costView = (NumberView) findViewById(R.id.cost_number_view);
        costView.setNumberChangeListener(new NumberView.OnNumberChangeListener()
        {
            @Override
            public void onChange(DoubleNumber oldValue, DoubleNumber newValue)
            {
                obligatoryFields.put(R.id.cost_number_view, !newValue.isNull());
                // check state
                checkObliagoryFieldsState();
            }
        });
    }

    @Override
    protected EventBean createNewEvent()
    {
        // called to create new event
        EventBean newEvent = new EventBean();
        newEvent.setEventType(EventType.PAYMENT_EVENT);
        newEvent.setEventDate(Utils.getCurrentDate());
        return newEvent;
    }

    /**
     * Loads the service event to the form views
     */
    @Override
    protected void loadEventToForm()
    {
        Log.d(PaymentEventActivity.class.getName(), "loadEventToForm()");

        // select the vehicle
        Utils.setVehicleSpinnerSelection(this, R.id.vehicle_spinner, event.getVehicleRef());

        // set date
        Utils.setDateViewValue(this, R.id.date_edit_text, event.getEventDate());

        Utils.setViewText(this, R.id.description_edit_text, event.getDescription());

        // cost
        Utils.setNumberViewValue(this, R.id.cost_number_view, event.getCost());

        // odometer
        Utils.setNumberViewValue(this, R.id.odometer_number_view, event.getOdometer());

        Utils.setViewText(this, R.id.note_edit_text, event.getNote());
    }

    @Override
    protected void saveFormToEvent()
    {
        Log.d(PaymentEventActivity.class.getName(), "saveFormToEvent()");

        // set vehicle reference
        event.setVehicleRef(Utils.getSpinnerValueAsLong(this, R.id.vehicle_spinner));

        // event date
        event.setEventDate(Utils.getDateViewValue(this, R.id.date_edit_text));

        event.setDescription(Utils.getViewText(this, R.id.description_edit_text));

        // cost
        event.setCost(Utils.getNumberViewValue(this, R.id.cost_number_view));

        // odometer
        event.setOdometer(Utils.getNumberViewValue(this, R.id.odometer_number_view));

        event.setNote(Utils.getViewText(this, R.id.note_edit_text));
    }

    /**
     * initialize any spinners and auto complete text views
     */
    @Override
    protected void initChoices()
    {
        Log.d(FuelEventActivity.class.getName(), "initChoices()");

        // fill spinner with vehicles names, including empty choice
        Spinner vSpinner = (Spinner) findViewById(R.id.vehicle_spinner);
        VehicleSpinnerAdapter vsa = new VehicleSpinnerAdapter(this, true);
        vSpinner.setAdapter(vsa);
    }

    // ////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.payment_event, menu);
        return true;
    }
}
