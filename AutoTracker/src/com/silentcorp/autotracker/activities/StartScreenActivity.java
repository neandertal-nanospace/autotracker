package com.silentcorp.autotracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.VehicleDB;

public class StartScreenActivity extends SherlockFragmentActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        // Check if there are any active vehicles and show hint
        // only at startup
        if (!VehicleDB.hasActiveVehicles(this))
        {
            Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.text_you_need_vehicle),
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // Check if there are any active vehicles and enable components
        // or show info otherwise
        boolean enable = VehicleDB.hasActiveVehicles(this);
        // disable fuel up/maintenance/repair
        findViewById(R.id.button_add_fill_up).setEnabled(enable);
        findViewById(R.id.button_add_maintenance).setEnabled(enable);
        findViewById(R.id.button_add_repair).setEnabled(enable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean result = false;
        // back button in action bar
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else if (item.getItemId() == R.id.action_settings)
        {
            // start vehicle activity to create new vehicle
            openSettingsActivity();
            result = true;
        }
        else if (item.getItemId() == R.id.action_about)
        {
            // delete selected vehicles
            openAboutFragment();
            result = true;
        }
        else
        {
            result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    // Open Add Fill-up screen
    public void openFuelEventActivity(View view)
    {
        Intent intent = new Intent(this, FuelEventActivity.class);
        startActivity(intent);
    }

    // Open Add Notification screen
    public void openNotificationActivity(View view)
    {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    // Open Add Payment screen
    public void openPaymentActivity(View view)
    {
        Intent intent = new Intent(this, PaymentEventActivity.class);
        startActivity(intent);
    }

    // Open Add Repair record screen
    public void openRepairActivity(View view)
    {
        Intent intent = new Intent(this, RepairEventActivity.class);
        startActivity(intent);
    }

    // Open Add Maintenance record screen
    public void openMaintenanceActivity(View view)
    {
        Intent intent = new Intent(this, MaintenanceEventActivity.class);
        startActivity(intent);
    }

    // Open List vehicles activity
    public void openListVehiclesActivity(View view)
    {
        Intent intent = new Intent(this, ListVehiclesActivity.class);
        startActivity(intent);
    }

    // Open List vehicles activity
    public void openListEventsActivity(View view)
    {
        Intent intent = new Intent(this, ListEventsActivity.class);
        startActivity(intent);
    }

    // Open List vehicles activity
    public void openListNotificationsActivity(View view)
    {
        Intent intent = new Intent(this, ListNotificationsActivity.class);
        startActivity(intent);
    }

    // Open Statistics activity
    public void openStatisticsActivity(View view)
    {
        // TODO Intent intent = new Intent(this,
        // ListNotificationsActivity.class);
        // startActivity(intent);
    }

    // Open Settings screen
    public void openSettingsActivity()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Open About fragment
    public void openAboutFragment()
    {
        // TODO
    }

}
