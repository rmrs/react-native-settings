package io.rumors.reactnativesettings.listeners;

import android.content.Intent;
import android.content.Context;
import android.view.accessibility.CaptioningManager;

import io.rumors.reactnativesettings.handlers.CaptioningSettingsHandler;
import io.rumors.reactnativesettings.Constants;

public class CaptioningChangeListener extends CaptioningManager.CaptioningChangeListener {
  private Context mContext;

  public CaptioningChangeListener(Context context) {
    this.mContext = context;
    CaptioningManager captioningManager = (CaptioningManager) mContext.getSystemService(Context.CAPTIONING_SERVICE);
    captioningManager.addCaptioningChangeListener(this);
  }

  @Override
  public void onEnabledChanged(boolean enabled) {
    String captioning = (new CaptioningSettingsHandler(mContext)).getSetting();

    Intent i = new Intent(Constants.CAPTIONING_CHANGED);
    i.putExtra(Constants.CAPTIONING_SETTINGS, captioning);
    mContext.sendBroadcast(i);
  }
}
