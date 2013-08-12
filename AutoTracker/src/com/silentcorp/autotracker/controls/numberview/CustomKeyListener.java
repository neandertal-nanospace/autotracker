package com.silentcorp.autotracker.controls.numberview;

import com.silentcorp.autotracker.utils.Utils;

import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.NumberKeyListener;

/**
 * Custom filter for decimal edit text view.
 * Allows decimal point, does not allow '-' sign.
 * TODO Allows exactly 2 digits after the decimal point
 * @author neandertal
 *
 */
public class CustomKeyListener extends NumberKeyListener
{
    private char[] mAccepted;
    private boolean mDecimal;

    @Override
    protected char[] getAcceptedChars()
    {
        return mAccepted;
    }

    /**
     * The characters that are used.
     * 
     * @see KeyEvent#getMatch
     * @see #getAcceptedChars
     */
    private static final char[][] CHARACTERS = new char[][] {
            new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' },
            new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', Utils.DECIMAL_SEPARATOR }
    };

    /**
     * Allocates a DigitsKeyListener that accepts the digits 0 through 9 or decimal point (only one per
     * field) if specified. Digits after the decimal point - only 2
     */
    public CustomKeyListener(boolean decimal)
    {
        mDecimal = decimal;

        mAccepted = CHARACTERS[(decimal) ? 1: 0];
    }

    public int getInputType()
    {
        int contentType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        if (mDecimal)
        {
            contentType |= InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }
        return contentType;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
    {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);

        if (mDecimal == false)
        {
            return out;
        }

        if (out != null)
        {
            source = out;
            start = 0;
            end = out.length();
        }

        int decimal = -1;
        int dlen = dest.length();

        /*
         * Find out if the existing text has '.' characters.
         */

        for (int i = 0; i < dstart; i++)
        {
            char c = dest.charAt(i);
            if (c == '.')
            {
                decimal = i;
            }
        }
        
        for (int i = dend; i < dlen; i++)
        {
            char c = dest.charAt(i);

            if (c == '.')
            {
                decimal = i;
            }
        }

        /*
         * If it does, we must strip them out from the source. Go in reverse order so the offsets are stable.
         */

        SpannableStringBuilder stripped = null;

        for (int i = end - 1; i >= start; i--)
        {
            char c = source.charAt(i);
            boolean strip = false;

            if (c == '.')
            {
                if (decimal >= 0)
                {
                    strip = true;
                }
                else
                {
                    decimal = i;
                }
            }

            if (strip)
            {
                if (end == start + 1)
                {
                    return ""; // Only one character, and it was stripped.
                }

                if (stripped == null)
                {
                    stripped = new SpannableStringBuilder(source, start, end);
                }

                stripped.delete(i - start, i + 1 - start);
            }
        }
        
        //TODO limit only 2 digits after decimal point

        if (stripped != null)
        {
            return stripped;
        }
        else if (out != null)
        {
            return out;
        }
        else
        {
            return null;
        }
    }
}