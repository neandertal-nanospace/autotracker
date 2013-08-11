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

        setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SherlockFragmentActivity activity = (SherlockFragmentActivity) v.getContext();
                dialog.show(activity.getSupportFragmentManager(), "NumberViewDialog");
            }
        });

        if (attrs != null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberView, 0, 0);

            setValueDecimal(a.getBoolean(R.styleable.NumberView_valueDecimal, false));

            setNullAllowed(a.getBoolean(R.styleable.NumberView_nullAllowed, false));

            setSuffix(a.getString(R.styleable.NumberView_suffix));

            if (a.hasValue(R.styleable.NumberView_value))
            {
                setValue(a.getFloat(R.styleable.NumberView_value, 0.0f));
            }

            setDialogTitle(a.getString(R.styleable.NumberView_dialogTitle));

            if (a.hasValue(R.styleable.NumberView_step))
            {
                float step = a.getFloat(R.styleable.NumberView_step, 1);
                setStep((double) step);
            }

            if (a.hasValue(R.styleable.NumberView_rangeMin))
            {
                float min = a.getFloat(R.styleable.NumberView_rangeMin, Float.MIN_VALUE);
                setRangeMin((double) min);
            }

            if (a.hasValue(R.styleable.NumberView_rangeMax))
            {
                float max = a.getFloat(R.styleable.NumberView_rangeMax, Float.MAX_VALUE);
                setRangeMax((double) max);
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
    public void onNumberSet(Number newNumber, Number oldNumber)
    {
        setInternal(newNumber);

        fireNumberChange(newNumber, oldNumber);
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
    public Double getValueAsDouble()
    {
        if (dialog.isEmptyValue())
        {
            return null;
        }

        return dialog.getValue().doubleValue();
    }

    /**
     * Get value
     * 
     * @return
     */
    public Integer getValueAsInteger()
    {
        if (dialog.isEmptyValue())
        {
            return null;
        }

        return dialog.getValue().intValue();
    }

    /**
     * Get value
     * 
     * @return
     */
    public Long getValueAsLong()
    {
        if (dialog.isEmptyValue())
        {
            return null;
        }

        return dialog.getValue().longValue();
    }

    /**
     * Set this view value
     * 
     * @param newValue
     */
    public void setValue(Number valueArg)
    {
        Number oldValue = dialog.getValue();
        dialog.setValue(valueArg);

        setInternal(dialog.getValue());

        fireNumberChange(dialog.getValue(), oldValue);
    }

    private void setInternal(Number newNumber)
    {
        String sValue = null;
        boolean markEmptyValue = false;

        if (newNumber == null)
        {
            sValue = "---";
        }
        else
        {
            if (dialog.isValueDecimal())
            {
                sValue = Utils.df.format(newNumber.doubleValue());
            }
            else
            {
                sValue = Long.toString(newNumber.longValue());
            }
            
            markEmptyValue = dialog.isEmptyValue();
        }

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
    private void fireNumberChange(Number newValue, Number oldValue)
    {
        if (ncListener != null)
        {
            ncListener.onChange(oldValue, newValue, dialog.isEmptyValue());
        }
    }

    /**
     * Listener for when number is changed.
     * 
     * @author neandertal
     */
    public interface OnNumberChangeListener
    {
        public void onChange(Number oldValue, Number newValue, boolean isEmptyValue);
    }
}
