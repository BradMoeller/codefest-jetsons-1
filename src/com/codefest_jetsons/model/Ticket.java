package com.codefest_jetsons.model;


import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Ticket {
    public static final String TICKET_ID = "ticket.ticketid";
    public static final String PURCHASE_TIME = "ticket.purchasetime";
    public static final String MINUTES_PURCHASED = "ticket.minpurchased";
    public static final String MAX_MINUTES = "ticket.maxminutes";
    public static final String LATITUDE = "ticket.latitude";
    public static final String LONGTIUDE = "ticket.longitude";

    private long ticketId;
    private Date purchaseTime;
    private Date endTime;
    private int minutesPurchased;
    private int maxMinutes;
    private double latitude;
    private double longitude;

    public Ticket(long ticketId, Date purchaseTime, int minutesPurchased, int maxMinutes, double lat, double lon) {
        this.ticketId = ticketId;
        this.purchaseTime = purchaseTime;
        this.minutesPurchased = minutesPurchased;
        this.maxMinutes = maxMinutes;
        latitude = lat;
        longitude = lon;

        Calendar cal = Calendar.getInstance();
        cal.setTime(purchaseTime);
        cal.add(Calendar.MINUTE, minutesPurchased);
        this.endTime = cal.getTime();
    }

    public boolean isExpired() {
       return (endTime.getTime() < new Date().getTime());
    }

    public long getMillisecondsLeft() {
        return endTime.getTime() - new Date().getTime();
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public int getMinutesPurchased() {
        return minutesPurchased;
    }

    public void setMinutesPurchased(int minutesPurchased) {
        this.minutesPurchased = minutesPurchased;
    }

    public int getMaxMinutes() {
        return maxMinutes;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public double getLatitude() {
        return latitude;
    }

    public void setMaxMinutes(int maxMinutes) {
        this.maxMinutes = maxMinutes;
    }

    @Override
    public String toString() {
        return "TICKET" + "\n" + "================" + "\n" +
                "Ticket id: " + ticketId + "\n" +
                "Purchase Time: " + purchaseTime.toString() + "\n" +
                "Minutes Purchased: " + maxMinutes + "\n" +
                "Max Minutes: " + maxMinutes;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
