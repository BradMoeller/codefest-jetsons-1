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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codefest_jetsons.LicensePlateAdapter;
import com.codefest_jetsons.LicensePlateAdapterInterface;
import com.codefest_jetsons.R;
import com.codefest_jetsons.model.Vehicle;
import com.codefest_jetsons.util.ParkingSharedPref;

public class TicketCreateActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener, LicensePlateAdapterInterface, OnClickListener {

	private SeekBar mTimeBar;
	private TextView mHour;
	private TextView mMinute;
	private TextView mClock;
	private PagerAdapter mPagerAdapter;
	private Button mPay;
	private Context mAppContext;
	private ViewPager myPager;
	private Animation mDownAnimation;
	private Animation mUpAnimation;
	private ImageButton mSliderLayout;
	
	private final int SNAP_DELTA_MINUTES = 15;
	private final int mMaxtimeSeconds = 7200; // maximum time in seconds the user can choose
	private final double COST_PER_MINUTE = 0.01666666666666;
	private final String USER_ID = "0";

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
		mSliderLayout = (ImageButton) findViewById(R.id.sliderLayout);
		
		mSliderLayout.setOnClickListener(this);
		
		// set the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		setClockTime(calendar);

		mTimeBar.setOnSeekBarChangeListener(this);
		mTimeBar.setMax((mMaxtimeSeconds/60)/SNAP_DELTA_MINUTES);
		
		//mTimeBar.setMax(max)
		 // Make some mock data for now
        List<Vehicle> vhs = new ArrayList<Vehicle>();
        int y = 2013;
        Random r = new Random();
        if (ParkingSharedPref.getAllVehicles(mAppContext, USER_ID).size() == 0) {
        	// Create some fake data...
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 1, "ABC1234");
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 2, "FJT2651");
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 3, "EGG1337");
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 4, "LOL4592");
        }
        
        for(Vehicle v : ParkingSharedPref.getAllVehicles(mAppContext, USER_ID)) {
        	vhs.add(v);
        }
        
		mPagerAdapter = new LicensePlateAdapter(vhs, getApplicationContext(), this);
		myPager = (ViewPager) findViewById(R.id.viewPager);
		myPager.setAdapter(mPagerAdapter);
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		myPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		// A little space between pages
		myPager.setPageMargin(15);
		//myPager.setOnPageChangeListener(this);
		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		myPager.setClipChildren(false);
		
		// Animation stuff
		AnimationSet set = new AnimationSet(true);
		// Slide down animation
		mDownAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, //fromXType 
                0.0f,                       //fromXValue
                Animation.RELATIVE_TO_SELF, //toXType
                0.0f,                      //toXValue
                Animation.RELATIVE_TO_SELF, //fromYType
                -1.0f,                       //fromYValue
                Animation.RELATIVE_TO_SELF, //toYType
                0.0f);                      //toYValue
		mDownAnimation.setDuration(500);
		mDownAnimation.setFillAfter(true);
        set.addAnimation(mDownAnimation);
        // Slide up animation
        mUpAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, //fromXType 
                0.0f,                       //fromXValue
                Animation.RELATIVE_TO_SELF, //toXType
                0.0f,                      //toXValue
                Animation.RELATIVE_TO_SELF, //fromYType
                0.0f,                       //fromYValue
                Animation.RELATIVE_TO_SELF, //toYType
                -1.0f);                      //toYValue
        mUpAnimation.setDuration(500);
        mUpAnimation.setFillAfter(true);
		set.addAnimation(mUpAnimation);
		
        //AnimationController controller = new LayoutAnimationController(set, 0.25f);
        
        getWindow().setSoftInputMode(
        	    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		progress *= SNAP_DELTA_MINUTES;
		// calculate the seconds moved
		// 100 is the max progress, by default
		int secondsMoved = progress * 60;//(progress * mMaxtimeSeconds) / 100;

		// update the time
		int minutes = (secondsMoved / 60) % 60;
		int hours = (secondsMoved / (60 * 60)) % 24;
		mMinute.setText(String.format("%02d", minutes));
		mHour.setText(String.format("%02d", hours));
		
		// Update the cost
		double cost = COST_PER_MINUTE * progress;
		String costString = java.text.NumberFormat.getCurrencyInstance().format(cost);
		mPay.setText(getResources().getString(R.string.pay) + " (" +costString+")");

		// update the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis()
				+ (secondsMoved * 1000));
		setClockTime(calendar);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
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

	@Override
	public void enteredCreatePlate() {
		mSliderLayout.startAnimation(mDownAnimation);	
	}

	@Override
	public void exitedCreatePlate() {
		mSliderLayout.startAnimation(mUpAnimation);	
	}

	@Override
	public void onClick(View arg0) {
		// TODO NICK 
	}

}
