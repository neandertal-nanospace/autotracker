package com.silentcorp.autotracker.activities;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.beans.EventBean;
import com.silentcorp.autotracker.controls.NumberView;
import com.silentcorp.autotracker.controls.SuffixView;
import com.silentcorp.autotracker.controls.spinneradapter.FuelSpinnerAdapter;
import com.silentcorp.autotracker.controls.spinneradapter.LabelValuePair;
import com.silentcorp.autotracker.controls.spinneradapter.NameIdPair;
import com.silentcorp.autotracker.controls.spinneradapter.VehicleSpinnerAdapter;
import com.silentcorp.autotracker.db.VehicleDB;
import com.silentcorp.autotracker.utils.EventType;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Add/Edit fill-up activity. Used to add or edit an fill-up event to DB.
 * 
 * @author neandertal
 * 
 */
public class FuelEventActivity extends AbstractEventActivity
{

    @Override
    protected int getAddTitleID()
    {
        return R.string.text_add_fill_up;
    }

    @Override
    protected int getEditTitleID()
    {
        return R.string.text_edit_fill_up;
    }

    /**
     * Return activity layout
     * 
     * @return
     */
    protected int getLayoutId()
    {
        return R.layout.activity_fuel_event;
    }

    @Override
    protected void initViews()
    {
        String currSuff = Utils.getLocalizedCurrencySuffix(this);
        String distSuff = Utils.getLocalizedDistanceSuffix(this);

        // initialize total cost view
        NumberView totalView = (NumberView) findViewById(R.id.total_cost_number_view);
        totalView.setSuffix(currSuff);

        // initialize odometer view
        NumberView odometerView = (NumberView) findViewById(R.id.odometer_number_view);
        odometerView.setSuffix(distSuff);
    }

    /**
     * Add listeners
     */
    protected void initListeners()
    {
        // Initially fill obligatory fields map
        obligatoryFields.put(R.id.quantity_number_view, Boolean.FALSE);
        obligatoryFields.put(R.id.total_cost_number_view, Boolean.FALSE);

        // quantity field
        NumberView quantityView = (NumberView) findViewById(R.id.quantity_number_view);
        quantityView.setNumberChangeListener(new NumberView.OnNumberChangeListener()
        {
            @Override
            public void onChange(Number oldValue, Number newValue, boolean isEmptyValue)
            {
                obligatoryFields.put(R.id.quantity_number_view, !isEmptyValue);
                // check state
                checkObliagoryFieldsState();

                // recalculate price per unit
                NumberView totalCost = (NumberView) findViewById(R.id.total_cost_number_view);
                double ppu = calculatePricePerUnit(newValue, totalCost.getValueAsDouble());
                SuffixView ppuView = (SuffixView) findViewById(R.id.price_per_unit_text_view);
                ppuView.setValue(ppu);
            }
        });

        // total cost field
        NumberView totalCost = (NumberView) findViewById(R.id.total_cost_number_view);
        totalCost.setNumberChangeListener(new NumberView.OnNumberChangeListener()
        {
            @Override
            public void onChange(Number oldValue, Number newValue, boolean isEmptyValue)
            {
                obligatoryFields.put(R.id.total_cost_number_view, !isEmptyValue);
                // check state
                checkObliagoryFieldsState();

                // recalculate price per unit
                NumberView quantityView = (NumberView) findViewById(R.id.quantity_number_view);
                double ppu = calculatePricePerUnit(quantityView.getValueAsDouble(), newValue);
                SuffixView ppuView = (SuffixView) findViewById(R.id.price_per_unit_text_view);
                ppuView.setValue(ppu);
            }
        });

        // Add listener to update fuel spinner according vehicle selection
        Spinner vSpinner = (Spinner) findViewById(R.id.vehicle_spinner);
        vSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                VehicleSpinnerAdapter vsa = (VehicleSpinnerAdapter) parentView.getAdapter();
                NameIdPair pair = vsa.getItem(position);

                // Set fuel choices depending on vehicle
                String[] fuels = VehicleDB.getVehicleFuels(FuelEventActivity.this, pair.getID());
                Utils.setSpinnerSelection(FuelEventActivity.this, R.id.fuel_type_spinner, fuels, event.getFuel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {}
        });

        // Add listener to update units suffix when fuel type changed
        Spinner ftSpinner = (Spinner) findViewById(R.id.fuel_type_spinner);
        ftSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                FuelSpinnerAdapter fsa = (FuelSpinnerAdapter) parentView.getAdapter();
                LabelValuePair pair = fsa.getItem(position);

                // Set quantity units according fuel type
                pair.getValue();
                String quanSuff = Utils.getLocalizedQuantitySuffix(FuelEventActivity.this, pair.getValue());

                String currSuff = Utils.getLocalizedCurrencySuffix(FuelEventActivity.this);
                String currPerQuanSuff = currSuff + "/" + quanSuff;

                // quantity view
                NumberView quantityView = (NumberView) findViewById(R.id.quantity_number_view);
                quantityView.setSuffix(quanSuff);
                quantityView.refresh();

                // price per unit text view
                SuffixView priceView = (SuffixView) findViewById(R.id.price_per_unit_text_view);
                priceView.setSuffix(currPerQuanSuff);
                priceView.refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {}
        });
    }

    /**
     * Calculate and set price per unit
     * 
     * @param quantity
     * @param totalCost
     */
    private double calculatePricePerUnit(Number quantity, Number totalCost)
    {
        if (quantity != null && totalCost != null && quantity.doubleValue() > 0 && totalCost.doubleValue() > 0)
        {
            return totalCost.doubleValue() / quantity.doubleValue();
        }
        return 0;
    }

    @Override
    protected EventBean createNewEvent()
    {
        // called to create new event
        EventBean newEvent = new EventBean();
        newEvent.setEventType(EventType.FUEL_EVENT);
        newEvent.setEventDate(Utils.getCurrentDate());
        return newEvent;
    }

    /**
     * Loads the fuel event to the form views
     */
    @Override
    protected void loadEventToForm()
    {
        Log.d(FuelEventActivity.class.getName(), "loadEventToForm()");

        // select the vehicle
        Utils.setVehicleSpinnerSelection(this, R.id.vehicle_spinner, event.getVehicleRef());

        // set date formatted
        Utils.setDateViewValue(this, R.id.date_edit_text, event.getEventDate());

        // Set fuel choices depending on vehicle
        String[] fuels = VehicleDB.getVehicleFuels(this, event.getVehicleRef());
        Utils.setSpinnerSelection(this, R.id.fuel_type_spinner, fuels, event.getFuel());

        // quantity
        Utils.setNumberViewValue(this, R.id.quantity_number_view, event.getQuantity());

        // price
        Utils.setSuffixViewValue(this, R.id.price_per_unit_text_view, event.getPrice());

        // total cost
        Utils.setNumberViewValue(this, R.id.total_cost_number_view, event.getCost());

        // odometer
        Utils.setNumberViewValue(this, R.id.odometer_number_view, event.getOdometer());

        Utils.setViewText(this, R.id.place_edit_text, event.getPlace());
        Utils.setViewText(this, R.id.note_edit_text, event.getNote());
    }

    /**
     * Saves form values to the current event object
     */
    @Override
    protected void saveFormToEvent()
    {
        Log.d(FuelEventActivity.class.getName(), "saveFormToEvent()");

        // set vehicle reference
        event.setVehicleRef(Utils.getSpinnerValueAsLong(this, R.id.vehicle_spinner));

        // set event date
        event.setEventDate(Utils.getDateViewValue(this, R.id.date_edit_text));

        // set fuel
        event.setFuel(Utils.getSpinnerValueAsStr(this, R.id.fuel_type_spinner));

        // quantity
        event.setQuantity(Utils.getNumberViewValue(this, R.id.quantity_number_view));

        // price
        event.setPrice(Utils.getSuffixViewValue(this, R.id.price_per_unit_text_view));

        // total cost
        event.setCost(Utils.getNumberViewValue(this, R.id.total_cost_number_view));

        // odometer
        event.setOdometer(Utils.getNumberViewValueAsInt(this, R.id.odometer_number_view));

        event.setPlace(Utils.getViewText(this, R.id.place_edit_text));
        event.setNote(Utils.getViewText(this, R.id.note_edit_text));
    }

    /**
     * initialize any spinners and auto complete text views
     */
    @Override
    protected void initChoices()
    {
        Log.d(FuelEventActivity.class.getName(), "initChoices()");

        // fill spinner with vehicles names
        Spinner vSpinner = (Spinner) findViewById(R.id.vehicle_spinner);
        VehicleSpinnerAdapter vsa = new VehicleSpinnerAdapter(this, false);
        vSpinner.setAdapter(vsa);

        // fill fuel spinner with fuel names
        Spinner fSpinner = (Spinner) findViewById(R.id.fuel_type_spinner);
        FuelSpinnerAdapter fsa = new FuelSpinnerAdapter(this, false);
        fSpinner.setAdapter(fsa);
    }

    // /////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.fuel_event, menu);
        return true;
    }
}
