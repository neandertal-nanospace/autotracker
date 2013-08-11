package com.silentcorp.autotracker.controls.numberview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.utils.Utils;

/**
 * A custom DialogFragment to show custom AlertDialog, allowing the user to
 * choose a number between MIN and MAX values. Optionally the value can be
 * <NULL>, has increase and decrease buttons.
 * 
 * @author neandertal
 * 
 */
public class NumberViewDialog extends DialogFragment implements OnClickListener, OnEditorActionListener,
        OnFocusChangeListener, OnLongClickListener
{
    private static final int DEFAULT_RANGE_MAX = Integer.MAX_VALUE;
    private static final int DEFAULT_RANGE_MIN = 0;
    private static final int DEFAULT_STEP = 1;

    private String dialogTitle = "";
    private Number value = null;
    private int step = DEFAULT_STEP;
    private boolean isNullAllowed = false;
    private boolean isValueDecimal = false;
    private double rangeMin = DEFAULT_RANGE_MIN;
    private double rangeMax = DEFAULT_RANGE_MAX;
    private String suffix = null;

    private Number oldValue = null;
    private EditText inputView;

    private OnNumberSetListener mListener;

    /**
     * Binding interface for events notification
     * 
     * @author neandertal
     */
    public interface OnNumberSetListener
    {
        public void onNumberSet(Number newNumber, Number oldNumber);
    }

    /**
     * Contructor
     */
    public NumberViewDialog()
    {}

    @Override
    public void show(FragmentManager manager, String tag)
    {

        super.show(manager, tag);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // keep old value
        oldValue = value;

        Activity activity = getActivity();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_number_view, null);

        // Increase button text
        Button increaseBtn = (Button) contentView.findViewById(R.id.increase_button);
        increaseBtn.setText("+ " + Integer.toString(step));
        increaseBtn.setOnClickListener(this);
        increaseBtn.setOnLongClickListener(this);

        // decrease button text
        Button decreaseBtn = (Button) contentView.findViewById(R.id.decrease_button);
        decreaseBtn.setText("- " + Integer.toString(step));
        decreaseBtn.setOnClickListener(this);
        decreaseBtn.setOnLongClickListener(this);

        TextView suffixLabel = (TextView) contentView.findViewById(R.id.metric_label);
        if (suffix == null)
        {
            suffixLabel.setVisibility(View.GONE);
        }
        else
        {
            suffixLabel.setVisibility(View.VISIBLE);
            suffixLabel.setText(suffix);
        }

        // initialize edit view
        inputView = (EditText) contentView.findViewById(R.id.input_view);
        KeyListener kl = new CustomKeyListener(isValueDecimal);
        inputView.setKeyListener(kl);
        inputView.setOnFocusChangeListener(this);
        inputView.setOnEditorActionListener(this);
        setInput();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(contentView);

        builder.setTitle(dialogTitle);

        // TODO title icon

        // Set button
        builder.setPositiveButton(R.string.text_set, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                value = readInput();

                if (mListener != null)
                {
                    mListener.onNumberSet(value, oldValue);
                }
            }
        });

        // Clear button
        builder.setNeutralButton(R.string.text_clear, null);

        // Cancel button
        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                value = oldValue;
                setInput();
            }
        });

        // Create the AlertDialog, hack the neutral button to not close the
        // dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        Button neutralBtn = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isNullAllowed)
                {
                    value = null;
                }
                else
                {
                    value = rangeMin;
                }

                setInput();
            }
        });

        return dialog;
    }

    // //////////////////
    // Getters and setters

    public String getDialogTitle()
    {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitleArg)
    {
        if (dialogTitleArg == null)
        {
            dialogTitle = "";
        }
        dialogTitle = dialogTitleArg;
    }

    public Number getValue()
    {
        return value;
    }
    
    /**
     * Returns true if not NULL enabled and value == minimum
     * @return
     */
    public boolean isEmptyValue()
    {
        if (value != null && !isNullAllowed && rangeMin == value.doubleValue())
        {
            return true;
        }
        
        if (value == null)
        {
            return true;
        }
        
        return false;
    }

    public void setValue(Number valueArg)
    {
        if (valueArg == null && !isNullAllowed)
        {
            value = rangeMin;
            return;
        }

        value = valueArg;
    }

    public double getStep()
    {
        return step;
    }

    public void setStep(Number stepArg)
    {
        if (stepArg == null || stepArg.intValue() < 0)
        {
            step = DEFAULT_STEP;
            return;
        }

        step = stepArg.intValue();
    }

    public boolean isNullAllowed()
    {
        return isNullAllowed;
    }

    public void setNullAllowed(boolean isNullAllowed)
    {
        this.isNullAllowed = isNullAllowed;
    }

    public boolean isValueDecimal()
    {
        return isValueDecimal;
    }

    public void setValueDecimal(boolean isValueDecimal)
    {
        this.isValueDecimal = isValueDecimal;
    }

    public double getRangeMin()
    {
        return rangeMin;
    }

    public void setRangeMin(Number rangeMinArg)
    {
        if (rangeMinArg == null || rangeMinArg.doubleValue() < DEFAULT_RANGE_MIN
                || rangeMinArg.doubleValue() > rangeMax)
        {
            rangeMin = DEFAULT_RANGE_MIN;
            return;
        }
        rangeMin = rangeMinArg.doubleValue();
    }

    public double getRangeMax()
    {
        return rangeMax;
    }

    public void setRangeMax(Number rangeMaxArg)
    {
        if (rangeMaxArg == null || rangeMaxArg.doubleValue() > DEFAULT_RANGE_MAX
                || rangeMaxArg.doubleValue() < rangeMin)
        {
            rangeMax = DEFAULT_RANGE_MAX;
            return;
        }
        rangeMax = rangeMaxArg.doubleValue();
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffixArg)
    {
        suffix = suffixArg;
    }

    /**
     * Set listener for result
     * 
     * @param listener
     */
    public void setOnNumberSetListener(OnNumberSetListener listener)
    {
        mListener = listener;
    }

    // ///////////////////////
    // / Handle events

    /**
     * When Increase or decrease buttons pressed
     */
    @Override
    public boolean onLongClick(View v)
    {
        onClick(v);
        return true;
    }

    /**
     * When Increase or decrease buttons pressed
     */
    @Override
    public void onClick(View v)
    {
        value = readInput();

        if (!inputView.hasFocus())
        {
            inputView.requestFocus();
        }

        // now perform the increment/decrement
        if (R.id.increase_button == v.getId())
        {
            increment();
        }
        else if (R.id.decrease_button == v.getId())
        {
            decrement();
        }

        setInput();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        // When focus is lost check that the text field has valid values.
        if (!hasFocus)
        {
            value = readInput();
            setInput();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (v == inputView)
        {
            value = readInput();
            setInput();
        }

        // Don't return true, let Android handle the soft keyboard
        return false;
    }

    // /////////////////////////
    // Internal

    private void decrement()
    {
        if (value == null)
        {
            value = rangeMin;
        }
        else
        {
            double newValue = value.doubleValue() - step;
            newValue = Math.max(newValue, rangeMin);
            value = newValue;
        }
    }

    private void increment()
    {
        if (value == null)
        {
            value = rangeMin + step;
        }
        else
        {
            double newValue = value.doubleValue() + step;
            newValue = Math.min(newValue, rangeMax);
            value = newValue;
        }
    }

    private void setInput()
    {
        // Set back to input view
        String newText = formatNumber(value);

        int curSelectedPos = inputView.getSelectionStart();
        int curTextLength = inputView.getText().length();

        inputView.setText(newText);

        // Preserve previous cursor position (right justified)
        int selectPos = newText.length();
        selectPos -= (curTextLength - Math.max(curSelectedPos, 0));
        selectPos = Math.max(selectPos, 0);
        inputView.setSelection(selectPos);
    }

    /**
     * Read from inputView
     * 
     * @param v
     */
    private Number readInput()
    {
        String str = String.valueOf(inputView.getText());
        if ("".equals(str))
        {
            // If empty values are allowed, set value to NULL
            if (isNullAllowed)
            {
                return null;
            }

            return rangeMin;
        }
        else
        {
            double val = 0;
            try
            {
                val = Double.parseDouble(str);
            }
            catch (NumberFormatException e)
            {
                return value;
            }

            if (val < rangeMin)
            {
                return rangeMin;
            }

            if (val > rangeMax)
            {
                return rangeMax;
            }

            return val;
        }
    }

    /**
     * Format value to string, NULL is resolved to empty string
     * 
     * @param value
     * @return
     */
    private String formatNumber(Number value)
    {
        if (value == null)
        {
            return "";
        }

        return (isValueDecimal) ? Utils.df.format(value) : Integer.toString(value.intValue());
    }
}
