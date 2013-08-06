package com.silentcorp.autotracker.controls.spinneradapter;

/**
 * Name - ID pair.
 * @author neandertal
 */
public class NameIdPair
{
    private String name;
    private Long id;

    public NameIdPair(String nameArg, Long idArg)
    {
        name = nameArg;
        id = idArg;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Long getID()
    {
        return id;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
