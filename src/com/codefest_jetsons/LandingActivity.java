package com.codefest_jetsons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    }
}
