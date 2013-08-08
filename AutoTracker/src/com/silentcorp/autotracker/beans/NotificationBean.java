package com.silentcorp.autotracker.beans;

/**
 * A notification Bean object acting as container.
 * 
 * @author neandertal
 * 
 */
public class NotificationBean
{
    private long id = -1;

    private String activity;
    private Long vehicleRef;
    private Boolean enabled = Boolean.TRUE;
    private String note;
    
    private Long periodNext;
    private String periodRepeat;
    private Integer periodAdvance = 0;
    
    private Integer distanceNext;
    private Integer distanceRepeat = 1000;
    private Integer distanceAdvance = 0;
    
    /**
     * Notifications constructor
     */
    public NotificationBean()
    {
    }

    /**
     * Get the ID, -1 for non existing in DB
     * @return
     */
    public long getId()
    {
        return id;
    }

    /**
     * Set the ID
     * @param id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Get notification activity. Can not be NULL.
     * @return
     */
    public String getActivity()
    {
        return activity;
    }

    /**
     * Set activity. Can not be NULL.
     * @param activity
     */
    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    /**
     * Get vehicle reference, NULL if notification is not linked to a vehicle.
     * @return
     */
    public Long getVehicleRef()
    {
        return vehicleRef;
    }

    /**
     * Set vehicle reference, NULL if notification is not linked to a vehicle.
     * @param vehicleRef
     */
    public void setVehicleRef(Long vehicleRef)
    {
        this.vehicleRef = vehicleRef;
    }

    /**
     * Is notification enabled. Can not be NULL.
     * @return
     */
    public Boolean getEnabled()
    {
        return enabled;
    }

    /**
     * Set is notification enabled. Can not be NULL.
     * @param enabled
     */
    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Note. Can be NULL.
     * @return
     */
    public String getNote()
    {
        return note;
    }

    /**
     * Set note. Can be NULL.
     * @param note
     */
    public void setNote(String note)
    {
        this.note = note;
    }

    /**
     * Get "next occurrence date" for period based notification. Can be NULL.
     * @return
     */
    public Long getPeriodNext()
    {
        return periodNext;
    }

    /**
     * Set "next occurrence date" for period based notification. Can be NULL.
     * @param periodLarst
     */
    public void setPeriodNext(Long periodNext)
    {
        this.periodNext = periodNext;
    }

    /**
     * Get "repeat every <period>" for period based notification. Can be NULL.
     * @return
     */
    public String getPeriodRepeat()
    {
        return periodRepeat;
    }

    /**
     * Set "repeat every <period>" for date based notification. Can be NULL.
     * @return
     */
    public void setPeriodRepeat(String periodRepeat)
    {
        this.periodRepeat = periodRepeat;
    }

    /**
     * Get "advance remainder in X days" for period based notification. Can be NULL.
     * @param dateAdvance
     */
    public Integer getPeriodAdvance()
    {
        return periodAdvance;
    }

    /**
     * Set "advance remainder in X days" for period based notification. Can be NULL.
     * @param dateAdvance
     */
    public void setPeriodAdvance(Integer periodAdvance)
    {
        this.periodAdvance = periodAdvance;
    }

    /**
     * Get "next occurrence at distance" for distance based notification. Can be NULL.
     * @param periodLarst
     */
    public Integer getDistanceNext()
    {
        return distanceNext;
    }

    /**
     * Set "next occurred at distance" for distance based notification. Can be NULL.
     * @param periodLarst
     */
    public void setDistanceNext(Integer distanceNext)
    {
        this.distanceNext = distanceNext;
    }

    /**
     * Get "repeat every X units" for distance based notification. Can be NULL.
     * @return
     */
    public Integer getDistanceRepeat()
    {
        return distanceRepeat;
    }

    /**
     * Set "repeat every X units" for distance based notification. Can be NULL.
     * @return
     */
    public void setDistanceRepeat(Integer distanceRepeat)
    {
        this.distanceRepeat = distanceRepeat;
    }

    /**
     * Get "advance remainder in X units" for distance based notification. Can be NULL.
     * @param dateAdvance
     */
    public Integer getDistanceAdvance()
    {
        return distanceAdvance;
    }

    /**
     * Set "advance remainder in X units" for distance based notification. Can be NULL.
     * @param dateAdvance
     */
    public void setDistanceAdvance(Integer distanceAdvance)
    {
        this.distanceAdvance = distanceAdvance;
    }
}
