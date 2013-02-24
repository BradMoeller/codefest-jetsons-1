package com.codefest_jetsons;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.codefest_jetsons.model.Vehicle;

public class LicensePlateAdapter extends PagerAdapter  {
	private List<Vehicle> mVehicles;
	private Context mContext;
	private int mPrimaryItem = 0;
	private LicensePlateAdapterInterface mListener;
    private WeakReference<EditText> license1;
    private WeakReference<EditText> license2;

	public LicensePlateAdapter(List<Vehicle> vehicles, Context context, LicensePlateAdapterInterface listener) {
		mVehicles = vehicles;
		mContext = context;
		mListener = listener;
        license1 = null;
        license2 = null;
	}
	
	@Override
	public int getCount() {
		return mVehicles.size()+1;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}
	
	

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		if (position != mPrimaryItem) {
			if (mPrimaryItem == mVehicles.size()) {
				mListener.exitedCreatePlate();
				InputMethodManager im = ((InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE));
				im.hideSoftInputFromWindow(container.getWindowToken(), 0);
			}
			else if(position == mVehicles.size()) {
				mListener.enteredCreatePlate();
				// We just selected the new
				InputMethodManager im = ((InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE));
				im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
			mPrimaryItem = position; 
		}
	}

	@Override
	public float getPageWidth(int position) {
		// TODO Auto-generated method stub
		return super.getPageWidth(position);
		//return 0.8f;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}
	
	

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (position == mVehicles.size()) {
			view = inflater.inflate(R.layout.license_plate_edit_page, null);
			final EditText txt = (EditText) view.findViewById(R.id.firstText);  
			final EditText txt2 = (EditText) view.findViewById(R.id.secondText);
            license1 = new WeakReference<EditText>(txt);
            license2 = new WeakReference<EditText>(txt2);

			Typeface font = Typeface.createFromAsset(mContext.getAssets(), "license_plate_usa.ttf");  
			txt.setTypeface(font);
			txt2.setTypeface(font);
			
			txt.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					EditText e = (EditText) txt;
					if (e.getText().length() > 3) {
						txt.setText(txt.getText().toString().substring(0, 3));
					} else if (e.getText().length() == 3) {
						txt2.requestFocus();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void afterTextChanged(Editable t) {}
			});
			
		}
		else {
			view = inflater.inflate(R.layout.licence_plate_page, null);
			TextView txt = (TextView) view.findViewById(R.id.firstText);  
			TextView txt2 = (TextView) view.findViewById(R.id.secondText);
			
			Typeface font = Typeface.createFromAsset(mContext.getAssets(), "license_plate_usa.ttf");  
			txt.setTypeface(font);
			txt2.setTypeface(font);
			Vehicle v = mVehicles.get(position);
			txt.setText(v.getLicensePlate().substring(0, 3));
			txt2.setText(v.getLicensePlate().substring(3));
			TranslateAnimation ta = new TranslateAnimation(mContext, null);
		}
		((ViewPager) container).addView(view);
		return view;
	}

    public WeakReference<EditText> getLicenseEdit(int i) {
        if(i == 0) {
            return license1;
        }
        else {
            return license2;
        }
    }
}
