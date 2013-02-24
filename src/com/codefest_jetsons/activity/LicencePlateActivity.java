package com.codefest_jetsons.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.codefest_jetsons.LicensePlateAdapter;
import com.codefest_jetsons.R;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.CreditCard.CreditCardType;
import com.codefest_jetsons.util.ParkingSharedPref;

public class LicencePlateActivity extends Activity {
    private Context mAppContext;
	
	private PagerAdapter mPagerAdapter;
	
	/**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.license_plate_activity);
        
        // Make some mock data for now
        List<CreditCard> ccs = new ArrayList<CreditCard>();
        int y = 2013;
        Random r = new Random();
        for(int i = 0; i < 4; i++) {
        	ParkingSharedPref.setCreditCard(mAppContext, "frank@gmail.com", r.nextInt(Integer.MAX_VALUE), "Frank", "Fitchard", "111122223333",
                    "July", ++y + "", "492", CreditCardType.MASTER_CARD);
            for(CreditCard cc : ParkingSharedPref.getAllCreditCards(mAppContext, "frank@gmail.com")) {
                ccs.add(cc);
            }
        }
        
        mPagerAdapter = new LicensePlateAdapter(ccs, getApplicationContext());
        ViewPager myPager = (ViewPager) findViewById(R.id.viewPager);
        myPager.setAdapter(mPagerAdapter);
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        myPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        //A little space between pages
        myPager.setPageMargin(15);
	      
        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        myPager.setClipChildren(false);
    }
}
