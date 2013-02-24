package com.codefest_jetsons.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Date;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codefest_jetsons.LicensePlateAdapter;
import com.codefest_jetsons.LicensePlateAdapterInterface;
import com.codefest_jetsons.R;
import com.codefest_jetsons.fragment.HeatMapDialogFragment;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.model.Vehicle;
import com.codefest_jetsons.util.MyLocationListener;
import com.codefest_jetsons.util.MyLocationManager;
import com.codefest_jetsons.util.ParkingSharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.tesseract.android.TessBaseAPI;

public class TicketCreateActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener, LicensePlateAdapterInterface, OnClickListener, AnimationListener, MyLocationListener, OnMapClickListener {

	private SeekBar mTimeBar;
	private TextView mHour;
	private TextView mMinute;
	private TextView mClock;
	private LicensePlateAdapter mPagerAdapter;
	private Button mPay;
	private Context mAppContext;
	private ViewPager myPager;
	private Animation mDownAnimation;
	private Animation mUpAnimation;
	private ImageButton mSliderLayout;
	private FrameLayout mMapHolder;
	private ImageView mImageOverlay;

	private MyLocationManager mLocationManager;
	private MapFragment mMapFragment;
	private Marker mLastMarker;
    private EditText license1;
    private EditText license2;

	private final int SNAP_DELTA_MINUTES = 15;
	private final int mMaxtimeSeconds = 7200; // maximum time in seconds the user can choose
	private final double COST_PER_MINUTE = 0.01666666666666;
	private final int LOCATION_UPDATE_INTERVAL = 5000;
	private final String USER_ID = "frank@gmail.com";

    public static final String PACKAGE_NAME = "com.datumdroid.android.ocr.simple";
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
    public static final String lang = "eng";
    private static final String TAG = "SimpleAndroidOCR.java";
    protected String _path;
    protected boolean _taken;
    protected TextView _field;
    protected static final String PHOTO_TAKEN = "photo_taken";

    private int mMinutesPurchased;
    private double mLatitude;
    private double mLongitude;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAppContext = getApplicationContext();
		setContentView(R.layout.ticket_creation_activity);

		mTimeBar = (SeekBar) findViewById(R.id.park_time);
		mHour = (TextView) findViewById(R.id.hour);
		mMinute = (TextView) findViewById(R.id.minute);
		mClock = (TextView) findViewById(R.id.clock);
		mPay = (Button) findViewById(R.id.pay);
		mSliderLayout = (ImageButton) findViewById(R.id.sliderLayout);
		mImageOverlay = (ImageView) findViewById(R.id.imageOverlay);
		mMapHolder = (FrameLayout) findViewById(R.id.mapHolder);
		
		mSliderLayout.setOnClickListener(this);
		
		// set the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		setClockTime(calendar);

		mTimeBar.setOnSeekBarChangeListener(this);
		mTimeBar.setMax((mMaxtimeSeconds/60)/SNAP_DELTA_MINUTES);
		
		mLocationManager = new MyLocationManager(this, this);
		
		//mTimeBar.setMax(max)
		 // Make some mock data for now
        List<Vehicle> vhs = new ArrayList<Vehicle>();

		// Make some mock data for now
        List<CreditCard> ccs = new ArrayList<CreditCard>();
        int y = 2013;
        Random r = new Random();
        /*
        if (ParkingSharedPref.getAllVehicles(mAppContext, USER_ID).size() == 1) {
        	// Create some fake data...
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 1, "ABC1234");
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 2, "FJT2651");
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 3, "EGG1337");
        	ParkingSharedPref.setVehicle(mAppContext, USER_ID, 4, "LOL4592");
        }
        */
        
        for(Vehicle v : ParkingSharedPref.getAllVehicles(mAppContext, USER_ID)) {
        	vhs.add(v);
        }
        
		mPagerAdapter = new LicensePlateAdapter(vhs, getApplicationContext(), this);
		myPager = (ViewPager) findViewById(R.id.viewPager);
		myPager.setAdapter(mPagerAdapter);
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		myPager.setOffscreenPageLimit(mPagerAdapter.getCount());
		// A little space between pages
		//myPager.setPageMargin(15);
		//myPager.setOnPageChangeListener(this);
		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		myPager.setClipChildren(false);

		// Animation stuff
		AnimationSet set = new AnimationSet(true);
		// Slide down animation
		mDownAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, //fromXType 
                0.0f,                       //fromXValue
                Animation.RELATIVE_TO_SELF, //toXType
                0.0f,                      //toXValue
                Animation.RELATIVE_TO_SELF, //fromYType
                -1.0f,                       //fromYValue
                Animation.RELATIVE_TO_SELF, //toYType
                0.0f);                      //toYValue
		mDownAnimation.setDuration(500);
		mDownAnimation.setFillAfter(true);
        set.addAnimation(mDownAnimation);
        // Slide up animation
        mUpAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, //fromXType 
                0.0f,                       //fromXValue
                Animation.RELATIVE_TO_SELF, //toXType
                0.0f,                      //toXValue
                Animation.RELATIVE_TO_SELF, //fromYType
                0.0f,                       //fromYValue
                Animation.RELATIVE_TO_SELF, //toYType
                -1.0f);                      //toYValue
        mUpAnimation.setDuration(500);
        mUpAnimation.setFillAfter(true);
		set.addAnimation(mUpAnimation);
		mUpAnimation.setAnimationListener(this);
		
		if (ParkingSharedPref.alreadyLaunched(mAppContext)) {
			mImageOverlay.setVisibility(View.GONE);
		}
		else {
			mImageOverlay.setVisibility(View.VISIBLE);
			mImageOverlay.setOnClickListener(this);
			ParkingSharedPref.setFirstLaunch(mAppContext);
		}
		
		mImageOverlay.setOnClickListener(this);
		
		FragmentManager manager = getFragmentManager();
	    FragmentTransaction transaction = manager.beginTransaction();

	    mMapFragment = MapFragment.newInstance();
	    transaction.add(R.id.mapHolder, mMapFragment);           
	    transaction.commit();
	    
	    mMapHolder.setOnClickListener(this);
		
        getWindow().setSoftInputMode(
        	    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Vehicle currVehicle = mPagerAdapter.getSelectedVehicle();
                Log.d("DERP", "CURR VEHICLE: " + currVehicle);

                long ticketID = System.currentTimeMillis();
                Date d = new Date();
                int maxTime = mMaxtimeSeconds / 60;
                ParkingSharedPref.setTicket(mAppContext, "frank@gmail.com", ticketID, d, mMinutesPurchased, maxTime, mLatitude, mLongitude);

                ParkingSharedPref.setCurrentTicketID(mAppContext, "frank@gmail.com", ticketID);

                startActivity(new Intent(mAppContext, PaymentActivity.class));
			}
		});

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }

        // lang.traineddata file with the app (in assets folder)
        // You can get them at:
        // http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/eng.traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }


        // _image = (ImageView) findViewById(R.id.image);
        //_field = (TextView) findViewById(R.id.ocr_field);
        _path = DATA_PATH + "/ocr.jpg";

        ImageButton ib = (ImageButton) findViewById(R.id.sliderLayout);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraActivity();
            }
        });
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.stopGettingLocations();
	}

	@Override
	protected void onResume() {
		mLocationManager.startGettingLocations(LOCATION_UPDATE_INTERVAL);
		mMapFragment.getMap().getUiSettings().setZoomControlsEnabled(false);
		mMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
		mMapFragment.getMap().setOnMapClickListener(this);
		super.onResume();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		progress *= SNAP_DELTA_MINUTES;
		// calculate the seconds moved
		// 100 is the max progress, by default
		int secondsMoved = progress * 60;//(progress * mMaxtimeSeconds) / 100;

        mMinutesPurchased = secondsMoved / 60;

		// update the time
		int minutes = (secondsMoved / 60) % 60;
		int hours = (secondsMoved / (60 * 60)) % 24;
		mMinute.setText(String.format("%02d", minutes));
		mHour.setText(String.format("%02d", hours));
		
		// Update the cost
		double cost = COST_PER_MINUTE * progress;
		String costString = java.text.NumberFormat.getCurrencyInstance().format(cost);
		mPay.setText(getResources().getString(R.string.pay) + " (" +costString+")");

		// update the clock
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis()
				+ (secondsMoved * 1000));
		setClockTime(calendar);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// do nothing
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
	public void enteredCreatePlate() {
		mSliderLayout.setVisibility(View.VISIBLE);
		mSliderLayout.setEnabled(true);
		mSliderLayout.startAnimation(mDownAnimation);	
	}

	@Override
	public void exitedCreatePlate() {
		mSliderLayout.startAnimation(mUpAnimation);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == mImageOverlay.getId()) {
			mImageOverlay.setVisibility(View.GONE);
		}
		else if(v.getId() == mMapHolder.getId()) {
			FragmentManager fm = this.getFragmentManager();
	        HeatMapDialogFragment df = new HeatMapDialogFragment();
	        df.show(fm, "fragment_name");
		}
		// TODO NICK 
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		mSliderLayout.setVisibility(View.GONE);
		mSliderLayout.setEnabled(false);
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void gotLocation(Location location) {
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		LatLng ll = new LatLng(lat, lon);
		if (mLastMarker != null) {
			mLastMarker.remove();
		}
		mMapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15.0f));
		mLastMarker = mMapFragment.getMap()
		.addMarker(new MarkerOptions()
		.position(ll)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
		
        mLatitude = lat;
        mLongitude = lon;
	}
	
    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "resultCode: " + resultCode);

        if (resultCode == -1) {
            onPhotoTaken();
        } else {
            Log.v(TAG, "User cancelled");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TicketCreateActivity.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(TicketCreateActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }

    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

        try {
            ExifInterface exif = new ExifInterface(_path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            if (rotate != 0) {

                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }

            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        } catch (IOException e) {
            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
        }

        // _image.setImageBitmap( bitmap );

        Log.v(TAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, lang);
        baseApi.setImage(bitmap);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        // You now have the text in recognizedText var, you can do anything with it.
        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
        // so that garbage doesn't make it to the display.

        Log.v(TAG, "OCRED TEXT: " + recognizedText);

        if ( lang.equalsIgnoreCase("eng") ) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }

        recognizedText = recognizedText.trim();

        if ( recognizedText.length() != 0 ) {
            if(recognizedText.length() >= 7) {
                mPagerAdapter.getLicenseEdit(0).get().setText(recognizedText.substring(0, 3));
                mPagerAdapter.getLicenseEdit(1).get().setText(recognizedText.substring(3, 7));
            }

            //_field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
            //_field.setSelection(_field.getText().toString().length());
        }

        // Cycle done.
    }

	@Override
	public void onMapClick(LatLng arg0) {
		FragmentManager fm = this.getFragmentManager();
        HeatMapDialogFragment df = new HeatMapDialogFragment();
        df.show(fm, "fragment_name");
	}

}
