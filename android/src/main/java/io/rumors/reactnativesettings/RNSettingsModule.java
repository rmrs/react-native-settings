
package io.rumors.reactnativesettings;

import android.provider.Settings;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.content.BroadcastReceiver;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;
import java.util.HashMap;

public class RNSettingsModule extends ReactContextBaseJavaModule {

  private static final String GPS_PROVIDER_EVENT = "GPS_PROVIDER_EVENT";

  private static final String E_FAILED_TO_GET_SETTINGS = "E_FAILED_TO_GET_SETTINGS";
  private static final String E_FAILED_TO_OPEN_SETTINGS = "E_FAILED_TO_OPEN_SETTINGS";

  public static final String LOCATION_SETTING = "LOCATION_SETTING";
  public static final String ENABLED = "ENABLED";
  public static final String DISABLED = "DISABLED";

  private static final String ACTION_LOCATION_SOURCE_SETTINGS = "ACTION_LOCATION_SOURCE_SETTINGS";

  private Map<String, Integer> requestCodes = new HashMap<String, Integer>();
  private Map<String, SettingsHandler> settingsHandlers = new HashMap<String, SettingsHandler>();

  private Promise mSettingsPromise;

  private ReactContext mReactContext;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCodes.get(Settings.ACTION_LOCATION_SOURCE_SETTINGS) == requestCode) {
        if (mSettingsPromise != null) {
          mSettingsPromise.resolve(settingsHandlers.get(LOCATION_SETTING).getSetting());
          mSettingsPromise = null;
        }
      }
    }
  };

  private void sendEvent(String eventName, @Nullable WritableMap params) {
    mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit(eventName, params);
  }

  private void listenLocationProviderChange(Context reactContext) {
    IntentFilter intentFilter = new IntentFilter(GpsLocationReceiver.PROVIDERS_CHANGED);
    reactContext.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String providerSetting = intent.getStringExtra(LOCATION_SETTING);
        WritableMap params = Arguments.createMap();
        params.putString(LOCATION_SETTING, providerSetting);
        sendEvent(GPS_PROVIDER_EVENT, params);
      }
    }, intentFilter);
  }

  public RNSettingsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.mReactContext = reactContext;

    this.listenLocationProviderChange(reactContext);

    settingsHandlers.put(LOCATION_SETTING, new LocationSettingsHandler(reactContext));
    requestCodes.put(Settings.ACTION_LOCATION_SOURCE_SETTINGS, 0);
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNSettings";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();

    //event listeners
    constants.put(GPS_PROVIDER_EVENT, GPS_PROVIDER_EVENT);

    //get settings
    constants.put(LOCATION_SETTING, LOCATION_SETTING);
    constants.put(ENABLED, ENABLED);
    constants.put(DISABLED, DISABLED);

    //open settings
    constants.put(ACTION_LOCATION_SOURCE_SETTINGS, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    return constants;
  }

  @ReactMethod
  public void getSetting(String setting, final Promise promise) {
    SettingsHandler handler = settingsHandlers.get(setting);
    if (handler != null) {
      promise.resolve(handler.getSetting());
    } else {
      promise.reject(E_FAILED_TO_GET_SETTINGS, setting);
    }
  }

  @ReactMethod
  public void openSetting(String setting, final Promise promise) {
    mSettingsPromise = promise;

    try {
      Activity currentActivity = getCurrentActivity();
      final Intent settingsIntent = new Intent(setting);
      currentActivity.startActivityForResult(settingsIntent, requestCodes.get(setting));
    } catch (Exception e) {
      mSettingsPromise.reject(E_FAILED_TO_OPEN_SETTINGS, e);
      mSettingsPromise = null;
    }
  }
}
