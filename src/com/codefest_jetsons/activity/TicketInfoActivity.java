package com.codefest_jetsons.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.ExifInterface;
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
    private Runnable expiredRunnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.ticket_info_activity);

        ParkingSharedPref.clearPrefs(mAppContext); // todo - remove or prefs cleared always - for testing
        setListeners();

        Random r = new Random();
        long id = r.nextInt(Integer.MAX_VALUE);
        ParkingSharedPref.setTicket(mAppContext, "ntate@gmail.com", 50, new Date(), 2, 60, 40.431368, -79.9805);
        t = ParkingSharedPref.getTicket(mAppContext, "ntate@gmail.com", 50);

        ticketTimer = t.getMillisecondsLeft();

        //ParkingNotifications.startNotifications(mAppContext, ticketTimer);

        final TextView endTime = (TextView) findViewById(R.id.expiration_time);
        SimpleDateFormat s = new SimpleDateFormat("h:m a");
        endTime.setText(s.format(t.getEndTime()));
        loadSwitchers();

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

                //rMin.setText("DONE");
                //findViewById(R.id.countdown_timer).setVisibility(View.GONE);
                //findViewById(R.id.expired).setVisibility(View.VISIBLE);
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

        boolean b = ParkingSharedPref.getValidated(mAppContext, "ntate22@gmail.com", "12345");
        if(ParkingSharedPref.getValidated(mAppContext, "ntate22@gmail.com", "12345")) {
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("VALIDATED");
            header.setBackgroundResource(R.drawable.blue_gradient);
        }

        if(ticketTimer < 0) {
            //findViewById(R.id.countdown_timer).setVisibility(View.GONE);
            //findViewById(R.id.expired).setVisibility(View.VISIBLE);
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("EXPIRED");
            header.setBackgroundResource(R.drawable.red_gradient);
            TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
            expirationTime.setText("YOUR TICKET HAS EXPIRED");
            expirationTime.setTextColor(Color.parseColor("#990000"));
        }

        /*
        ParkingSharedPref.setCreditCard(mAppContext, "ntate@gmail.com", id,
                "Nick", "Tate", "4300112233445566", "09", "2014", "554", CreditCard.CreditCardType.MASTER_CARD);
        CreditCard cc = ParkingSharedPref.getCreditCard(mAppContext, "ntate@gmail.com", id);
        */
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

        if(ParkingSharedPref.getValidated(mAppContext, "ntate22@gmail.com", "12345")) {
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("VALIDATED");
            header.setBackgroundResource(R.drawable.blue_gradient);
        }

        t = ParkingSharedPref.getTicket(mAppContext, "ntate@gmail.com", 50);
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
            TextView header = (TextView) findViewById(R.id.paid_header);
            header.setText("EXPIRED");
            header.setBackgroundResource(R.drawable.red_gradient);
            TextView expirationTime = (TextView) findViewById(R.id.expiration_time);
            expirationTime.setText("YOUR TICKET HAS EXPIRED");
            expirationTime.setTextColor(Color.parseColor("#990000"));
        }
        else {
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

}
