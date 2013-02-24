package com.codefest_jetsons.activity;

import com.codefest_jetsons.R;
import com.codefest_jetsons.fragment.AddCardFragment;
import com.codefest_jetsons.fragment.SelectCardFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class PaymentActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener{

	private ViewPager mViewPager;
	private ActionBar mActionBar;
	private Tab mSelectCardsTab;
	private Tab mAddCardTab;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.payment_activity);
		
		mActionBar = getActionBar();

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		
		mSelectCardsTab = mActionBar.newTab().setText("Select Cards");
		mAddCardTab = mActionBar.newTab().setText("Add Card");
		
		mSelectCardsTab.setTabListener(this);
		mAddCardTab.setTabListener(this);
		
		mActionBar.addTab(mSelectCardsTab, 0);
		mActionBar.addTab(mAddCardTab, 1);
		
		mViewPager = (ViewPager) findViewById(R.id.payment_pager);
		mViewPager.setAdapter(new PaymentPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// do nothing
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(mViewPager != null)
			mViewPager.setCurrentItem(tab.getPosition(), true);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// do nothing
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// do nothing
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// do nothing
	}

	@Override
	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}
	
	private class PaymentPagerAdapter extends FragmentPagerAdapter{

		public PaymentPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View)object);
		}
		
		@Override
	    public Fragment getItem(int position) {
			return (position == 0) ? SelectCardFragment.newInstance(): AddCardFragment.newInstance();
		}
	}
}
