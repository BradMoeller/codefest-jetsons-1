package com.codefest_jetsons.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.codefest_jetsons.R;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.service.TicketService;
import com.codefest_jetsons.util.ParkingNotifications;
import com.codefest_jetsons.util.ParkingSharedPref;

/**
 * Created with IntelliJ IDEA. User: nick49rt Date: 2/23/13 Time: 2:59 PM To
 * change this template use File | Settings | File Templates.
 */
public class TicketInfoActivity extends Activity {
    private Context mAppContext;

    private long ticketTimer;
    private static final int SECOND = 1000;

    private TextSwitcher rHours;
    private TextSwitcher rMin;
    private TextSwitcher rSec;
    private int lastS;
    private int lastM;
    private int lastH;
    private CountDownTimer countDownTimer;
    private Ticket t;
    private Button mAddTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.ticket_info_activity);
        setListeners();

        Random r = new Random();
        long id = r.nextInt(Integer.MAX_VALUE);
        ParkingSharedPref.setTicket(mAppContext, "ntate@gmail.com", 50, new Date(), 11, 60);
        t = ParkingSharedPref.getTicket(mAppContext, "ntate@gmail.com", 50);

        ticketTimer = t.getMillisecondsLeft();

        //ParkingNotifications.startNotifications(mAppContext, ticketTimer);

        final TextView endTime = (TextView) findViewById(R.id.expiration_time);
        SimpleDateFormat s = new SimpleDateFormat("h:m a");
        endTime.setText(s.format(t.getEndTime()));
        loadSwitchers();

        lastH = getRemainingHours(ticketTimer);
        rHours.setCurrentText(lastH + "");
        lastM = getRemainingMinutes(ticketTimer);
        rMin.setCurrentText(lastM + "");
        lastS = getRemainingSeconds(ticketTimer);
        rSec.setCurrentText(lastS + "");

        countDownTimer = new CountDownTimer(ticketTimer, SECOND) {
            public void onTick(long millisUntilFinished) {
                if(getRemainingHours(millisUntilFinished) < lastH) {
                    lastH = getRemainingHours(millisUntilFinished);
                    rHours.setText(lastH+"");
                }

                if(getRemainingMinutes(millisUntilFinished) < lastM || lastM == 0) {
                    lastM = getRemainingMinutes(millisUntilFinished);

                    if(lastM < 10) {
                        rMin.setText("0"+lastM);
                    }
                    else {
                        rMin.setText(lastM+"");
                    }
                }

                if(getRemainingSeconds(millisUntilFinished) < lastS || lastS == 0) {
                    lastS = getRemainingSeconds(millisUntilFinished);

                    if(lastS < 10) {
                        rSec.setText("0"+lastS);
                    }
                    else {
                        rSec.setText(lastS+"");
                    }
                }
            }

            public void onFinish() {
                //.setText("done!");
                rMin.setText("DONE");
            }
        }.start();

        /*
        ParkingSharedPref.setCreditCard(mAppContext, "ntate@gmail.com", id,
                "Nick", "Tate", "4300112233445566", "09", "2014", "554", CreditCard.CreditCardType.MASTER_CARD);
        CreditCard cc = ParkingSharedPref.getCreditCard(mAppContext, "ntate@gmail.com", id);
        */
        
        mAddTime = (Button) findViewById(R.id.add_time);
        mAddTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyDialogFragment df = new MyDialogFragment();
				df.show(getFragmentManager(), null);
			}
		});
    }

    private void setListeners() {
        Button validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TicketInfoActivity.this, ValidateActivity.class));
            }
        });

    }

    private int getRemainingHours(long milli) {
        int hours = (int) ((milli/1000)/60/60);
        return hours;
    }

    private int getRemainingMinutes(long milli) {
        int hours = (int) ((milli/1000)/60/60);
        int minutes = (int) ((milli/1000)/60)-(hours*60);
        return minutes;
    }

    private int getRemainingSeconds(long milli) {
        int hours = (int) ((milli/1000)/60/60);
        int minutes = (int) ((milli/1000)/60)-(hours*60);
        int seconds = (int) ((milli/1000))-(minutes*60)-(hours*60*60);
        return seconds;
    }

    private void loadSwitchers() {
        rHours = (TextSwitcher) findViewById(R.id.remaining_hours);
        prepareSwitcher(rHours);
        rMin = (TextSwitcher) findViewById(R.id.remaining_minutes);
        prepareSwitcher(rMin);
        rSec = (TextSwitcher) findViewById(R.id.remaining_seconds);
        prepareSwitcher(rSec);
    }
    private void prepareSwitcher(TextSwitcher mSwitcher) {
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return getLayoutInflater().inflate(R.layout.ticket_timer_tv, null, false);
            }
        });

        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        in.setDuration(200);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        out.setDuration(200);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        t = ParkingSharedPref.getTicket(mAppContext, "ntate@gmail.com", 50);
        ticketTimer = t.getMillisecondsLeft();
        ParkingNotifications.startNotifications(mAppContext, ticketTimer);
        //loadSwitchers();
        lastH = getRemainingHours(ticketTimer);
        rHours.setCurrentText(lastH + "");
        lastM = getRemainingMinutes(ticketTimer);
        rMin.setCurrentText(lastM + "");
        lastS = getRemainingSeconds(ticketTimer);
        rSec.setCurrentText(lastS + "");

        countDownTimer = new CountDownTimer(ticketTimer, SECOND) {
            public void onTick(long millisUntilFinished) {
                if(getRemainingHours(millisUntilFinished) < lastH) {
                    lastH = getRemainingHours(millisUntilFinished);
                    rHours.setText(lastH+"");
                }

                if(getRemainingMinutes(millisUntilFinished) < lastM || lastM == 0) {
                    lastM = getRemainingMinutes(millisUntilFinished);

                    if(lastM < 10) {
                        rMin.setText("0"+lastM);
                    }
                    else {
                        rMin.setText(lastM+"");
                    }
                }

                if(getRemainingSeconds(millisUntilFinished) < lastS || lastS == 0) {
                    lastS = getRemainingSeconds(millisUntilFinished);

                    if(lastS < 10) {
                        rSec.setText("0"+lastS);
                    }
                    else {
                        rSec.setText(lastS+"");
                    }
                }
            }

            public void onFinish() {
                //.setText("done!");
                rMin.setText("DONE");
            }
        }.start();
    }

    private class MyDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener{

    	private final int SNAP_DELTA_MINUTES = 15;
    	private final double COST_PER_MINUTE = 0.01666666666666;
    	private SeekBar mTimeBar;
    	private TextView mHour;
    	private TextView mMinute;
    	private TextView mClock;
    	private Button mPark;
    	
    	private final int mMaxtimeSeconds = 7200; // maximum time in seconds the user can choose
    	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			getDialog().setTitle("Add Time");
			
			View v = inflater.inflate(R.layout.add_time_fragment, null);
			
			mTimeBar = (SeekBar) v.findViewById(R.id.park_time);
			mHour = (TextView) v.findViewById(R.id.hour);
			mMinute = (TextView) v.findViewById(R.id.minute);
			mClock = (TextView) v.findViewById(R.id.clock);
			mPark = (Button) v.findViewById(R.id.park);
			mPark.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getDialog().dismiss();
				}
			});
			
			mTimeBar.setMax((mMaxtimeSeconds/60)/SNAP_DELTA_MINUTES);
			mTimeBar.setOnSeekBarChangeListener(this);
			
			return v;
		}

		/**
		 * 
		 * @param calendar
		 *            - a Calendar object
		 */
		private void setClockTime(Calendar calendar) {
			int period = calendar.get(Calendar.AM_PM);
			String am_pm = (period == Calendar.AM) ? "AM" : "PM";
			int clock_hour = calendar.get(Calendar.HOUR_OF_DAY) % 12;
			
			// clock hour is 0. implies that the current hour is 12
			if(clock_hour == 0) 
				clock_hour = 12;
			
			int clock_minute = calendar.get(Calendar.MINUTE);
			mClock.setText(String.format("%02d:%02d %s", clock_hour, clock_minute,
					am_pm));
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
			mPark.setText("PARK (" +costString+")");

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
		
    }
}
