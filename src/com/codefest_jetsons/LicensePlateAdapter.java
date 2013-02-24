package com.codefest_jetsons;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.codefest_jetsons.model.CreditCard;

public class LicensePlateAdapter extends PagerAdapter  {
	private List<CreditCard> mCreditCards;
	private Context mContext;
	
	public LicensePlateAdapter(List<CreditCard> cards, Context context) {
		mCreditCards = cards;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return mCreditCards.size()+1;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
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
		if (position == mCreditCards.size()) {
			view = inflater.inflate(R.layout.license_plate_edit_page, null);
			final EditText txt = (EditText) view.findViewById(R.id.firstText);  
			final EditText txt2 = (EditText) view.findViewById(R.id.secondText);
			
			Typeface font = Typeface.createFromAsset(mContext.getAssets(), "license_plate_usa.ttf");  
			txt.setTypeface(font);
			txt2.setTypeface(font);
			txt.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					EditText e = (EditText)txt;
					if(e.getText().length() >3) {
						txt.setText(txt.getText().toString().substring(0, 3));
					}
					else if(e.getText().length() ==3) {
						txt2.requestFocus();
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable t) {
					
				}
			});
		}
		else {
			view = inflater.inflate(R.layout.licence_plate_page, null);
			TextView txt = (TextView) view.findViewById(R.id.firstText);  
			TextView txt2 = (TextView) view.findViewById(R.id.secondText);
			
			
			Typeface font = Typeface.createFromAsset(mContext.getAssets(), "license_plate_usa.ttf");  
			txt.setTypeface(font);
			txt2.setTypeface(font);
		}
		((ViewPager) container).addView(view);
		return view;
	}
}
