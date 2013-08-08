package com.silentcorp.autotracker.beans;

import com.silentcorp.autotracker.utils.EventType;


/**
 * An event bean object acting as container.
 * 
 * @author neandertal
 * 
 */
public class EventBean
{
    private long id = -1;

    private Long vehicleRef;
    private Long eventDate;
    private EventType eventType;
    private Double cost;
    private Integer odometer;
    private String note;

    private String fuel;
    private Double quantity;
    private Double price;

    private String description;
    private String place;

    /**
     * Event constructor
     */
    public EventBean()
    {
    }

    /**
     * Get the ID, -1 for non existing in DB
     * 
     * @return
     */
    public long getId()
    {
        return id;
    }

    /**
     * Set the ID, -1 for non existing in DB
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Get vehicle reference, NULL if event is not linked to a vehicle.
     */
    public Long getVehicleRef()
    {
        return vehicleRef;
    }

    /**
     * Set vehicle reference, NULL if event is not linked to a vehicle.
     */
    public void setVehicleRef(Long vehicleRef)
    {
        this.vehicleRef = vehicleRef;
    }

    /**
     * Get Date of the event. Can not be NULL.
     */
    public Long getEventDate()
    {
        return eventDate;
    }

    /**
     * Set Date of the event. Can not be NULL.
     */
    public void setEventDate(Long eventDate)
    {
        this.eventDate = eventDate;
    }

    /**
     * Get type of the event. Can not be NULL.
     */
    public EventType getEventType()
    {
        return eventType;
    }

    /**
     * Set type of the event. Can not be NULL.
     */
    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
    }

    /**
     * Get cost of the event. Can not be NULL.
     */
    public Double getCost()
    {
        return cost;
    }

    /**
     * Set cost of the event. Can not be NULL.
     */
    public void setCost(Double cost)
    {
        this.cost = cost;
    }

    /**
     * Get odometer of the event. Can be NULL.
     */
    public Integer getOdometer()
    {
        return odometer;
    }

    /**
     * Set odometer of the event. Can be NULL.
     */
    public void setOdometer(Integer odometer)
    {
        this.odometer = odometer;
    }

    /**
     * Note. Can be NULL.
     */
    public String getNote()
    {
        return note;
    }

    /**
     * Note. Can be NULL.
     */
    public void setNote(String note)
    {
        this.note = note;
    }

    /**
     * Get fuel type. Can be NULL.
     */
    public String getFuel()
    {
        return fuel;
    }

    /**
     * Set fuel type. Can be NULL.
     */
    public void setFuel(String fuel)
    {
        this.fuel = fuel;
    }

    /**
     * Get quantity of fuel event. Can be NULL.
     */
    public Double getQuantity()
    {
        return quantity;
    }

    /**
     * Set quantity of fuel event. Can be NULL.
     */
    public void setQuantity(Double quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Get price for fuel event. Can be NULL.
     */
    public Double getPrice()
    {
        return price;
    }

    /**
     * Set price for fuel event. Can be NULL.
     */
    public void setPrice(Double price)
    {
        this.price = price;
    }

    /**
     * Get description for non fuel event. Can be NULL.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set description for non fuel event. Can be NULL.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get place for non payment event. Can be NULL.
     */
    public String getPlace()
    {
        return place;
    }

    /**
     * Set place for non payment event. Can be NULL.
     */
    public void setPlace(String place)
    {
        this.place = place;
    }
}
