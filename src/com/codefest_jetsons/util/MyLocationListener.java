package com.codefest_jetsons.util;

import android.location.Location;

public interface MyLocationListener {

	/**
	* gotLocation will be triggered when BBLocationManager is running and has determined the users location has changed.
	* This could be due to BBLocationManager finding a more accurate location, or the user physically moving.
	* @param location The best current approximation of the user's location
	*/
	void gotLocation(Location location);
}
