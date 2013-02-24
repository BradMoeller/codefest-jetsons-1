package com.codefest_jetsons.fragment;


import java.util.ArrayList;

import android.content.Intent;
import android.widget.Button;
import com.codefest_jetsons.R;
import com.codefest_jetsons.activity.TicketInfoActivity;
import com.codefest_jetsons.model.CreditCard;
import com.codefest_jetsons.util.ParkingSharedPref;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class SelectCardFragment extends Fragment {
	
	private ListView mListView;
	private ArrayList<CreditCard> mCreditCards;
	private Context mContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContext = getActivity();
		View view = inflater.inflate(R.layout.select_card_fragment, null);
		mListView = (ListView) view.findViewById(R.id.cards_list);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setItemsCanFocus(false);

        Button park = (Button) view.findViewById(R.id.select_card_park);
        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, TicketInfoActivity.class));
            }
        });

        String userID = "frank@gmail.com";
        mCreditCards = ParkingSharedPref.getAllCreditCards(mContext, userID);
		
		return view;
	}

	public static SelectCardFragment newInstance(){
		return new SelectCardFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mCreditCards = ParkingSharedPref.getAllCreditCards(mContext, "frank@gmail.com");
		if(mCreditCards != null && mCreditCards.size() != 0)
			mListView.setAdapter(new CardListAdapter());
	}
	
	private class CardListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mCreditCards.size();
		}

		@Override
		public CreditCard getItem(int position) {
			return mCreditCards.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckedTextView v = (CheckedTextView)convertView;

			if (v == null) {
				LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = (CheckedTextView)li.inflate(R.layout.checkedtextview_row, null);
				
				v.setText(formatCardNumber(mCreditCards.get(position).getCcNumber()));
			}
			
			return v;
		}

	}
	
	private String formatCardNumber(String cardNumber){
		return String.format("xxxx - xxxx - xxxx - %s", cardNumber.substring(cardNumber.length() - 4));
	}
}
