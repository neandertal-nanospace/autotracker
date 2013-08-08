package com.silentcorp.autotracker.controls.spinneradapter;

/**
 * Interface for Label-Value spinner adapters
 * @author neandertal
 */
public interface ISpinnerAdapter
{
    /**
     * Get position in array for pair with this value
     * 
     * @param value
     * @return position of LabelvaluePair, when the values match, or -1 if not
     *         found
     */
    public int getPosition(String value);
}
