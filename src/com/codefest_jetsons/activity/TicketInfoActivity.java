package com.codefest_jetsons.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.codefest_jetsons.R;
import com.codefest_jetsons.model.Ticket;
import com.codefest_jetsons.util.ParkingSharedPref;

import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TicketInfoActivity extends Activity {
    private Context mAppContext;

    private static long ticketTimer;
    private static final int SECOND = 1000;

    private TextSwitcher rHours;
    private TextSwitcher rMin;
    private TextSwitcher rSec;
    private static int lastS;
    private static int lastM;
    private static int lastH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.ticket_info_activity);

        Random r = new Random();
        long id = r.nextInt(Integer.MAX_VALUE);
        ParkingSharedPref.setTicket(mAppContext, "ntate@gmail.com", id, new Date(), 120, 180);
        final Ticket t = ParkingSharedPref.getTicket(mAppContext, "ntate@gmail.com", id);

        ticketTimer = t.getMillisecondsLeft();
        final TextView endTime = (TextView) findViewById(R.id.expiration_time);
        endTime.setText(t.getEndTime().toString());
        loadSwitchers();

        lastH = getRemainingHours(ticketTimer);
        rHours.setText(lastH+"");
        lastM = getRemainingMinutes(ticketTimer);
        rMin.setText(lastM+"");
        lastS = getRemainingSeconds(ticketTimer);
        rSec.setText(lastS+"");

        new CountDownTimer(ticketTimer, SECOND) {
            public void onTick(long millisUntilFinished) {
                ticketTimer -= SECOND;

                if(getRemainingHours(ticketTimer) < lastH) {
                    lastH = getRemainingHours(ticketTimer);
                    rHours.setText(lastH+"");
                }

                if(getRemainingMinutes(ticketTimer) < lastM) {
                    lastM = getRemainingMinutes(ticketTimer);
                    rMin.setText(lastM+"");
                }

                if(getRemainingSeconds(ticketTimer) < lastS) {
                    lastS = getRemainingSeconds(ticketTimer);
                    rSec.setText(lastS+"");
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
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
    }

}
