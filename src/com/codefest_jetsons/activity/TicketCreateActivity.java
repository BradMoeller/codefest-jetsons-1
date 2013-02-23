package com.codefest_jetsons.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import com.codefest_jetsons.R;

public class TicketCreateActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener {

	private SeekBar mTimeBar;
	private TextView mHour;
	private TextView mMinute;
	private TextView mClock;
	private int mMaxtimeMillis = 7200; // maximum time in seconds the user can choose

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_creation_activity);

		mTimeBar = (SeekBar) findViewById(R.id.park_time);
		mHour = (TextView) findViewById(R.id.hour);
		mMinute = (TextView) findViewById(R.id.minute);
		mClock = (TextView) findViewById(R.id.clock);
		
		// set the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		setClockTime(calendar);

		mTimeBar.setOnSeekBarChangeListener(this);

        startActivity(new Intent(TicketCreateActivity.this, TicketInfoActivity.class));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// calculate the seconds moved
		// 100 is the max progress, by default
		int secondsMoved = (progress * mMaxtimeMillis)/100;
		
		// update the time
		int minutes =  (secondsMoved/60) % 60;
		int hours = (secondsMoved/(60 * 60)) % 24;
		mMinute.setText(String.format("%02d", minutes));
		mHour.setText(String.format("%02d", hours));
		
		// update the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis() + (secondsMoved * 1000));
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
	 * @param calendar - a Calendar object
	 */
	private void setClockTime(Calendar calendar){
		int period = calendar.get(Calendar.AM_PM);
		String am_pm = (period == Calendar.AM) ? "AM": "PM";
		int clock_hour = calendar.get(Calendar.HOUR);
		int clock_minute = calendar.get(Calendar.MINUTE);
		mClock.setText(String.format("%02d  :  %02d   %s", clock_hour, clock_minute, am_pm));
	}
}
