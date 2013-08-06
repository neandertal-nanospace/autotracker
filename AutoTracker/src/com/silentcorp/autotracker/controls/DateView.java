package com.silentcorp.autotracker.controls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.silentcorp.autotracker.db.Utils;

public class DateView extends EditText implements DatePickerDialog.OnDateSetListener
{
    private SimpleDateFormat formatter;
    private Long date;
    private Context context;

    public DateView(Context contextArg, AttributeSet attrs, int defStyle)
    {
        super(contextArg, attrs, defStyle);
        context = contextArg;
        init();
    }

    public DateView(Context contextArg, AttributeSet attrs)
    {
        super(contextArg, attrs);
        context = contextArg;
        init();
    }

    public DateView(Context contextArg)
    {
        super(contextArg);
        context = contextArg;
        init();
    }

    private void init()
    {
        // formatter
        formatter = new SimpleDateFormat(Utils.getSelectedDateFormat(context));

        setFocusable(false);
        
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDateDialog();
            }
        });
    }

    private void showDateDialog()
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis((date == null) ? System.currentTimeMillis() : date);

        DatePickerDialog dp = new DatePickerDialog(context, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        Calendar cal = new GregorianCalendar(year, month, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        date = Long.valueOf(cal.getTimeInMillis());

        // format the date
        String formattedDate = formatter.format(cal.getTime());
        setText(formattedDate);
    }

    public void setDate(Long dateArg)
    {
        date = dateArg;
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis((date == null) ? System.currentTimeMillis() : date);

        // format the date
        String formattedDate = formatter.format(cal.getTime());
        setText(formattedDate);
    }

    public Long getDate()
    {
        return date;
    }
}
