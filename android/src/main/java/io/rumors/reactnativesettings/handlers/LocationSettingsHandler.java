package io.rumors.reactnativesettings.handlers;

import android.location.LocationManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import io.rumors.reactnativesettings.Constants;

public class LocationSettingsHandler implements SettingsHandler<String> {
  private Context mContext;

  public LocationSettingsHandler(Context context) {
    this.mContext = context;
  }

  public static int getLocationMode(Context context) {
    int locationMode = Settings.Secure.LOCATION_MODE_OFF;
    String locationProviders;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      try {
        locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
      } catch (Settings.SettingNotFoundException e) {
        e.printStackTrace();
      }
    } else {
      locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

      if (TextUtils.isEmpty(locationProviders)) {
        locationMode = Settings.Secure.LOCATION_MODE_OFF;
      }
      else if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
        locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
      }
      else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
        locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
      }
      else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
        locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
      }

    }

    return locationMode;
  }

  public String getSetting() {
    if(getLocationMode(mContext) == Settings.Secure.LOCATION_MODE_OFF)
    {
      return Constants.DISABLED;
    }
    else
      return Constants.ENABLED;

  }
}
