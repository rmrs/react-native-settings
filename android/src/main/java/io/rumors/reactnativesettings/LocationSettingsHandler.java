package io.rumors.reactnativesettings;

import android.location.LocationManager;
import android.location.LocationManager;
import android.content.Context;
import android.app.Activity;

public class LocationSettingsHandler implements SettingsHandler {
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
      return RNSettingsModule.ENABLED;
    }

    return RNSettingsModule.DISABLED;
  }
}
