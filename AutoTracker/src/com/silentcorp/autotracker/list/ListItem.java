package com.silentcorp.autotracker.list;

/**
 * List Item - holds data for the visible list item
 * 
 * @author neandertal
 * 
 */
public class ListItem
{
    private boolean isChecked;
    private Object icon;
    private String header;
    private String description;
    //If value is NULL, not displayed
    private String value;

    public ListItem()
    {
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public Object getIcon()
    {
        return icon;
    }

    public void setIcon(Object icon)
    {
        this.icon = icon;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ListItem[");
        sb.append("checked=");
        sb.append(isChecked);
        sb.append(", icon=");
        sb.append(icon.toString());
        sb.append(", header=");
        sb.append(header);
        sb.append(", description=");
        sb.append(description);
        sb.append(", value=");
        sb.append(value);
        return sb.toString();
    }
}
