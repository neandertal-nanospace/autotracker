package com.silentcorp.autotracker.beans;

import com.silentcorp.autotracker.utils.DoubleNumber;
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
    private DoubleNumber cost;
    private DoubleNumber odometer;
    private String note;

    private String fuel;
    private DoubleNumber quantity;
    private DoubleNumber price;

    private String description;
    private String place;

    /**
     * Event constructor
     */
    public EventBean()
    {
        cost = new DoubleNumber(null);
        odometer = new DoubleNumber(null);
        quantity = new DoubleNumber(null);
        price = new DoubleNumber(null);
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
     * Get cost of the event.
     */
    public DoubleNumber getCost()
    {
        return cost;
    }

    /**
     * Set cost of the event.
     */
    public void setCost(DoubleNumber cost)
    {
        this.cost = cost;
    }

    /**
     * Get odometer of the event.
     */
    public DoubleNumber getOdometer()
    {
        return odometer;
    }

    /**
     * Set odometer of the event.
     */
    public void setOdometer(DoubleNumber odometer)
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
    public DoubleNumber getQuantity()
    {
        return quantity;
    }

    /**
     * Set quantity of fuel event.
     */
    public void setQuantity(DoubleNumber quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Get price for fuel event.
     */
    public DoubleNumber getPrice()
    {
        return price;
    }

    /**
     * Set price for fuel event.
     */
    public void setPrice(DoubleNumber price)
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
