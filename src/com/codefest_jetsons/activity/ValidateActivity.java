package com.codefest_jetsons.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codefest_jetsons.R;
import com.codefest_jetsons.components.QRCode;
import com.codefest_jetsons.util.ParkingSharedPref;


/**
 * Created with IntelliJ IDEA. User: nick49rt Date: 2/23/13 Time: 6:38 PM To
 * change this template use File | Settings | File Templates.
 */
public class ValidateActivity extends Activity {
	Context mAppContext;
    private Handler mHandler;
    private Runnable mRunnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAppContext = getApplicationContext();
		setContentView(R.layout.validate_activity);


        final LinearLayout container = (LinearLayout) findViewById(R.id.qr_container);
        if(ParkingSharedPref.getValidated(mAppContext, "ntate22@gmail.com", "12345")) {
            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.ticket_timer_tv, null, false);
            tv.setText("VALIDATED");
            container.addView(tv);
        }
        else {
            final QRCode qr = new QRCode(mAppContext, "test", 600, 600);
            container.addView(qr);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    // fake data
                    ParkingSharedPref.setValidated(mAppContext, "ntate22@gmail.com", "12345", true);
                    qr.setVisibility(View.GONE);
                    TextView tv = (TextView) getLayoutInflater().inflate(R.layout.ticket_timer_tv, null, false);
                    tv.setText("VALIDATED");
                    container.addView(tv);
                }
            };

            mHandler = new Handler();
            mHandler.postDelayed(mRunnable, 7500); // wait 15 seconds
        }
	}
}
