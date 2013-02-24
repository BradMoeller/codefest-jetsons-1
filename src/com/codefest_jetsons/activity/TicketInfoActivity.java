package com.codefest_jetsons.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.codefest_jetsons.R;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.util.MyLocationListener;
import com.codefest_jetsons.util.MyLocationManager;
import com.codefest_jetsons.util.ParkingNotifications;
import com.codefest_jetsons.util.ParkingSharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created with IntelliJ IDEA. User: nick49rt Date: 2/23/13 Time: 2:59 PM To
 * change this template use File | Settings | File Templates.
 */
public class TicketInfoActivity extends Activity implements MyLocationListener, OnMarkerClickListener {
    private Context mAppContext;

    private long ticketTimer;
    private static final int SECOND = 1000;
    private final int LOCATION_UPDATE_INTERVAL = 5000;
    private int HANDLER_DELAY = 1000;

    private TextSwitcher rHours;
    private TextSwitcher rMin;
    private TextSwitcher rSec;
    private int lastS;
    private int lastM;
    private int lastH;
    private CountDownTimer countDownTimer;
    private Ticket t;

    private Runnable expiredRunnable;
    private MapFragment mMapFragment;
    private Marker mLastMarker;
    private Marker mLastUserMarker;
    private MyLocationManager mLocationManager;
    private Button mAddTime;
    private Button mEndPark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.ticket_info_activity);

        //ParkingSharedPref.clearPrefs(mAppContext); // todo - remove or prefs cleared always - for testing
        setListeners();

        Random r = new Random();
        long id = r.nextInt(Integer.MAX_VALUE);
        //ParkingSharedPref.setTicket(mAppContext, "frank@gmail.com", 50, new Date(), 11, 60, 40.431368, -79.9805);
        ArrayList<Ticket> allTickets = ParkingSharedPref.getAllTickets(mAppContext, "frank@gmail.com");
        //t = ParkingSharedPref.getTicket(mAppContext, "frank@gmail.com", 50);
        t = allTickets.get(allTickets.size()-1);

        ticketTimer = t.getMillisecondsLeft();

        //ParkingNotifications.startNotifications(mAppContext, ticketTimer);

        final TextView endTime = (TextView) findViewById(R.id.expiration_time);
        SimpleDateFormat s = new SimpleDateFormat("h:mm a");
        endTime.setText(s.format(t.getEndTime()));
        loadSwitchers();
        
        mLocationManager = new MyLocationManager(this, this);

        lastH = getRemainingHours(ticketTimer);
        if(lastH < 10) {
            rHours.setCurrentText(lastH + "");
        }
        else {
            rHours.setCurrentText(lastH + "");
        }

        lastM = getRemainingMinutes(ticketTimer);
        if(lastM < 10) {
            rMin.setCurrentText("0" + lastM);
        }
        else {
            rMin.setCurrentText(lastM + "");
        }

        lastS = getRemainingSeconds(ticketTimer);
        if(lastS < 10) {
            rSec.setCurrentText("0" + lastS);
        }
        else {
            rSec.setCurrentText(lastS + "");
        }

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(ticketTimer, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                //if(getRemainingHours(millisUntilFinished) < lastH) {
                    lastH = Math.abs(getRemainingHours(millisUntilFinished));
                    rHours.setText(lastH+"");
                //}

                //if(getRemainingMinutes(millisUntilFinished) < lastM || lastM == 0) {
                    lastM = Math.abs(getRemainingMinutes(millisUntilFinished));

                    if(lastM < 10) {
                        rMin.setText("0"+lastM);
                    }
                    else {
                        rMin.setText(lastM+"");
                    }
                //}

                //if(getRemainingSeconds(millisUntilFinished) < lastS || lastS == 0) {
                    lastS = Math.abs(getRemainingSeconds(millisUntilFinished));

                    if(lastS < 10) {
                        rSec.setText("0"+lastS);
                    }
                    else {
                        rSec.setText(lastS+"");
                    }
                //}
            }

            @Override
            public void onFinish() {

                //rMin.setText("DONE");
                //findViewById(R.id.countdown_timer).setVisibility(View.GONE);
                //findViewById(R.id.expired).setVisibility(View.VISIBLE);
                Log.d("DERP", "ONFINISH COUNTDOWNTIMER: ");
                TextView header = (TextView) findViewById(R.id.paid_header);
                header.setText("EXPIRED");
                header.setBackgroundResource(R.drawable.red_gradient);
                TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
                expirationTime.setText("YOUR TICKET HAS EXPIRED");
                expirationTime.setTextColor(Color.parseColor("#990000"));
                rSec.setText("00");
                ((TextView) findViewById(R.id.remaining_text)).setText("TIME EXPIRED");
            }
        }.start();

        String currTicketID = String.valueOf(ParkingSharedPref.getCurrentTicketID(mAppContext, "frank@gmail.com"));
        boolean b = ParkingSharedPref.getValidated(mAppContext, "frank@gmail.com", currTicketID);
        if(ParkingSharedPref.getValidated(mAppContext, "frank@gmail.com", currTicketID)) {
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("VALIDATED");
            header.setBackgroundResource(R.drawable.blue_gradient);
        }

        if(ticketTimer < 0) {
            //findViewById(R.id.countdown_timer).setVisibility(View.GONE);
            //findViewById(R.id.expired).setVisibility(View.VISIBLE);
            Log.d("DERP", "TICKET TIMER LESS THAN ZERO: "+ticketTimer);
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("EXPIRED");
            header.setBackgroundResource(R.drawable.red_gradient);
            TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
            expirationTime.setText("YOUR TICKET HAS EXPIRED");
            expirationTime.setTextColor(Color.parseColor("#990000"));
        }

        /*
        ParkingSharedPref.setCreditCard(mAppContext, "frank@gmail.com", id,
                "Nick", "Tate", "4300112233445566", "09", "2014", "554", CreditCard.CreditCardType.MASTER_CARD);
        CreditCard cc = ParkingSharedPref.getCreditCard(mAppContext, "frank@gmail.com", id);
        */
        
        FragmentManager manager = getFragmentManager();
	    FragmentTransaction transaction = manager.beginTransaction();

	    mMapFragment = MapFragment.newInstance();
	    transaction.add(R.id.mapHolder, mMapFragment);           
	    transaction.commit();

        mAddTime = (Button) findViewById(R.id.add_time);
        mAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogFragment df = new MyDialogFragment();
                df.show(getFragmentManager(), null);
            }
        });

        mEndPark = (Button) findViewById(R.id.end_park);
        mEndPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mAppContext, TicketCreateActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
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
        mLocationManager.stopGettingLocations();
    }

    @Override
    public void onResume() {
        super.onResume();

        String currTicketID = String.valueOf(ParkingSharedPref.getCurrentTicketID(mAppContext, "frank@gmail.com"));
        if(ParkingSharedPref.getValidated(mAppContext, "frank@gmail.com", currTicketID)) {
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("VALIDATED");
            header.setBackgroundResource(R.drawable.blue_gradient);
        }

        if (mLastUserMarker != null) {
        	mLastUserMarker.remove();
		}
        
        mMapFragment.getMap().getUiSettings().setZoomControlsEnabled(false);
		mMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
        LatLng ll = new LatLng(t.getLatitude(), t.getLongitude());
		mMapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 13.0f));
		mLastUserMarker = mMapFragment.getMap()
		.addMarker(new MarkerOptions()
		.position(ll));
		mMapFragment.getMap().setOnMarkerClickListener(this);
		
		mLocationManager.startGettingLocations(LOCATION_UPDATE_INTERVAL);
		
        t = ParkingSharedPref.getTicket(mAppContext, "frank@gmail.com", ParkingSharedPref.getCurrentTicketID(this, "frank@gmail.com"));
        ticketTimer = t.getMillisecondsLeft();
        ParkingNotifications.startNotifications(mAppContext, ticketTimer);

        lastH = getRemainingHours(ticketTimer);
        if(lastH < 10) {
            rHours.setCurrentText("" + lastH);
        }
        else {
            rHours.setCurrentText(lastH + "");
        }

        lastM = getRemainingMinutes(ticketTimer);
        if(lastM < 10) {
            rMin.setCurrentText("0" + lastM);
        }
        else {
            rMin.setCurrentText(lastM + "");
        }

        lastS = getRemainingSeconds(ticketTimer);
        if(lastS < 10) {
            rSec.setCurrentText("0" + lastS);
        }
        else {
            rSec.setCurrentText(lastS + "");
        }

        if(ticketTimer < 0) {
            //findViewById(R.id.countdown_timer).setVisibility(View.GONE);
            //findViewById(R.id.expired).setVisibility(View.VISIBLE);
            Log.d("DERP", "TICKET TIMER: "+ticketTimer);
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("EXPIRED");
            header.setBackgroundResource(R.drawable.red_gradient);
            TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
            expirationTime.setText("YOUR TICKET HAS EXPIRED");
            expirationTime.setTextColor(Color.parseColor("#990000"));
        }
        else {
            if(countDownTimer != null) {
                countDownTimer.cancel();
            }
            countDownTimer = new CountDownTimer(ticketTimer, SECOND) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //if(getRemainingHours(millisUntilFinished) < lastH) {
                        lastH = Math.abs(getRemainingHours(millisUntilFinished));
                        rHours.setText(lastH+"");
                    //}

                    //if(getRemainingMinutes(millisUntilFinished) < lastM || lastM == 0) {
                        lastM = Math.abs(getRemainingMinutes(millisUntilFinished));

                        if(lastM < 10) {
                            rMin.setText("0"+lastM);
                        }
                        else {
                            rMin.setText(lastM+"");
                        }
                    //}

                    //if(getRemainingSeconds(millisUntilFinished) < lastS || lastS == 0) {
                        lastS = Math.abs(getRemainingSeconds(millisUntilFinished));

                        if(lastS < 10) {
                            rSec.setText("0"+lastS);
                        }
                        else {
                            rSec.setText(lastS+"");
                        }
                    //}
                }

                @Override
                public void onFinish() {
                    Log.d("DERP", "DERP ON FINISH EXPIRED");
                    TextView header = (TextView) findViewById(R.id.paid_header);
                    header.setText("EXPIRED");
                    header.setBackgroundResource(R.drawable.red_gradient);
                    TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
                    expirationTime.setText("YOUR TICKET HAS EXPIRED");
                    expirationTime.setTextColor(Color.parseColor("#990000"));
                    rSec.setText("00");
                    ((TextView) findViewById(R.id.remaining_text)).setText("TIME EXPIRED");
                }
            }.start();
        }

    }

	@Override
	public void gotLocation(Location location) {
		double lat = location.getLatitude() + 0.00001;
		double lon = location.getLongitude();
		final LatLng ll = new LatLng(lat, lon);
		if (mLastMarker != null) {
			mLastMarker.remove();
		}
		
		double MAX_LAT = Math.max(t.getLatitude(), lat);
		double MAX_LONG = Math.max(t.getLongitude(), lon);
		double MIN_LAT = Math.min(t.getLatitude(), lat);
		double MIN_LONG = Math.min(t.getLongitude(), lon);
        
		final LatLng northeast = new LatLng(MAX_LAT, MAX_LONG);
        final LatLng southwest = new LatLng(MIN_LAT, MIN_LONG);

        Runnable mapLoad = new Runnable() {
            @Override
            public void run() {
                GoogleMap map = mMapFragment.getMap();
                if(map != null) {
                    HANDLER_DELAY = 250;
                    //map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                    //        new LatLngBounds(southwest, northeast), 90));
                    LatLng ll2 = new LatLng(t.getLatitude(), t.getLongitude());
                    mMapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(ll2, 15.0f));
                    mLastMarker = map.addMarker(new MarkerOptions()
                    .position(ll)
            		.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                }
                else {
                    Handler myHandler = new Handler();
                    HANDLER_DELAY *= 2;
                    myHandler.postDelayed(this, HANDLER_DELAY);
                }
            }
        };

        Handler myHandler = new Handler();
        myHandler.postDelayed(mapLoad, HANDLER_DELAY);
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return true;
	}

    public void updateTicket(int minutesChanged) {
        t.updateTicket(minutesChanged);
        long currTicketID = ParkingSharedPref.getCurrentTicketID(mAppContext, "frank@gmail.com");
        ParkingSharedPref.setValidated(mAppContext, "frank@gmail.com", String.valueOf(currTicketID), false);
        ParkingSharedPref.setTicket(mAppContext, "frank@gmail.com", currTicketID, t.getPurchaseTime(), t.getMinutesPurchased(),
                t.getMaxMinutes(), t.getLatitude(), t.getLongitude());
        t = ParkingSharedPref.getTicket(mAppContext, "frank@gmail.com", currTicketID);

        TextView header = (TextView) findViewById(R.id.paid_header);
        header.setText("PAID");
        header.setBackgroundResource(R.drawable.green_gradient);

        ticketTimer = t.getMillisecondsLeft();
        ParkingNotifications.startNotifications(mAppContext, ticketTimer);

        lastH = getRemainingHours(ticketTimer);
        if(lastH < 10) {
            rHours.setCurrentText("" + lastH);
        }
        else {
            rHours.setCurrentText(lastH + "");
        }

        lastM = getRemainingMinutes(ticketTimer);
        if(lastM < 10) {
            rMin.setCurrentText("0" + lastM);
        }
        else {
            rMin.setCurrentText(lastM + "");
        }

        lastS = getRemainingSeconds(ticketTimer);
        if(lastS < 10) {
            rSec.setCurrentText("0" + lastS);
        }
        else {
            rSec.setCurrentText(lastS + "");
        }

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(ticketTimer, SECOND) {
            public void onTick(long millisUntilFinished) {
                //if(getRemainingHours(millisUntilFinished) < lastH) {
                lastH = Math.abs(getRemainingHours(millisUntilFinished));
                rHours.setText(lastH+"");
                //}

                //if(getRemainingMinutes(millisUntilFinished) < lastM || lastM == 0) {
                lastM = Math.abs(getRemainingMinutes(millisUntilFinished));

                if(lastM < 10) {
                    rMin.setText("0"+lastM);
                }
                else {
                    rMin.setText(lastM+"");
                }
                //}

                //if(getRemainingSeconds(millisUntilFinished) < lastS || lastS == 0) {
                lastS = Math.abs(getRemainingSeconds(millisUntilFinished));

                if(lastS < 10) {
                    rSec.setText("0"+lastS);
                }
                else {
                    rSec.setText(lastS+"");
                }
                //}
            }

            public void onFinish() {
                TextView header = (TextView) findViewById(R.id.paid_header);
                Log.d("DERP", "DERP 2 EXPIRED LOL");
                header.setText("EXPIRED");
                header.setBackgroundResource(R.drawable.red_gradient);
                TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
                expirationTime.setText("YOUR TICKET HAS EXPIRED");
                expirationTime.setTextColor(Color.parseColor("#990000"));
                rSec.setText("00");
                ((TextView) findViewById(R.id.remaining_text)).setText("TIME EXPIRED");
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
        private int minutesChanged;
    	
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
                    ((TicketInfoActivity) getActivity()).updateTicket(minutesChanged);
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
            minutesChanged = minutes;

			int hours = (secondsMoved / (60 * 60)) % 24;
            minutesChanged += hours*60;

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
