package com.codefest_jetsons.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.model.Vehicle;

/**
 * User: nick49rt
 * Date: 2/23/13
 * Time: 11:47 AM
 */

public class ParkingSharedPref {
    private static final String CREDIT_CARD_PREF = "com.codefest_jetsons.creditcardpref";
    private static final String CREDIT_CARD_IDS_PREF = "com.codefest_jetsons.creditcardidspref";
    private static final String VEHICLE_PREF = "com.codefest_jetsons.vehiclepref";
    private static final String VEHICLE_IDS_PREF = "com.codefest_jetsons.vehicleidspref";
    private static final String TICKET_PREF = "com.codefest_jetsons.ticketpref";
    private static final String TICKET_IDS_PREF = "com.codefest_jetsons.ticketidspref";

    public static void setCreditCard(Context ctx, String userID, long cID, String fname, String lname, String number,
                                String expMonth, String expYear, String ccv, CreditCard.CreditCardType ccType) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(CREDIT_CARD_PREF, Context.MODE_PRIVATE).edit();

        Map<String, String> ccInfo = new HashMap<String, String>();
        String cardID = String.valueOf(cID);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.CARD_ID), cardID);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.FIRST_NAME), fname);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.LAST_NAME), lname);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.CC_NUMBER), number);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.CC_EXP_MONTH), expMonth);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.CC_EXP_YEAR), expYear);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.CC_CCV), ccv);
        ccInfo.put(userKeyMap(userID, cardID, CreditCard.CC_TYPE), ccType.name());

        putMap(spEditor, ccInfo);
        spEditor.commit();
        putCreditCardId(ctx, userID, cardID);
    }

    public static CreditCard getCreditCard(Context ctx, String userID, long cID) {
        SharedPreferences sp = ctx.getSharedPreferences(CREDIT_CARD_PREF, Context.MODE_PRIVATE);

        String cardID = String.valueOf(cID);
        return new CreditCard(Long.valueOf(sp.getString(userKeyMap(userID, cardID, CreditCard.CARD_ID), "0")),
                sp.getString(userKeyMap(userID, cardID, CreditCard.FIRST_NAME), ""),
                          sp.getString(userKeyMap(userID, cardID, CreditCard.LAST_NAME), ""),
                        sp.getString(userKeyMap(userID, cardID, CreditCard.CC_NUMBER), ""),
                        sp.getString(userKeyMap(userID, cardID, CreditCard.CC_EXP_MONTH), ""),
                        sp.getString(userKeyMap(userID, cardID, CreditCard.CC_EXP_YEAR), ""),
                        sp.getString(userKeyMap(userID, cardID, CreditCard.CC_CCV), ""),
                        CreditCard.CreditCardType.getFromLabel(sp.getString(userKeyMap(userID, cardID, CreditCard.CC_TYPE), "")));
    }

    public static ArrayList<CreditCard> getAllCreditCards(Context ctx, String userID) {
        ArrayList<CreditCard> creditCards = new ArrayList<CreditCard>();
        HashSet<String> ids = getCreditCardIds(ctx, userID);
        for (String id : ids) {
            creditCards.add(getCreditCard(ctx, userID, Long.parseLong(id)));
        }
        return creditCards;
    }

    public static void setVehicle(Context ctx, String userID, long vehicleId, String licensePlate) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(VEHICLE_PREF, Context.MODE_PRIVATE).edit();

        Map<String, String> vehicleInfo = new HashMap<String, String>();
        vehicleInfo.put(userKeyMap(userID, String.valueOf(vehicleId), Vehicle.VEHICLE_ID), String.valueOf(vehicleId));
        vehicleInfo.put(userKeyMap(userID, String.valueOf(vehicleId), Vehicle.LICENSE_PLATE), licensePlate);

        putMap(spEditor, vehicleInfo);
        spEditor.commit();
        putVehicleIds(ctx, userID, String.valueOf(vehicleId));
    }

    public static Vehicle getVehicle(Context ctx, String userID, long vehicleID) {
        SharedPreferences sp = ctx.getSharedPreferences(VEHICLE_PREF, Context.MODE_PRIVATE);
        return new Vehicle(Long.valueOf(sp.getString(userKeyMap(userID, String.valueOf(vehicleID), Vehicle.VEHICLE_ID), "0")),
                sp.getString(userKeyMap(userID, String.valueOf(vehicleID), Vehicle.LICENSE_PLATE), ""));
    }

    public static ArrayList<Vehicle> getAllVehicles(Context ctx, String userID) {
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        HashSet<String> ids = getVehicleIds(ctx, userID);
        for (String id : ids) {
            vehicles.add(getVehicle(ctx, userID, Long.parseLong(id)));
        }
        return vehicles;
    }

    public static void setTicket(Context ctx, String userID, long tID, Date purchaseTime, int minutesPurchased, int maxMinutes, double lat, double lon) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(TICKET_PREF, Context.MODE_PRIVATE).edit();

        String ticketID = String.valueOf(tID);
        Map<String, String> ticketInfo = new HashMap<String, String>();
        ticketInfo.put(userKeyMap(userID, ticketID, Ticket.TICKET_ID), ticketID);
        ticketInfo.put(userKeyMap(userID, ticketID, Ticket.PURCHASE_TIME), String.valueOf(purchaseTime.getTime()));
        ticketInfo.put(userKeyMap(userID, ticketID, Ticket.MINUTES_PURCHASED), String.valueOf(minutesPurchased));
        ticketInfo.put(userKeyMap(userID, ticketID, Ticket.MAX_MINUTES), String.valueOf(maxMinutes));

        putMap(spEditor, ticketInfo);
        spEditor.commit();
        putTicketId(ctx, userID, ticketID);
    }

    public static Ticket getTicket(Context ctx, String userID, long tID) {
        SharedPreferences sp = ctx.getSharedPreferences(TICKET_PREF, Context.MODE_PRIVATE);

        String ticketID = String.valueOf(tID);
        return new Ticket(Long.valueOf(sp.getString(userKeyMap(userID, ticketID, Ticket.TICKET_ID), "0")),
                new Date(Long.valueOf(sp.getString(userKeyMap(userID, ticketID, Ticket.PURCHASE_TIME), "0"))),
                Integer.valueOf(sp.getString(userKeyMap(userID, ticketID, Ticket.MINUTES_PURCHASED), "0")),
                Integer.valueOf(sp.getString(userKeyMap(userID, ticketID, Ticket.MAX_MINUTES), "0")),
                Double.valueOf(sp.getString(userKeyMap(userID, ticketID, Ticket.LATITUDE), "0")),
                Double.valueOf(sp.getString(userKeyMap(userID, ticketID, Ticket.LONGTIUDE), "0")));
    }

    public static ArrayList<Ticket> getAllTickets(Context ctx, String userID) {
        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        HashSet<String> ids = getTicketIds(ctx, userID);
        for (String id : ids) {
            tickets.add(getTicket(ctx, userID, Long.parseLong(id)));
        }
        return tickets;
    }

    private static void putMap(SharedPreferences.Editor ed, Map<String, String> map) {
        for(String key : map.keySet()) {
            ed.putString(key, map.get(key));
        }
    }

    private static String userKeyMap(String userID, String id, String param) {
        return userID + "-" + id + "-" + param;
    }

    private static void putCreditCardId(Context ctx, String userID, String id) {
        HashSet<String> ids = getCreditCardIds(ctx, userID);
        ids.add(id);
        ctx.getSharedPreferences(CREDIT_CARD_IDS_PREF, Context.MODE_PRIVATE).edit().putStringSet(userID,  ids).commit();
    }

    public static void removeCreditCardId(Context ctx, String userID, long id) {
        HashSet<String> ids = getCreditCardIds(ctx, userID);
        ids.remove(String.valueOf(id));
        ctx.getSharedPreferences(CREDIT_CARD_IDS_PREF, Context.MODE_PRIVATE).edit().putStringSet(userID,  ids).commit();
    }

    private static void putTicketId(Context ctx, String userID, String id) {
        HashSet<String> ids = getTicketIds(ctx, userID);
        ids.add(id);
        ctx.getSharedPreferences(TICKET_IDS_PREF, Context.MODE_PRIVATE).edit().putStringSet(userID,  ids).commit();
    }

    public static void removeTicketId(Context ctx, String userID, long id) {
        HashSet<String> ids = getTicketIds(ctx, userID);
        ids.remove(String.valueOf(id));
        ctx.getSharedPreferences(TICKET_IDS_PREF, Context.MODE_PRIVATE).edit().putStringSet(userID,  ids).commit();
    }

    private static void putVehicleIds(Context ctx, String userID, String id) {
        HashSet<String> ids = getVehicleIds(ctx, userID);
        ids.add(id);
        ctx.getSharedPreferences(VEHICLE_IDS_PREF, Context.MODE_PRIVATE).edit().putStringSet(userID,  ids).commit();
    }

    public static void removeVehicleId(Context ctx, String userID, long id) {
        HashSet<String> ids = getVehicleIds(ctx, userID);
        ids.remove(String.valueOf(id));
        ctx.getSharedPreferences(VEHICLE_IDS_PREF, Context.MODE_PRIVATE).edit().putStringSet(userID,  ids).commit();
    }

    private static HashSet<String> getCreditCardIds(Context ctx, String userID) {
        return (HashSet<String>) ctx.getSharedPreferences(CREDIT_CARD_IDS_PREF, Context.MODE_PRIVATE).getStringSet(userID, new HashSet<String>());
    }
    private static HashSet<String> getTicketIds(Context ctx, String userID) {
        return (HashSet<String>) ctx.getSharedPreferences(TICKET_IDS_PREF, Context.MODE_PRIVATE).getStringSet(userID, new HashSet<String>());
    }
    private static HashSet<String> getVehicleIds(Context ctx, String userID) {
        return (HashSet<String>) ctx.getSharedPreferences(VEHICLE_IDS_PREF, Context.MODE_PRIVATE).getStringSet(userID, new HashSet<String>());
    }
}
