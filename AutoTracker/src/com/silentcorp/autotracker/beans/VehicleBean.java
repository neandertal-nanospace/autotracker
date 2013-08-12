package com.silentcorp.autotracker.beans;

import com.silentcorp.autotracker.utils.DoubleNumber;

/**
 * A vehicle Bean object acting as container.
 * 
 * @author neandertal
 * 
 */
public class VehicleBean
{
    private long id = -1;

    private String name;
    private Integer color;

    private String make;
    private String model;
    private DoubleNumber year;
    private String licensePlate;

    private String primaryFuel;
    private String secondaryFuel;

    private Long purchaseDate;
    private DoubleNumber purchasePrice;
    private DoubleNumber purchaseOdometer;
    private String purchaseNote;

    private Boolean isSold = Boolean.FALSE;
    private Long sellDate;
    private DoubleNumber sellPrice;
    private DoubleNumber sellOdometer;
    private String sellNote;

    /**
     * Constructor
     */
    public VehicleBean()
    {
        year = new DoubleNumber(null);
        purchasePrice = new DoubleNumber(null);
        purchaseOdometer = new DoubleNumber(null);
        sellPrice = new DoubleNumber(null);
        sellOdometer = new DoubleNumber(null);
    }

    /**
     * get vehicle record ID in DB
     * 
     * @return
     */
    public long getId()
    {
        return id;
    }

    /**
     * Set vehicle record ID in DB
     * 
     * @param id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Get vehicle name. Can not be null.
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set vehicle name. Can not be null.
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get vehicle color. Can not be null.
     * 
     * @return
     */
    public Integer getColor()
    {
        return color;
    }

    /**
     * Set vehicle color. Can not be null.
     * 
     * @param color
     */
    public void setColor(Integer color)
    {
        this.color = color;
    }

    /**
     * Get vehicle make. Can be null.
     * 
     * @return
     */
    public String getMake()
    {
        return make;
    }

    /**
     * Set vehicle make. Can be null.
     * 
     * @param make
     */
    public void setMake(String make)
    {
        this.make = make;
    }

    /**
     * Get vehicle model. Can be null.
     * 
     * @return
     */
    public String getModel()
    {
        return model;
    }

    /**
     * Set vehicle model. Can be null.
     * 
     * @param model
     */
    public void setModel(String model)
    {
        this.model = model;
    }

    /**
     * Get vehicle year.
     * 
     * @return
     */
    public DoubleNumber getYear()
    {
        return year;
    }

    /**
     * Set vehicle year.
     * 
     * @param year
     */
    public void setYear(DoubleNumber year)
    {
        this.year = year;
    }

    /**
     * Get license plate, can be null.
     * 
     * @return
     */
    public String getLicensePlate()
    {
        return licensePlate;
    }

    /**
     * Set license plate. Can be null;
     * 
     * @param licensePlate
     */
    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }

    /**
     * Get vehicle primary fuel. Can not be null.
     * 
     * @return
     */
    public String getPrimaryFuel()
    {
        return primaryFuel;
    }

    /**
     * Set vehicle primary fuel. Can not be null;
     * 
     * @param primaryFuel
     */
    public void setPrimaryFuel(String primaryFuel)
    {
        this.primaryFuel = primaryFuel;
    }

    /**
     * Get secondary fuel. Can be null.
     * 
     * @return
     */
    public String getSecondaryFuel()
    {
        return secondaryFuel;
    }

    /**
     * Set vehicle secondary fuel. Can be null;
     * 
     * @param secondaryFuel
     */
    public void setSecondaryFuel(String secondaryFuel)
    {
        this.secondaryFuel = secondaryFuel;
    }

    /**
     * Get purchase date in millis. Can be null
     * 
     * @return
     */
    public Long getPurchaseDate()
    {
        return purchaseDate;
    }

    /**
     * Set purchase date in millis. Can be null.
     * 
     * @param purchaseDate
     */
    public void setPurchaseDate(Long purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Get purchase price is currency units.
     * 
     * @return
     */
    public DoubleNumber getPurchasePrice()
    {
        return purchasePrice;
    }

    /**
     * Set purchase price in currency units.
     * 
     * @param purchasePrice
     */
    public void setPurchasePrice(DoubleNumber purchasePrice)
    {
        this.purchasePrice = purchasePrice;
    }

    /**
     * Get purchase mileage in distance units.
     * 
     * @return
     */
    public DoubleNumber getPurchaseOdometer()
    {
        return purchaseOdometer;
    }

    /**
     * Set purchase mileage in distance units.
     * 
     * @param purchaseOdometer
     */
    public void setPurchaseOdometer(DoubleNumber purchaseOdometer)
    {
        this.purchaseOdometer = purchaseOdometer;
    }

    /**
     * Get purchase note. Can be null.
     * 
     * @return
     */
    public String getPurchaseNote()
    {
        return purchaseNote;
    }

    /**
     * Set purchase note, can be null.
     * 
     * @param purchaseNote
     */
    public void setPurchaseNote(String purchaseNote)
    {
        this.purchaseNote = purchaseNote;
    }

    /**
     * Get sell date in millis. Can be null.
     * 
     * @return
     */
    public Long getSellDate()
    {
        return sellDate;
    }

    /**
     * Set sell date in millis. Can be null.
     * 
     * @param sellDate
     */
    public void setSellDate(Long sellDate)
    {
        this.sellDate = sellDate;
    }

    /**
     * Get sell price in currency units.
     * 
     * @return
     */
    public DoubleNumber getSellPrice()
    {
        return sellPrice;
    }

    /**
     * Set sell price in currency units.
     * 
     * @param sellPrice
     */
    public void setSellPrice(DoubleNumber sellPrice)
    {
        this.sellPrice = sellPrice;
    }

    /**
     * Get the sell mileage in distance units.
     * 
     * @return
     */
    public DoubleNumber getSellOdometer()
    {
        return sellOdometer;
    }

    /**
     * Set sell mileage in distance units.
     * 
     * @param sellOdometer
     */
    public void setSellOdometer(DoubleNumber sellOdometer)
    {
        this.sellOdometer = sellOdometer;
    }

    /**
     * Get sell note. Can be null.
     * 
     * @return
     */
    public String getSellNote()
    {
        return sellNote;
    }

    /**
     * Set sell note. Can be null.
     * 
     * @param sellNote
     */
    public void setSellNote(String sellNote)
    {
        this.sellNote = sellNote;
    }

    public Boolean getIsSold()
    {
        return isSold;
    }

    public void setIsSold(Boolean isSold)
    {
        this.isSold = isSold;
    }
}
