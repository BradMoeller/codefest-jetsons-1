package com.codefest_jetsons.fragment;

import android.content.Intent;
import android.widget.Button;
import com.codefest_jetsons.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codefest_jetsons.activity.TicketInfoActivity;

public class AddCardFragment extends Fragment {

	public static AddCardFragment newInstance(){
		return new AddCardFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.add_card_fragment, null);

        Button park = (Button) view.findViewById(R.id.add_card_park);
        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TicketInfoActivity.class));
            }
        });

		return view;
	}
}
