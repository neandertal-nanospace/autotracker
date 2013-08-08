package com.silentcorp.autotracker.utils;

/**
 * Event types
 * 
 * @author neandertal
 */
public enum EventType
{
    // @formatter:off
    
    FUEL_EVENT("fuel_event"),
    MAINTENANCE_EVENT("maintenance_event"), 
    REPAIR_EVENT("repair_event"),
    PAYMENT_EVENT("payment_event");

    // @formatter:on
    
    private final String value;

    private EventType(String valueArg)
    {
        value = valueArg;
    }

    @Override
    public String toString()
    {
        return value;
    }

    public static EventType parse(String value)
    {
        EventType[] v = EventType.values();
        for (EventType val : v)
        {
            if (val.toString().equals(value))
            {
                return val;
            }
        }

        return null;
    }
}
