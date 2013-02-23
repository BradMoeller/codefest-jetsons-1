package com.codefest_jetsons.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.model.Vehicle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:47 AM
 */

public class ParkingSharedPref {
    private static final String CREDIT_CARD_PREF = "com.codefest_jetsons.creditcardpref";
    private static final String VEHICLE_PREF = "com.codefest_jetsons.creditcardpref";
    private static final String TICKET_PREF = "com.codefest_jetsons.creditcardpref";

    public static void setCreditCard(Context ctx, String userID, String fname, String lname, String number,
                                String expMonth, String expYear, String ccv, CreditCard.CreditCardType ccType) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(CREDIT_CARD_PREF, Context.MODE_PRIVATE).edit();

        Map<String, String> ccInfo = new HashMap<String, String>();
        ccInfo.put(userKeyMap(userID, CreditCard.FIRST_NAME), fname);
        ccInfo.put(userKeyMap(userID, CreditCard.LAST_NAME), lname);
        ccInfo.put(userKeyMap(userID, CreditCard.CC_NUMBER), number);
        ccInfo.put(userKeyMap(userID, CreditCard.CC_EXP_MONTH), expMonth);
        ccInfo.put(userKeyMap(userID, CreditCard.CC_EXP_YEAR), expYear);
        ccInfo.put(userKeyMap(userID, CreditCard.CC_CCV), ccv);
        ccInfo.put(userKeyMap(userID, CreditCard.CC_TYPE), ccType.name());

        putMap(spEditor, ccInfo);
        spEditor.commit();
    }

    public static CreditCard getCreditCard(Context ctx, String userID) {
        SharedPreferences sp = ctx.getSharedPreferences(CREDIT_CARD_PREF, Context.MODE_PRIVATE);

        return new CreditCard(sp.getString(userKeyMap(userID, CreditCard.FIRST_NAME), ""),
                          sp.getString(userKeyMap(userID, CreditCard.LAST_NAME), ""),
                        sp.getString(userKeyMap(userID, CreditCard.CC_NUMBER), ""),
                        sp.getString(userKeyMap(userID, CreditCard.CC_EXP_MONTH), ""),
                        sp.getString(userKeyMap(userID, CreditCard.CC_EXP_YEAR), ""),
                        sp.getString(userKeyMap(userID, CreditCard.CC_CCV), ""),
                        CreditCard.CreditCardType.getFromLabel(sp.getString(userKeyMap(userID, CreditCard.CC_TYPE), "")));
    }

    public static void setVehicle(Context ctx, String userID, String licensePlate) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(VEHICLE_PREF, Context.MODE_PRIVATE).edit();

        Map<String, String> vehicleInfo = new HashMap<String, String>();
        vehicleInfo.put(userKeyMap(userID, Vehicle.LICENSE_PLATE), licensePlate);

        putMap(spEditor, vehicleInfo);
        spEditor.commit();
    }

    public static Vehicle getVehicle(Context ctx, String userID) {
        SharedPreferences sp = ctx.getSharedPreferences(VEHICLE_PREF, Context.MODE_PRIVATE);
        return new Vehicle(sp.getString(userKeyMap(userID, Vehicle.LICENSE_PLATE), ""));
    }

    public static void setTicket(Context ctx, String userID, Date purchaseTime, int minutesPurchased, int maxMinutes) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(TICKET_PREF, Context.MODE_PRIVATE).edit();

        Map<String, String> ticketInfo = new HashMap<String, String>();
        ticketInfo.put(userKeyMap(userID, Ticket.PURCHASE_TIME), String.valueOf(purchaseTime.getTime()));
        ticketInfo.put(userKeyMap(userID, Ticket.MINUTES_PURCHASED), String.valueOf(minutesPurchased));
        ticketInfo.put(userKeyMap(userID, Ticket.MAX_MINUTES), String.valueOf(maxMinutes));

        putMap(spEditor, ticketInfo);
        spEditor.commit();
    }

    public static Ticket getTicket(Context ctx, String userID) {
        SharedPreferences sp = ctx.getSharedPreferences(TICKET_PREF, Context.MODE_PRIVATE);
        return new Ticket(new Date(Long.valueOf(sp.getString(userKeyMap(userID, Ticket.PURCHASE_TIME), "0"))),
                Integer.valueOf(sp.getString(userKeyMap(userID, Ticket.MINUTES_PURCHASED), "0")),
                Integer.valueOf(sp.getString(userKeyMap(userID, Ticket.MAX_MINUTES), "0")));
    }

    private static void putMap(SharedPreferences.Editor ed, Map<String, String> map) {
        for(String key : map.keySet()) {
            ed.putString(key, map.get(key));
        }
    }
    private static String userKeyMap(String userID, String param) {
        return userID + "-" + param;
    }
}
