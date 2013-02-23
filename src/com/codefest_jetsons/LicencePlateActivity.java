package com.codefest_jetsons;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.CreditCard.CreditCardType;

public class LicencePlateActivity extends Activity {
	
	private PagerAdapter mPagerAdapter;
	
	/**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_plate_activity);
        
        // Make some mock data for now
        List<CreditCard> ccs = new ArrayList<CreditCard>();
        int y = 2013;
        for(int i = 0; i < 4; i++) {
        	CreditCard cc = new CreditCard("Frank", "Fitchard", "111122223333", 
        			"July", ++y + "", "492", CreditCardType.MASTER_CARD);
        	ccs.add(cc);
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
