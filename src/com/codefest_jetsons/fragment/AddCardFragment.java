package com.codefest_jetsons.fragment;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import com.codefest_jetsons.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codefest_jetsons.activity.TicketInfoActivity;
import com.codefest_jetsons.util.ParkingSharedPref;
import com.codefest_jetsons.model.CreditCard;

public class AddCardFragment extends Fragment {

    private EditText cardHolderName;
    private EditText cardNumber;
    private EditText expirationMM;
    private EditText expirationYYYY;
    private EditText cvv;

	public static AddCardFragment newInstance(){
		return new AddCardFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.add_card_fragment, null);

        this.cardHolderName = (EditText) view.findViewById(R.id.card_name);
        this.cardNumber = (EditText) view.findViewById(R.id.card_number);
        this.expirationMM = (EditText) view.findViewById(R.id.card_month);
        this.expirationYYYY = (EditText) view.findViewById(R.id.card_year);
        this.cvv = (EditText) view.findViewById(R.id.card_cvv);

        Button park = (Button) view.findViewById(R.id.add_card_park);
        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long cID = 1234567;
                String ccType = "Visa";
                String userID = "frank@gmail.com";
                ParkingSharedPref.setCreditCard(getActivity(), userID, cID, AddCardFragment.this.cardHolderName.getText().toString(), AddCardFragment.this.cardHolderName.getText().toString(), AddCardFragment.this.cardNumber.getText().toString(), AddCardFragment.this.expirationMM.getText().toString(), AddCardFragment.this.expirationYYYY.getText().toString(), AddCardFragment.this.cvv.getText().toString(), CreditCard.CreditCardType.VISA);
                startActivity(new Intent(getActivity(), TicketInfoActivity.class));
            }
        });

		return view;
	}
}
