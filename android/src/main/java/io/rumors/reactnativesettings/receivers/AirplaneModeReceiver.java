package io.rumors.reactnativesettings.receivers;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

import io.rumors.reactnativesettings.handlers.AirplaneModeSettingsHandler;
import io.rumors.reactnativesettings.Constants;

public class AirplaneModeReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().matches("android.intent.action.AIRPLANE_MODE")) {
      String airplaneMode = (new AirplaneModeSettingsHandler(context)).getSetting();

      Intent i = new Intent(Constants.AIRPLANE_MODE_CHANGED);
      i.putExtra(Constants.AIRPLANE_MODE_SETTING, airplaneMode);
      context.sendBroadcast(i);
    }
  }
 }
