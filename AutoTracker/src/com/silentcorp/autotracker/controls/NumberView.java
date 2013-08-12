package com.silentcorp.autotracker.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.controls.numberview.NumberViewDialog;
import com.silentcorp.autotracker.utils.DoubleNumber;
import com.silentcorp.autotracker.utils.Utils;

/**
 * Extends EditText to show number chooser dialog on click and to display the
 * numbers with metrics
 * 
 * @author neandertal
 */
public class NumberView extends EditText implements NumberViewDialog.OnNumberSetListener
{
    private NumberViewDialog dialog;
    private OnNumberChangeListener ncListener;
    private int normalColor;
    private boolean formatGroups = true;

    public NumberView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public NumberView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberView(Context context)
    {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setClickable(true);

        normalColor = getCurrentTextColor();

        dialog = new NumberViewDialog();
        dialog.setOnNumberSetListener(this);

        setFocusable(false);
        setGravity(Gravity.RIGHT);

        // show dialog
        setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SherlockFragmentActivity activity = (SherlockFragmentActivity) v.getContext();
                dialog.show(activity.getSupportFragmentManager(), "NumberViewDialog");
            }
        });

        setValue(new DoubleNumber());

        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberView, 0, 0);

            setValueDecimal(a.getBoolean(R.styleable.NumberView_valueDecimal, false));

            setNullAllowed(a.getBoolean(R.styleable.NumberView_nullAllowed, false));

            formatGroups = a.getBoolean(R.styleable.NumberView_formatGroups, true);
            
            setSuffix(a.getString(R.styleable.NumberView_suffix));

            setDialogTitle(a.getString(R.styleable.NumberView_dialogTitle));

            if (a.hasValue(R.styleable.NumberView_step))
            {
                int step = a.getInt(R.styleable.NumberView_step, 1);
                setStep(step);
            }

            if (a.hasValue(R.styleable.NumberView_rangeMin))
            {
                float min = a.getFloat(R.styleable.NumberView_rangeMin, 0);
                setRangeMin(min);
            }

            if (a.hasValue(R.styleable.NumberView_rangeMax))
            {
                float max = a.getFloat(R.styleable.NumberView_rangeMax, Integer.MAX_VALUE);
                setRangeMax(max);
            }

            a.recycle();
        }
    }

    /**
     * Called when a number is set to update this view
     * 
     * @param number
     */
    @Override
    public void onNumberSet(DoubleNumber oldNumber, DoubleNumber newNumber)
    {
        setInternal(newNumber);

        fireNumberChange(oldNumber, newNumber);
    }

    /**
     * Set title of this number view to be used in NumberPickerDialog
     * 
     * @param title
     */
    public void setDialogTitle(String dialogTitleArg)
    {
        dialog.setDialogTitle(dialogTitleArg);
    }

    /**
     * Set suffix.
     * 
     * @param suffArg
     */
    public void setSuffix(String suffixArg)
    {
        dialog.setSuffix(suffixArg);
    }

    /**
     * Get value
     * 
     * @return
     */
    public DoubleNumber getValue()
    {
        return dialog.getValue();
    }

    /**
     * Set this view value
     * 
     * @param newValue
     */
    public void setValue(DoubleNumber valueArg)
    {
        DoubleNumber oldValue = dialog.getValue();
        dialog.setValue(valueArg);

        setInternal(dialog.getValue());

        fireNumberChange(oldValue, dialog.getValue());
    }

    private void setInternal(DoubleNumber newNumber)
    {
        String sValue = Utils.format(newNumber, dialog.isNullAllowed(), dialog.isValueDecimal(), formatGroups);

        boolean markEmptyValue = !dialog.isNullAllowed() && newNumber.isNull();

        String suffix = dialog.getSuffix();
        if (suffix != null)
        {
            sValue = sValue + " " + suffix;
        }

        super.setText(sValue);
        super.setTextColor((markEmptyValue) ? Color.RED : normalColor);
    }

    /**
     * Forces refresh
     */
    public void refresh()
    {
        setInternal(dialog.getValue());
    }

    /** Sets if this view values are decimal and should be formatted accordingly */
    public void setValueDecimal(boolean isValueDecimal)
    {
        dialog.setValueDecimal(isValueDecimal);
    }

    /**
     * Sets low limits
     * 
     * @param low
     */
    public void setRangeMin(Number rangeMinArg)
    {
        dialog.setRangeMin(rangeMinArg);
    }

    /**
     * Sets high limits
     * 
     * @param high
     */
    public void setRangeMax(Number rangeMaxArg)
    {
        dialog.setRangeMax(rangeMaxArg);
    }

    /**
     * Set step for the number picker dialog
     * 
     * @param step
     */
    public void setStep(Number stepArg)
    {
        dialog.setStep(stepArg);
    }

    /**
     * Set number change listener
     * 
     * @param ncListArg
     */
    public void setNumberChangeListener(OnNumberChangeListener ncListArg)
    {
        ncListener = ncListArg;
    }

    public boolean isNullAllowed()
    {
        return dialog.isNullAllowed();
    }

    public void setNullAllowed(boolean isNullAllowed)
    {
        dialog.setNullAllowed(isNullAllowed);
    }

    /**
     * Fire event
     * 
     * @param oldValue
     * @param newValue
     */
    private void fireNumberChange(DoubleNumber oldValue, DoubleNumber newValue)
    {
        if (ncListener != null)
        {
            ncListener.onChange(oldValue, newValue);
        }
    }

    /**
     * Listener for when number is changed.
     * 
     * @author neandertal
     */
    public interface OnNumberChangeListener
    {
        public void onChange(DoubleNumber oldValue, DoubleNumber newValue);
    }
}
