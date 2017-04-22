package io.rumors.reactnativesettings;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class GpsLocationReceiver extends BroadcastReceiver {
  public static final String PROVIDERS_CHANGED = "PROVIDERS_CHANGED";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
      String gpsStatus = (new LocationSettingsHandler(context)).getSetting();

      Intent i = new Intent(PROVIDERS_CHANGED);
      i.putExtra(RNSettingsModule.LOCATION_SETTING, gpsStatus);
      context.sendBroadcast(i);
    }
  }
 }
