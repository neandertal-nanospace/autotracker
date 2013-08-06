/*
 * Copyright (C) 2010-2012 Mike Novak <michael.novakjr@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.silentcorp.autotracker.controls.numberpicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.silentcorp.autotracker.R;

public class NumberPickerDialog extends AlertDialog implements OnClickListener
{
    /**
     * Binding interface for events notification
     * @author neandertal
     */
    public interface OnNumberSetListener
    {
        public void onNumberSet(Double selectedNumber);
    }
    
    private OnNumberSetListener mListener;
    private NumberPicker mNumberPicker;
    private TextView suffixLabel;

    public NumberPickerDialog(Context context)
    {
        super(context,  -1);

        setButton(BUTTON_POSITIVE, context.getString(R.string.text_set), this);
        setButton(BUTTON_NEGATIVE, context.getString(R.string.text_cancel), (OnClickListener) null);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_number_picker, null);
        setView(view);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.num_picker);
        suffixLabel = (TextView) view.findViewById(R.id.label_metric);
        
        setSuffix(null);
    }

    /**
     * Set the range allowed for the number picker
     * 
     * @param low the minimum allowed value
     * @param high the maximum allowed value
     */
    public void setRange(Double low, Double high)
    {
        mNumberPicker.setRange(low, high);
    }

    /**
     * Set listener for result
     * @param listener
     */
    public void setOnNumberSetListener(OnNumberSetListener listener)
    {
        mListener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if (mListener != null)
        {
            mListener.onNumberSet(mNumberPicker.getCurrent());
        }
    }

    /**
     * Set the step
     * @param step
     */
    public void setStep(Double step)
    {
        mNumberPicker.setStep(step);
    }

    /**
     * Set is value decimal type
     * @param isDecimalArg
     */
    public void setValueDecimal(boolean isDecimalArg)
    {
        mNumberPicker.setDecimal(isDecimalArg);
    }

    /**
     * Set current value
     * @param newValue
     */
    public void setCurrent(double newValue)
    {
        mNumberPicker.setCurrent(newValue);
    }

    /**
     * Set suffix label. NULL makes it invisible.
     * @param suffArg
     */
    public void setSuffix(String suffArg)
    {
        suffixLabel.setVisibility((suffArg == null) ? View.GONE: View.VISIBLE);
        suffixLabel.setText(suffArg);
    }
}