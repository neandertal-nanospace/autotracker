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
    
    private Long dateDue;
    private Integer dateRepeat;
    private Integer dateAdvance;

    private Long periodLast;
    private Integer periodRepeat;
    private Integer periodAdvance;
    
    private Integer distanceLast;
    private Integer distanceRepeat;
    private Integer distanceAdvance;
    
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
     * Notification due date, if is date based. Can be NULL.
     * @return
     */
    public Long getDateDue()
    {
        return dateDue;
    }

    /**
     * Set notification due date, if is date based. Can be NULL.
     * @param dateDue
     */
    public void setDateDue(Long dateDue)
    {
        this.dateDue = dateDue;
    }

    /**
     * Get "repeat every X days" for date based notification. Can be NULL.
     * @return
     */
    public Integer getDateRepeat()
    {
        return dateRepeat;
    }

    /**
     * Set "repeat every X days" for date based notification. Can be NULL.
     * @param dateRepeat
     */
    public void setDateRepeat(Integer dateRepeat)
    {
        this.dateRepeat = dateRepeat;
    }

    /**
     * Get "advance remainder in X days" for date based notification. Can be NULL.
     * @return
     */
    public Integer getDateAdvance()
    {
        return dateAdvance;
    }

    /**
     * Set "advance remainder in X days" for date based notification. Can be NULL.
     * @param dateAdvance
     */
    public void setDateAdvance(Integer dateAdvance)
    {
        this.dateAdvance = dateAdvance;
    }

    /**
     * Get "last occurred date" for period based notification. Can be NULL.
     * @return
     */
    public Long getPeriodLast()
    {
        return periodLast;
    }

    /**
     * Set "last occurred date" for period based notification. Can be NULL.
     * @param periodLarst
     */
    public void setPeriodLast(Long periodLast)
    {
        this.periodLast = periodLast;
    }

    /**
     * Get "repeat every X days" for period based notification. Can be NULL.
     * @return
     */
    public Integer getPeriodRepeat()
    {
        return periodRepeat;
    }

    /**
     * Set "repeat every X days" for date based notification. Can be NULL.
     * @return
     */
    public void setPeriodRepeat(Integer periodRepeat)
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
     * Get "last occurred at distance" for distance based notification. Can be NULL.
     * @param periodLarst
     */
    public Integer getDistanceLast()
    {
        return distanceLast;
    }

    /**
     * Set "last occurred at distance" for distance based notification. Can be NULL.
     * @param periodLarst
     */
    public void setDistanceLast(Integer distanceLast)
    {
        this.distanceLast = distanceLast;
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
