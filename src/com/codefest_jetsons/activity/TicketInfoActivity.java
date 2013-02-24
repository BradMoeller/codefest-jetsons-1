package com.codefest_jetsons.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.codefest_jetsons.R;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.util.ParkingNotifications;
import com.codefest_jetsons.util.ParkingSharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private MapFragment mMapFragment;
    private Marker mLastMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.ticket_info_activity);
        setListeners();

        Random r = new Random();
        long id = r.nextInt(Integer.MAX_VALUE);
        ParkingSharedPref.setTicket(mAppContext, "ntate@gmail.com", 50, new Date(), 11, 60, -79.9805, 40.431368 );
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
        
        FragmentManager manager = getFragmentManager();
	    FragmentTransaction transaction = manager.beginTransaction();

	    mMapFragment = MapFragment.newInstance();
	    transaction.add(R.id.mapHolder, mMapFragment);           
	    transaction.commit();
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
        
        if (mLastMarker != null) {
			mLastMarker.remove();
		}
        
        mMapFragment.getMap().getUiSettings().setZoomControlsEnabled(false);
		mMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
        LatLng ll = new LatLng(t.getLatitude(), t.getLongitude());
		mMapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 13.0f));
		mLastMarker = mMapFragment.getMap().addMarker(new MarkerOptions().position(ll));

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

}
