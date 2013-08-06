package com.silentcorp.autotracker.controls.spinneradapter;

/**
 * Label - value relation class.
 * @author neandertal
 */
public class LabelValuePair
{
    private String label;
    private String value;

    public LabelValuePair(String labelArg, String valueArg)
    {
        label = labelArg;
        value = valueArg;
    }
    
    public String getLabel()
    {
        return label;
    }
    
    public String getValue()
    {
        return value;
    }
    
    @Override
    public String toString()
    {
        return label;
    }
}
