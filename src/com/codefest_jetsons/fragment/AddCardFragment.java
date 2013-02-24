package com.codefest_jetsons.fragment;

import com.codefest_jetsons.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddCardFragment extends Fragment {

	public static AddCardFragment newInstance(){
		return new AddCardFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.add_card_fragment, null);
		
		return view;
	}
}
