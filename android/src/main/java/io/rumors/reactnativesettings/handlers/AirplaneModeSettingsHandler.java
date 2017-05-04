package io.rumors.reactnativesettings.handlers;

import android.provider.Settings;
import android.content.Context;

import io.rumors.reactnativesettings.Constants;

public class AirplaneModeSettingsHandler implements SettingsHandler<String> {
  private Context mContext;

  public AirplaneModeSettingsHandler(Context context) {
    this.mContext = context;
  }

  public String getSetting() {
    boolean isAirplaneModeOn = Settings.Global.getInt(
      mContext.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

    if (isAirplaneModeOn) {
      return Constants.ENABLED;
    }

    return Constants.DISABLED;
  }
}
