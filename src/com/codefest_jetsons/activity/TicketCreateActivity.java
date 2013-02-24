package com.codefest_jetsons.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codefest_jetsons.LicensePlateAdapter;
import com.codefest_jetsons.R;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.CreditCard.CreditCardType;
import com.codefest_jetsons.util.ParkingSharedPref;

public class TicketCreateActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener {

	private SeekBar mTimeBar;
	private TextView mHour;
	private TextView mMinute;
	private TextView mClock;
	private PagerAdapter mPagerAdapter;
	private Button mPay;
	private Context mAppContext;
	
	private final int mMaxtimeMillis = 7200; // maximum time in seconds the user can choose

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAppContext = getApplicationContext();
		setContentView(R.layout.ticket_creation_activity);

		mTimeBar = (SeekBar) findViewById(R.id.park_time);
		mHour = (TextView) findViewById(R.id.hour);
		mMinute = (TextView) findViewById(R.id.minute);
		mClock = (TextView) findViewById(R.id.clock);
		mPay = (Button) findViewById(R.id.pay);

		// set the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		setClockTime(calendar);

		mTimeBar.setOnSeekBarChangeListener(this);

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
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		myPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		// A little space between pages
		myPager.setPageMargin(15);

		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		myPager.setClipChildren(false);

        startActivity(new Intent(TicketCreateActivity.this, TicketInfoActivity.class));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// calculate the seconds moved
		// 100 is the max progress, by default
		int secondsMoved = (progress * mMaxtimeMillis) / 100;

		// update the time
		int minutes = (secondsMoved / 60) % 60;
		int hours = (secondsMoved / (60 * 60)) % 24;
		mMinute.setText(String.format("%02d", minutes));
		mHour.setText(String.format("%02d", hours));

		// update the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis()
				+ (secondsMoved * 1000));
		setClockTime(calendar);

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// do nothing
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// do nothing
	}

	/**
	 * 
	 * @param calendar
	 *            - a Calendar object
	 */
	private void setClockTime(Calendar calendar) {
		int period = calendar.get(Calendar.AM_PM);
		String am_pm = (period == Calendar.AM) ? "AM" : "PM";
		int clock_hour = calendar.get(Calendar.HOUR);
		int clock_minute = calendar.get(Calendar.MINUTE);
		mClock.setText(String.format("%02d:%02d %s", clock_hour, clock_minute,
				am_pm));
	}
}
