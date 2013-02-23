package com.codefest_jetsons.model;


import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Ticket {
    public static final String PURCHASE_TIME = "ticket.purchasetime";
    public static final String MINUTES_PURCHASED = "ticket.minpurchased";
    public static final String MAX_MINUTES = "ticket.maxminutes";

    private Date purchaseTime;
    private int minutesPurchased;
    private int maxMinutes;

    public Ticket(Date purchaseTime, int minutesPurchased, int maxMinutes) {
        this.purchaseTime = purchaseTime;
        this.minutesPurchased = minutesPurchased;
        this.maxMinutes = maxMinutes;
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

    public void setMaxMinutes(int maxMinutes) {
        this.maxMinutes = maxMinutes;
    }
}
