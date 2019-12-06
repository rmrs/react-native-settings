package io.rumors.reactnativesettings.handlers;

import android.content.Context;
import android.view.accessibility.CaptioningManager;

import io.rumors.reactnativesettings.Constants;

public class CaptioningSettingsHandler implements SettingsHandler<String> {
  private Context mContext;

  public CaptioningSettingsHandler(Context context) {
    this.mContext = context;
  }

  public String getSetting() {
    CaptioningManager captioningManager = (CaptioningManager) mContext.getSystemService(Context.CAPTIONING_SERVICE);

    if (captioningManager.isEnabled()) {
      return Constants.ENABLED;
    }

    return Constants.DISABLED;
  }
}
