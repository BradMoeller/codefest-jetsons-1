package com.codefest_jetsons;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.util.ParkingSharedPref;

import java.util.Date;

public class LandingActivity extends Activity {
    private static final String APP_TAG = "com.codefest_jetsons";
    private Context mAppContext;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.main);

        ParkingSharedPref.setCreditCard(mAppContext, "nt@gmail.com", "nick", "tate", "234", "02", "2013", "469", CreditCard.CreditCardType.VISA);
        CreditCard cc = ParkingSharedPref.getCreditCard(mAppContext, "nt@gmail.com");

        ParkingSharedPref.setTicket(mAppContext, "nt@gmail.com", new Date(), 20, 60);
        Ticket tt = ParkingSharedPref.getTicket(mAppContext, "nt@gmail.com");

        Log.d(APP_TAG, cc.toString());
        Log.d(APP_TAG, tt.toString());
    }
}
