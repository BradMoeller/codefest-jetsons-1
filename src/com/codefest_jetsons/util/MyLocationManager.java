package com.codefest_jetsons.util;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationManager {

	private final int TIME_UNTIL_STALE = 1000 * 60 * 2; // 2 minutes

	private int _distancePrecision = 10;
	private Activity _a;
	private LocationManager _locationManager;
	private Timer _timer;
	private LocationListener _gpsListener;
	private LocationListener _networkListener;
	private boolean _gpsEnabled = false;
	private boolean _networkEnabled = false;
	private boolean _isRunning = false;
	private MyLocationListener _listener;

	private volatile Location _currentBestLocation;
	private volatile boolean _newBestSinceLastUpdate = false;

	public MyLocationManager(Activity a, MyLocationListener listener) {
		_listener = listener;
		_a = a;
		_locationManager = (LocationManager) _a
				.getSystemService(Context.LOCATION_SERVICE);
		_timer = new Timer(true);
		initializeListeners();
	}

	/**
	 * Begin getting user location updates.
	 * 
	 * @param updateIntervalMilli
	 *            The minimum number of milliseconds between location updates
	 * @return true if BBLocationManager was successfully able to start
	 */
	public boolean startGettingLocations(long updateIntervalMilli) {

		// If we're already running, don't run again
		if (_isRunning)
			return false;

		// Exceptions will be thrown if provider is not permitted.
		try {
			_gpsEnabled = _locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}

		try {
			_networkEnabled = _locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// Don't start listeners if no provider is enabled
		if (!_gpsEnabled && !_networkEnabled)
			return false;

		// Get the best last known location
		Location curNetLoc = _locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Location curGpsLoc = _locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (isBetterLocation(curNetLoc, curGpsLoc)) {
			_currentBestLocation = curGpsLoc;
		} else {
			_currentBestLocation = curNetLoc;
		}
		_newBestSinceLastUpdate = true;

		if (_gpsEnabled)
			_locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, updateIntervalMilli,
					_distancePrecision, _gpsListener);

		if (_networkEnabled)
			_locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, updateIntervalMilli,
					_distancePrecision, _networkListener);

		_isRunning = true;
		_timer.schedule(new NotifyUser(), Calendar.getInstance().getTime(),
				updateIntervalMilli);
		return true;
	}

	/**
	 * Stop BBLocationManager from requesting further updates
	 */
	public void stopGettingLocations() {
		if (_isRunning) {
			_timer.cancel();
			_timer = new Timer(true);
			_locationManager.removeUpdates(_gpsListener);
			_locationManager.removeUpdates(_networkListener);
			_isRunning = false;
		}
	}

	/**
	 * 
	 * @return true if BBLocationManager is currently running
	 */
	public boolean isRunning() {
		return _isRunning;
	}

	/**
	 * 
	 * @return How many meters the user needs to move in order for
	 *         BBLocationManager to acknowledge a location change
	 */
	public int getDistancePrecision() {
		return _distancePrecision;
	}

	/**
	 * 
	 * @param distancePrecision
	 *            The number of meters needed to be different from the last
	 *            location for BBLocationManager to acknowledge a location
	 *            change
	 */
	public void setDistancePrecision(int distancePrecision) {
		_distancePrecision = distancePrecision;
	}

	class NotifyUser extends TimerTask {
		@Override
		public void run() {
			if (_newBestSinceLastUpdate) {
				_a.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						_listener.gotLocation(_currentBestLocation);
					}
				});
				_newBestSinceLastUpdate = false;
			}
		}
	}

	private void initializeListeners() {
		_gpsListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
				_gpsEnabled = true;
			}

			@Override
			public void onProviderDisabled(String provider) {
				_gpsEnabled = false;
			}

			@Override
			public void onLocationChanged(Location location) {
				if (isBetterLocation(location, _currentBestLocation)) {
					_currentBestLocation = location;
					_newBestSinceLastUpdate = true;
				}
			}
		};

		_networkListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
				_networkEnabled = true;
			}

			@Override
			public void onProviderDisabled(String provider) {
				_networkEnabled = false;
			}

			@Override
			public void onLocationChanged(Location location) {
				if (isBetterLocation(location, _currentBestLocation)) {
					_currentBestLocation = location;
					_newBestSinceLastUpdate = true;
				}
			}
		};
	}

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TIME_UNTIL_STALE;
		boolean isSignificantlyOlder = timeDelta < -TIME_UNTIL_STALE;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}