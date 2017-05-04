package io.rumors.reactnativesettings.receivers;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

import io.rumors.reactnativesettings.handlers.LocationSettingsHandler;
import io.rumors.reactnativesettings.Constants;

public class GpsLocationReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
      String gpsStatus = (new LocationSettingsHandler(context)).getSetting();

      Intent i = new Intent(Constants.PROVIDERS_CHANGED);
      i.putExtra(Constants.LOCATION_SETTING, gpsStatus);
      context.sendBroadcast(i);
    }
  }
 }
