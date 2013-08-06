/*
 * Copyright 2008 The Android Open Source Project
 * Copyright 2011-2012 Michael Novak <michael.novakjr@gmail.com>.
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

import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.silentcorp.autotracker.R;
import com.silentcorp.autotracker.db.Utils;

/**
 * This class has been pulled from the Android platform source code, its an
 * internal widget that hasn't been made public so its included in the project
 * in this fashion for use with the preferences screen; I have made a few slight
 * modifications to the code here, I simply put a MAX and MIN default in the
 * code but these values can still be set publicly by calling code.
 * 
 * @author Google
 * @author Michael Novak
 * @author neandertal
 */
public class NumberPicker extends LinearLayout implements OnClickListener, OnEditorActionListener,
        OnFocusChangeListener, OnLongClickListener
{
    private static final double DEFAULT_MAX = 1000000;
    private static final double DEFAULT_MIN = 0;
    private static final double DEFAULT_VALUE = 0;
    private static final double DEFAULT_STEP = 1;
    private static final boolean DEFAULT_IS_DECIMAL = false;
    private static final boolean DEFAULT_WRAP = true;

    /**
     * Listener for events
     * 
     * @author neandertal
     */
    public interface OnChangedListener
    {
        void onChanged(NumberPicker picker, double oldVal, double newVal);
    }

    private final Handler mHandler;

    private final Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (mIncrement)
            {
                changeCurrent(mCurrent + mStep);
                mHandler.postDelayed(this, mSpeed);
            }
            else if (mDecrement)
            {
                changeCurrent(mCurrent - mStep);
                mHandler.postDelayed(this, mSpeed);
            }
        }
    };

    private final EditText mText;
    private final InputFilter mNumberInputFilter;

    // private String[] mDisplayedValues;
    protected double mStart;
    protected double mEnd;
    protected double mCurrent;
    protected double mStep;
    protected double mPrevious;
    protected OnChangedListener mListener;
    protected boolean mWrap;
    protected boolean mIsDecimal;
    protected long mSpeed = 300;
    private int mNumMaxDigitChars;

    private boolean mIncrement;
    private boolean mDecrement;

    public NumberPicker(Context context)
    {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.number_picker, this, true);
        mHandler = new Handler();
        InputFilter inputFilter = new NumberPickerInputFilter();
        mNumberInputFilter = new NumberRangeKeyListener();
        mIncrementButton = (NumberPickerButton) findViewById(R.id.increment);
        mIncrementButton.setOnClickListener(this);
        mIncrementButton.setOnLongClickListener(this);
        mIncrementButton.setNumberPicker(this);
        mDecrementButton = (NumberPickerButton) findViewById(R.id.decrement);
        mDecrementButton.setOnClickListener(this);
        mDecrementButton.setOnLongClickListener(this);
        mDecrementButton.setNumberPicker(this);

        mText = (EditText) findViewById(R.id.numpicker_input);
        mText.setOnFocusChangeListener(this);
        mText.setOnEditorActionListener(this);
        mText.setFilters(new InputFilter[] { inputFilter });// TODO

        if (!isEnabled())
        {
            setEnabled(false);
        }

        mWrap = DEFAULT_WRAP;
        mStep = DEFAULT_STEP;
        mIsDecimal = DEFAULT_IS_DECIMAL;
        setRangeInternal(DEFAULT_MIN, DEFAULT_MAX);
        setCurrentInternal(DEFAULT_VALUE);
        updateTextInputType();
        updateView();
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        mIncrementButton.setEnabled(enabled);
        mDecrementButton.setEnabled(enabled);
        mText.setEnabled(enabled);
    }

    public void setOnChangeListener(OnChangedListener listener)
    {
        mListener = listener;
    }

    /**
     * Set the range of numbers allowed for the number picker. The current value
     * will be automatically set to the start.
     * 
     * @param start the start of the range (inclusive)
     * @param end the end of the range (inclusive)
     */
    public void setRange(Double start, Double end)
    {
        if (start == null)
        {
            start = DEFAULT_MIN;
        }

        if (end == null)
        {
            end = DEFAULT_MAX;
        }

        setRangeInternal(start, end);
        updateView();
    }

    /**
     * Specify if numbers should wrap after the edge has been reached.
     * 
     * @param wrap values
     */
    public void setWrap(boolean wrap)
    {
        mWrap = wrap;
    }

    private void setRangeInternal(double start, double end)
    {
        if (end < start)
        {
            throw new IllegalArgumentException("End value cannot be less than the start value.");
        }

        mStart = start;
        mEnd = end;
        if (mCurrent < start)
        {
            mCurrent = start;
        }
        else if (mCurrent > end)
        {
            mCurrent = end;
        }

        mNumMaxDigitChars = formatNumber(Math.max(Math.abs(mStart), Math.abs(mEnd))).length();
        updateTextInputType();
    }

    private boolean isSignedNumberAllowed()
    {
        return (mStart < 0);
    }

    private void updateTextInputType()
    {
        int inputType = InputType.TYPE_CLASS_NUMBER;
        if (isSignedNumberAllowed())
        {
            inputType |= InputType.TYPE_NUMBER_FLAG_SIGNED;
        }
        mText.setInputType(inputType);
    }

    public void setCurrent(double current)
    {
        setCurrentInternal(current);
        updateView();
    }

    public void setCurrentAndNotify(int current)
    {
        setCurrentInternal(current);
        notifyChange();
        updateView();
    }

    private void setCurrentInternal(double current)
    {
        if (mStart > current)
        {
            throw new IllegalArgumentException("Current value cannot be less than the range start.");
        }
        if (mEnd < current)
        {
            throw new IllegalArgumentException("Current value cannot be greater than the range end.");
        }
        mCurrent = current;
    }

    /**
     * The speed (in milliseconds) at which the numbers will scroll when the the
     * +/- buttons are long pressed. Default is 300ms.
     */
    public void setSpeed(long speed)
    {
        mSpeed = speed;
    }

    @Override
    public void onClick(View v)
    {
        validateInput(mText);
        if (!mText.hasFocus())
        {
            mText.requestFocus();
        }

        // now perform the increment/decrement
        if (R.id.increment == v.getId())
        {
            changeCurrent(mCurrent + mStep);
        }
        else if (R.id.decrement == v.getId())
        {
            changeCurrent(mCurrent - mStep);
        }
    }

    protected String formatNumber(double value)
    {
        return (mIsDecimal) ? Utils.df.format(value) : Long.toString((long) value);
    }

    protected void changeCurrent(double current)
    {
        // Wrap around the values if we go past the start or end
        if (current > mEnd)
        {
            current = mWrap ? mStart : mEnd;
        }
        else if (current < mStart)
        {
            current = mWrap ? mEnd : mStart;
        }
        mPrevious = mCurrent;
        mCurrent = current;

        notifyChange();
        updateView();
    }

    protected void notifyChange()
    {
        if (mListener != null)
        {
            mListener.onChanged(this, mPrevious, mCurrent);
        }
    }

    protected void updateView()
    {
        int curSelectedPos = mText.getSelectionStart();
        int curTextLength = mText.getText().length();

        mText.setText(formatNumber(mCurrent));

        /* Preserve previous cursor position (right justified) */
        int selectPos = mText.getText().length();
        selectPos -= (curTextLength - Math.max(curSelectedPos, 0));
        selectPos = Math.max(selectPos, 0);

        mText.setSelection(selectPos);
    }

    private static double constrain(double x, double min, double max)
    {
        return Math.min(Math.max(x, min), max);
    }

    private void validateCurrentView(CharSequence str)
    {
        double val = getSelectedPos(str.toString());
        val = constrain(val, mStart, mEnd);
        if (mCurrent != val)
        {
            mPrevious = mCurrent;
            mCurrent = val;
            notifyChange();
        }
        updateView();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        /*
         * When focus is lost check that the text field has valid values.
         */
        if (!hasFocus)
        {
            validateInput(v);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (v == mText)
        {
            validateInput(v);
            // Don't return true, let Android handle the soft keyboard
        }
        return false;
    }

    private void validateInput(View v)
    {
        String str = String.valueOf(((TextView) v).getText());
        if ("".equals(str))
        {
            // Restore to the old value as we don't allow empty values
            updateView();
        }
        else
        {
            // Check the new value and ensure it's in range
            validateCurrentView(str);
        }
    }

    /**
     * We start the long click here but rely on the {@link NumberPickerButton}
     * to inform us when the long click has ended.
     */
    @Override
    public boolean onLongClick(View v)
    {
        /*
         * The text view may still have focus so clear it's focus which will
         * trigger the on focus changed and any typed values to be pulled.
         */
        mText.clearFocus();
        mText.requestFocus();
        if (R.id.increment == v.getId())
        {
            mIncrement = true;
            mHandler.post(mRunnable);
        }
        else if (R.id.decrement == v.getId())
        {
            mDecrement = true;
            mHandler.post(mRunnable);
        }

        return true;
    }

    public void cancelIncrement()
    {
        mIncrement = false;
    }

    public void cancelDecrement()
    {
        mDecrement = false;
    }

    //TODO decimal , ?
    private static final char[] DIGIT_CHARACTERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private static final char[] SIGNED_DIGIT_CHARACTERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '-' };

    private NumberPickerButton mIncrementButton;
    private NumberPickerButton mDecrementButton;

    private class NumberPickerInputFilter implements InputFilter
    {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
            return mNumberInputFilter.filter(source, start, end, dest, dstart, dend);
        }
    }

    private class NumberRangeKeyListener extends NumberKeyListener
    {
        // XXX This doesn't allow for range limits when controlled by a
        // soft input method!
        @Override
        public int getInputType()
        {
            //TODO decimal?
            if (isSignedNumberAllowed())
            {
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
            }
            else
            {
                return InputType.TYPE_CLASS_NUMBER;
            }
        }

        @Override
        protected char[] getAcceptedChars()
        {
            if (isSignedNumberAllowed())
                return SIGNED_DIGIT_CHARACTERS;
            else
                return DIGIT_CHARACTERS;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {

            CharSequence filtered = super.filter(source, start, end, dest, dstart, dend);
            if (filtered == null)
            {
                filtered = source.subSequence(start, end);
            }

            String result = String.valueOf(dest.subSequence(0, dstart)) + filtered
                    + dest.subSequence(dend, dest.length());

            if ("".equals(result))
            {
                return result;
            }
            if (isSignedNumberAllowed())
            {
                /*
                 * Don't allow the sign character if destination position is not
                 * front of the text
                 */
                if (filtered.length() > 0 && filtered.charAt(0) == '-')
                {
                    if (dstart != 0)
                    {
                        return "";
                    }
                }
                final int numSigneChars = countChar(result, '-');
                /* Don't allow multiple sign characters */
                if (numSigneChars > 1)
                {
                    return "";
                }
                /* Don't allow any characters before sign character */
                if (numSigneChars > 0 && result.charAt(0) != '-')
                {
                    return "";
                }
            }

            int len = result.length();
            if (len > 0 && result.charAt(0) == '-')
            {
                if (len > (mNumMaxDigitChars + 1))
                {
                    return "";
                }
            }
            else
            {
                if (len > (mNumMaxDigitChars))
                {
                    return "";
                }
            }
            return filtered;
        }

        private int countChar(String str, char c)
        {
            final int len = str.length();
            int count = 0;
            int offset = 0;
            while (offset < len)
            {
                final int index = str.indexOf(c, offset);
                if (index < 0)
                    break;
                offset += (index + 1);
                count++;
            }
            return count;
        }
    }

    private double getSelectedPos(String str)
    {
        if (str.equals("-"))
        {
            return 0;
        }
        
        try
        {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException e)
        {
            return mCurrent;
        }
    }

    /**
     * @return the current value.
     */
    public Double getCurrent()
    {
        validateInput(mText);
        return mCurrent;
    }

    /**
     * Set step
     * @param step
     */
    public void setStep(Double step)
    {
        if (step == null)
        {
            step = DEFAULT_STEP;
        }
        mStep = step;
    }

    /**
     * Set is decimal
     * @param isDecimalArg
     */
    public void setDecimal(boolean isDecimalArg)
    {
        mIsDecimal = isDecimalArg;
    }
}
