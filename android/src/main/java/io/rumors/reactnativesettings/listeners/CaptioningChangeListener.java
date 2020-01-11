package io.rumors.reactnativesettings.listeners;

import android.content.Intent;
import android.content.Context;
import android.view.accessibility.CaptioningManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Build;

import io.rumors.reactnativesettings.handlers.CaptioningSettingsHandler;
import io.rumors.reactnativesettings.Constants;

import java.util.concurrent.CountDownLatch;


public class CaptioningChangeListener extends CaptioningManager.CaptioningChangeListener {
  private Context mContext;

  private CaptioningManager mCaptioningManager;

  private CaptioningManager getCaptioningManager() {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      return (CaptioningManager) mContext.getSystemService(Context.CAPTIONING_SERVICE);
    } else {
      // Android <= 5 bug workaround
      // https://github.com/evernote/android-job/issues/48#issuecomment-221789418
      final CountDownLatch latch = new CountDownLatch(1);

      Handler handler = new Handler(Looper.getMainLooper());
      handler.post(new Runnable() {
          @Override
          public void run() {
              mCaptioningManager = (CaptioningManager) mContext.getSystemService(Context.CAPTIONING_SERVICE);
              latch.countDown();
          }
      });
  
      try {
          latch.await();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  
      return mCaptioningManager;
    }
  }

  public CaptioningChangeListener(Context context) {
    this.mContext = context;
    CaptioningManager captioningManager = getCaptioningManager();
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
