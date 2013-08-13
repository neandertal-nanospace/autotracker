package com.silentcorp.autotracker.utils;

/**
 * DoubleNumber representation. Contains whole part and fraction. Fraction is
 * from 0 to 100.
 * 
 * @author neandertal
 */
public class DoubleNumber
{
    int whole = 0;
    int fraction = 0;
    boolean isNull = false;

    public DoubleNumber()
    {}

    public DoubleNumber(Integer source)
    {
        if (source == null)
        {
            isNull = true;
        }
        else
        {
            whole = source.intValue() / 100;
            fraction = source.intValue() % 100;
        }
    }

    public DoubleNumber(int wholeArg, int fragmentArg, boolean isNullArg)
    {
        isNull = isNullArg;
        whole = wholeArg;
        fraction = fragmentArg;
    }

    public int getWhole()
    {
        return whole;
    }

    public int getFraction()
    {
        return fraction;
    }

    public boolean isNull()
    {
        return isNull;
    }

    public Integer getDoubleInt()
    {
        if (isNull)
        {
            return null;
        }

        return Integer.valueOf(whole * 100 + fraction);
    }
    
    public Integer getWholeInt()
    {
        if (isNull)
        {
            return null;
        }

        return Integer.valueOf(whole);
    }
}
