package com.codefest_jetsons.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.codefest_jetsons.R;

/**
 * Created with IntelliJ IDEA.
 * User: nick49rt
 * Date: 2/23/13
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TicketInfoActivity extends Activity {
    private Context mAppContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getApplicationContext();
        setContentView(R.layout.ticket_info_activity);
    }

}
