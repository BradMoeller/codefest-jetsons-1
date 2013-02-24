package com.codefest_jetsons.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.codefest_jetsons.R;
import com.codefest_jetsons.components.QRCode;
import com.codefest_jetsons.util.ParkingNotifications;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidateActivity extends Activity {
    Context mAppContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.validate_activity);

        LinearLayout container = (LinearLayout) findViewById(R.id.qr_container);
        QRCode qr = new QRCode(mAppContext, "test", 600, 600);
        container.addView(qr);
    }
}
