package com.codefest_jetsons.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codefest_jetsons.R;
import com.codefest_jetsons.components.QRCode;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.*;

/**
 * Created with IntelliJ IDEA. User: nick49rt Date: 2/23/13 Time: 6:38 PM To
 * change this template use File | Settings | File Templates.
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
