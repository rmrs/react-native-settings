
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

import io.rumors.reactnativesettings.handlers.*;

import java.util.Map;
import java.util.HashMap;

public class RNSettingsModule extends ReactContextBaseJavaModule {
  //javascript event names
  private static final String GPS_PROVIDER_EVENT = "GPS_PROVIDER_EVENT";
  private static final String AIRPLANE_MODE_EVENT = "AIRPLANE_MODE_EVENT";

  //error values
  private static final String E_FAILED_TO_GET_SETTINGS = "E_FAILED_TO_GET_SETTINGS";
  private static final String E_FAILED_TO_OPEN_SETTINGS = "E_FAILED_TO_OPEN_SETTINGS";

  //open settings names
  private static final String ACTION_LOCATION_SOURCE_SETTINGS = "ACTION_LOCATION_SOURCE_SETTINGS";
  private static final String ACTION_AIRPLANE_MODE_SETTINGS = "ACTION_AIRPLANE_MODE_SETTINGS";

  private Map<String, Integer> mOpenSettingToRequestCode = new HashMap<String, Integer>();
  private Map<Integer, String> mRequestCodeToOpenSetting = new HashMap<Integer, String>();

  private Map<String, String> mOpenSettingToSettingsName = new HashMap<String, String>();
  private Map<String, SettingsHandler> mSettingsHandlers = new HashMap<String, SettingsHandler>();

  private Promise mSettingsPromise;

  private ReactContext mReactContext;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      String openedSetting = mRequestCodeToOpenSetting.get(requestCode);
      if (openedSetting != null && mSettingsPromise != null) {
        String settingsName = mOpenSettingToSettingsName.get(openedSetting);
        mSettingsPromise.resolve(mSettingsHandlers.get(settingsName).getSetting());
        mSettingsPromise = null;
      }
    }
  };

  private class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String providerSetting = intent.getStringExtra(Constants.LOCATION_SETTING);
      WritableMap params = Arguments.createMap();
      params.putString(Constants.LOCATION_SETTING, providerSetting);
      sendEvent(GPS_PROVIDER_EVENT, params);
    }
  }

  private class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String providerSetting = intent.getStringExtra(Constants.AIRPLANE_MODE_SETTING);
      WritableMap params = Arguments.createMap();
      params.putString(Constants.AIRPLANE_MODE_SETTING, providerSetting);
      sendEvent(AIRPLANE_MODE_EVENT, params);
    }
  }

  private void sendEvent(String eventName, @Nullable WritableMap params) {
    mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
    .emit(eventName, params);
  }

  private void registerReceiver(Context reactContext, String filter, BroadcastReceiver receiver) {
    IntentFilter intentFilter = new IntentFilter(filter);
    reactContext.registerReceiver(receiver, intentFilter);
  }

  public RNSettingsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.mReactContext = reactContext;

    this.registerReceiver(reactContext, Constants.PROVIDERS_CHANGED, new LocationReceiver());
    this.registerReceiver(reactContext, Constants.AIRPLANE_MODE_CHANGED, new AirplaneModeReceiver());

    mSettingsHandlers.put(Constants.LOCATION_SETTING, new LocationSettingsHandler(reactContext));
    mSettingsHandlers.put(Constants.AIRPLANE_MODE_SETTING, new AirplaneModeSettingsHandler(reactContext));

    mOpenSettingToRequestCode.put(Settings.ACTION_LOCATION_SOURCE_SETTINGS, 0);
    mOpenSettingToRequestCode.put(Settings.ACTION_AIRPLANE_MODE_SETTINGS, 1);

    mRequestCodeToOpenSetting.put(0, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    mRequestCodeToOpenSetting.put(1, Settings.ACTION_AIRPLANE_MODE_SETTINGS);

    mOpenSettingToSettingsName.put(Settings.ACTION_LOCATION_SOURCE_SETTINGS, Constants.LOCATION_SETTING);
    mOpenSettingToSettingsName.put(Settings.ACTION_AIRPLANE_MODE_SETTINGS, Constants.AIRPLANE_MODE_SETTING);

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
    constants.put(AIRPLANE_MODE_EVENT, AIRPLANE_MODE_EVENT);

    //get settings
    constants.put(Constants.LOCATION_SETTING, Constants.LOCATION_SETTING);
    constants.put(Constants.AIRPLANE_MODE_SETTING, Constants.AIRPLANE_MODE_SETTING);
    constants.put(Constants.ENABLED, Constants.ENABLED);
    constants.put(Constants.DISABLED, Constants.DISABLED);

    //open settings
    constants.put(ACTION_LOCATION_SOURCE_SETTINGS, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    constants.put(ACTION_AIRPLANE_MODE_SETTINGS, Settings.ACTION_AIRPLANE_MODE_SETTINGS);
    return constants;
  }

  @ReactMethod
  public void getSetting(String setting, final Promise promise) {
    SettingsHandler handler = mSettingsHandlers.get(setting);
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
      currentActivity.startActivityForResult(settingsIntent, mOpenSettingToRequestCode.get(setting));
    } catch (Exception e) {
      mSettingsPromise.reject(E_FAILED_TO_OPEN_SETTINGS, e);
      mSettingsPromise = null;
    }
  }
}
