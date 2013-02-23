package com.codefest_jetsons;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import com.codefest_jetsons.activity.TicketInfoActivity;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.util.ParkingSharedPref;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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

        startActivity(new Intent(LandingActivity.this, TicketInfoActivity.class));


        /*
        Random r = new Random();
        for(int x = 0; x < 5; x++) {
            ParkingSharedPref.setCreditCard(mAppContext, "nt@gmail.com", r.nextLong(), "nick"+x, "tate"+x, "234", "02", "2013", "469", CreditCard.CreditCardType.VISA);
        }


        ArrayList<CreditCard> cards = ParkingSharedPref.getAllCreditCards(mAppContext, "nt@gmail.com");
        int x = 0;
        for(CreditCard card : cards) {
            if(x < 2) {
                ParkingSharedPref.removeCreditCardId(mAppContext, "nt@gmail.com", card.getCardId());
            }

            //Log.d(APP_TAG, card.toString());
            x++;
        }

        cards = ParkingSharedPref.getAllCreditCards(mAppContext, "nt@gmail.com");
        for(CreditCard card : cards) {
            Log.d(APP_TAG, card.toString());
        }

        */

    }
}
