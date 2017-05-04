package io.rumors.reactnativesettings.handlers;

import android.location.LocationManager;
import android.content.Context;

import io.rumors.reactnativesettings.Constants;

public class LocationSettingsHandler implements SettingsHandler<String> {
  private Context mContext;

  public LocationSettingsHandler(Context context) {
    this.mContext = context;
  }

  public String getSetting() {
    LocationManager LocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
    boolean gps_enabled = false;
    boolean network_enabled = false;

    try {
        gps_enabled = LocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } catch(Exception ex) {}

    try {
        network_enabled = LocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    } catch(Exception ex) {}

    if (gps_enabled || network_enabled) {
      return Constants.ENABLED;
    }

    return Constants.DISABLED;
  }
}
