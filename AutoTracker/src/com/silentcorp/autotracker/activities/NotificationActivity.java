package com.silentcorp.autotracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.beans.NotificationBean;
import com.silentcorp.autotracker.controls.NumberView;
import com.silentcorp.autotracker.controls.spinneradapter.VehicleSpinnerAdapter;
import com.silentcorp.autotracker.db.NotificationDB;
import com.silentcorp.autotracker.db.Utils;

/**
 * Add/Edit notification activity. Used to add or edit a notification to DB.
 * 
 * @author neandertal
 * 
 */
public class NotificationActivity extends SherlockActivity
{
    private NotificationBean notification;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // back button in action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        // initialize any spinners and auto complete text views
        initChoices();

        // add listeners
        initListeners();
    }

    private void initViews()
    {
        String timeSuff = getString(R.string.text_days);
        String distSuff = Utils.getLocalizedDistanceSuffix(this);

        // initialize date_repeat_number_view view
        NumberView dateRepeatView = (NumberView) findViewById(R.id.date_repeat_number_view);
        dateRepeatView.setDialogTitle(getString(R.string.text_repeat_every));
        dateRepeatView.setStep(1.0);
        dateRepeatView.setValueDecimal(false);
        dateRepeatView.setSuffix(timeSuff);

        // initialize date_remainder_number_view view
        NumberView dateRemainderView = (NumberView) findViewById(R.id.date_remainder_number_view);
        dateRemainderView.setDialogTitle(getString(R.string.text_advance_reminder_in));
        dateRemainderView.setStep(1.0);
        dateRemainderView.setValueDecimal(false);
        dateRemainderView.setSuffix(timeSuff);

        // initialize period_repeat_number_view view
        NumberView periodRepeatView = (NumberView) findViewById(R.id.period_repeat_number_view);
        periodRepeatView.setDialogTitle(getString(R.string.text_repeat_every));
        periodRepeatView.setStep(1.0);
        periodRepeatView.setValueDecimal(false);
        periodRepeatView.setSuffix(timeSuff);

        // initialize period_remainder_number_view view
        NumberView periodRemainderView = (NumberView) findViewById(R.id.period_remainder_number_view);
        periodRemainderView.setDialogTitle(getString(R.string.text_advance_reminder_in));
        periodRemainderView.setStep(1.0);
        periodRemainderView.setValueDecimal(false);
        periodRemainderView.setSuffix(timeSuff);

        // initialize distance_last_occurance_number_view view
        NumberView distanceLastView = (NumberView) findViewById(R.id.distance_last_occurance_number_view);
        distanceLastView.setDialogTitle(getString(R.string.text_last_occurance_at));
        distanceLastView.setStep(1000.0);
        distanceLastView.setValueDecimal(false);
        distanceLastView.setSuffix(distSuff);

        // initialize distance_repeat_number_view view
        NumberView distanceRepeatView = (NumberView) findViewById(R.id.distance_repeat_number_view);
        distanceRepeatView.setDialogTitle(getString(R.string.text_repeat_every));
        distanceRepeatView.setStep(1000.0);
        distanceRepeatView.setValueDecimal(false);
        distanceRepeatView.setSuffix(distSuff);
        distanceRepeatView.setHint(getString(R.string.hint_x) + " " + distSuff);

        // initialize distance_remainder_number_view view
        NumberView distanceRemainderView = (NumberView) findViewById(R.id.distance_remainder_number_view);
        distanceRemainderView.setDialogTitle(getString(R.string.text_advance_reminder_in));
        distanceRemainderView.setStep(1000.0);
        distanceRemainderView.setValueDecimal(false);
        distanceRemainderView.setSuffix(distSuff);
        distanceRemainderView.setHint(getString(R.string.hint_y) + " " + distSuff);
    }

    /**
     * Add listeners
     */
    private void initListeners()
    {
        // Initially disable save button
        Button saveBtn = (Button) findViewById(R.id.save_button);
        saveBtn.setEnabled(false);

        TextView activityView = (TextView) findViewById(R.id.activity_edit_text);
        activityView.addTextChangedListener(new TextWatcher()
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
                Button saveBtn = (Button) findViewById(R.id.save_button);
                saveBtn.setEnabled(s.length() > 0);
            }
        });

        // TODO disable the activity until a due date/period/distance is entered

        // change text of enabled check box
        CheckBox enabledChk = (CheckBox) findViewById(R.id.enabled_checkbox);
        enabledChk.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                buttonView.setText((isChecked) ? R.string.text_yes : R.string.text_no);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.d(NotificationActivity.class.getName(), "onStart()");

        Intent intent = getIntent();
        long notificationID = intent.getLongExtra(Utils.KEY_SELECTED_ID, -1l);

        if (notificationID < 0)
        {
            // change title
            setTitle(R.string.text_add_notification);

            // called to create new notification
            notification = new NotificationBean();
            Log.d(NotificationActivity.class.getName(), "Created new notification.");
        }
        else
        {
            // change title
            setTitle(R.string.text_edit_notification);

            notification = NotificationDB.readNotification(this, notificationID, notification);
            Log.d(NotificationActivity.class.getName(), "Read notification from DB:" + notificationID);

            // called to edit existing notification
            loadNotificationToForm(notificationID);
        }
    }

    /**
     * Called by "Cancel" button
     * 
     * @param view
     */
    public void onCancel(View view)
    {
        Log.d(NotificationActivity.class.getName(), "onCancel()");
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Called by "Save" button
     * 
     * @param view
     */
    public void onSave(View view)
    {
        Log.d(NotificationActivity.class.getName(), "onSave()");
        saveFormToNotification();

        // Update DB
        if (notification.getId() < 0)
        {
            NotificationDB.createNotification(this, notification);
        }
        else
        {
            NotificationDB.updateNotification(this, notification);
        }

        // set result to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Utils.KEY_SELECTED_ID, notification.getId());
        setResult(RESULT_OK, resultIntent);
        // finish activity
        finish();
    }

    /**
     * Back key pressed - cancel the operation.
     */
    @Override
    public void onBackPressed()
    {
        onCancel(null);
        return;
    }

    /**
     * Loads the notification to the form views
     */
    private void loadNotificationToForm(long notificationID)
    {
        Log.d(NotificationActivity.class.getName(), "loadNotificationToForm()");

        Utils.setViewText(this, R.id.activity_edit_text, notification.getActivity());

        // select the vehicle
        Utils.setVehicleSpinnerSelection(this, R.id.vehicle_spinner, notification.getVehicleRef());

        // set enabled check box
        Utils.setCheckboxSelection(this, R.id.enabled_checkbox, notification.getEnabled());

        Utils.setViewText(this, R.id.note_edit_text, notification.getNote());

        // group date
        Utils.setDateViewValue(this, R.id.date_due_edit_text, notification.getDateDue());
        // date repeat every X days
        Utils.setNumberViewValue(this, R.id.date_repeat_number_view, notification.getDateRepeat());
        // date remainder in X days
        Utils.setNumberViewValue(this, R.id.date_remainder_number_view, notification.getDateAdvance());
                
        // group period
        Utils.setDateViewValue(this, R.id.period_last_occurance_edit_text, notification.getPeriodLast());
        // period repeat every X days
        Utils.setNumberViewValue(this, R.id.period_repeat_number_view, notification.getPeriodRepeat());
        // period remainder in X day
        Utils.setNumberViewValue(this, R.id.period_remainder_number_view, notification.getPeriodAdvance());

        // group distance
        // distance last occurance
        Utils.setNumberViewValue(this, R.id.distance_last_occurance_number_view, notification.getDistanceRepeat());
        // distance repeat every X km
        Utils.setNumberViewValue(this, R.id.distance_repeat_number_view, notification.getDistanceRepeat());
        // distance remainder in X km
        Utils.setNumberViewValue(this, R.id.distance_remainder_number_view, notification.getDistanceAdvance());
    }

    /**
     * Saves form values to the current notification object
     */
    private void saveFormToNotification()
    {
        Log.d(NotificationActivity.class.getName(), "saveFormToNotification()");

        notification.setActivity(Utils.getViewText(this, R.id.activity_edit_text));

        // set vehicle reference
        notification.setVehicleRef(Utils.getVehicleSpinnerValue(this, R.id.vehicle_spinner));

        // set enabled checkbox
        notification.setEnabled(Utils.getCheckboxValue(this, R.id.enabled_checkbox));

        notification.setNote(Utils.getViewText(this, R.id.note_edit_text));

        // group date
        notification.setDateDue(Utils.getDateViewValue(this, R.id.date_due_edit_text));
        // date repeat
        notification.setDateRepeat(Utils.getNumberViewValueAsInt(this, R.id.date_repeat_number_view));
        // date advance remainder
        notification.setDateAdvance(Utils.getNumberViewValueAsInt(this, R.id.date_remainder_number_view));

        // group period
        notification.setPeriodLast(Utils.getDateViewValue(this, R.id.period_last_occurance_edit_text));
        // period repeat
        notification.setPeriodRepeat(Utils.getNumberViewValueAsInt(this, R.id.period_repeat_number_view));
        // period advance remainder
        notification.setPeriodAdvance(Utils.getNumberViewValueAsInt(this, R.id.period_remainder_number_view));

        // group distance
        notification.setDistanceLast(Utils.getNumberViewValueAsInt(this, R.id.distance_last_occurance_number_view));
        notification.setDistanceRepeat(Utils.getNumberViewValueAsInt(this, R.id.distance_repeat_number_view));
        notification.setDistanceAdvance(Utils.getNumberViewValueAsInt(this, R.id.distance_remainder_number_view));
    }

    /**
     * initialize any spinners and auto complete text views
     */
    private void initChoices()
    {
        Log.d(NotificationActivity.class.getName(), "initChoices()");

        // fill spinner with vehicles names, including empty choice
        Spinner vSpinner = (Spinner) findViewById(R.id.vehicle_spinner);
        VehicleSpinnerAdapter vsa = new VehicleSpinnerAdapter(this, true);
        vSpinner.setAdapter(vsa);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // back button in action bar
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
