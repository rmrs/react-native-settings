
# react-native-settings

## Still very much in Alpha

We created this module to allow us to query for specific device settings.
For example we wanted to know if the GPS is on/off without using 'react-native'
'geolocation' since using it will trigger a permission pop up.

We wanted to make the distinction between the two:

1. Permission is not allowed for our application.
2. Setting is disabled for the entire device.

This way we can prompt the user to go to the correct place in the settings
application and make sure our application is aware that the user disables/enables
a setting or denies/grants a permission.

Currently we've only added a way to extract the 'location' setting (and airplane mode on Android).
We will add more in the future based on requirements.

[`react-native example`](https://github.com/rmrs/react-native-settings/tree/master/example)  for both Android and iOS.

## Getting started

`$ npm install react-native-settings --save`

### Mostly automatic installation

`$ react-native link react-native-settings`

#### Android

In your `MainApplication.java` file register the receivers:

``` java
...

import android.content.IntentFilter;
import io.rumors.reactnativesettings.RNSettingsPackage;
import io.rumors.reactnativesettings.receivers.GpsLocationReceiver;
import io.rumors.reactnativesettings.receivers.AirplaneModeReceiver;

...

public class MainApplication extends Application implements ReactApplication {

  ...

  @Override
  public void onCreate() {

    ...

    registerReceiver(new GpsLocationReceiver(), new IntentFilter("android.location.PROVIDERS_CHANGED"));
    registerReceiver(new AirplaneModeReceiver(), new IntentFilter("android.intent.action.AIRPLANE_MODE"));
  }
}
```

### Manual installation

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-settings` and add `RNSettings.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNSettings.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`

- Add `import io.rumors.reactnativesettings.RNSettingsPackage;` to the imports at the top of the file
- Add `new RNSettingsPackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:

```java
include ':react-native-settings'
project(':react-native-settings').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-settings/android')
```

3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:

```java
  implementation project(':react-native-settings')
```

## Usage

### Android and iOS

#### Getting a setting

```javascript
import RNSettings from 'react-native-settings'

RNSettings.getSetting(RNSettings.LOCATION_SETTING).then(result => {
  if (result == RNSettings.ENABLED) {
    console.log('location is enabled')
  } else {
    console.log('location is disabled')
  }
})
```

#### Android only

```javascript
import RNSettings from 'react-native-settings'

RNSettings.getSetting(RNSettings.AIRPLANE_MODE_SETTING).then(result => {
  if (result == RNSettings.ENABLED) {
    console.log('airplane mode is enabled')
  } else {
    console.log('airplane mode is disabled')
  }
})
```

##### Open settings application in a specific setting

```javascript
import RNSettings from 'react-native-settings'

RNSettings.openSetting(RNSettings.ACTION_LOCATION_SOURCE_SETTINGS).
then((result) => {
if (result === RNSettings.ENABLED) {
  console.log('location is enabled')
}

RNSettings.openSetting(RNSettings.ACTION_AIRPLANE_MODE_SETTINGS).
then((result) => {
if (result === RNSettings.ENABLED) {
  console.log('airplane mode is enabled')
}
```

##### Listen to setting change event (when applicable)

```javascript
import RNSettings from 'react-native-settings'
import { DeviceEventEmitter } from 'react-native'

_handleGPSProviderEvent = (e) => {
  if (e[RNSettings.LOCATION_SETTING] === RNSettings.DISABLED) {
    console.log('Location was disabled')
  }
}

_handleAirplaneModeEvent = (e) => {
  if (e[RNSettings.AIRPLANE_MODE_SETTING] === RNSettings.ENABLED) {
    console.log('airplane mode was enabled')
  }
}

DeviceEventEmitter.addListener(RNSettings.GPS_PROVIDER_EVENT, this._handleGPSProviderEvent)
DeviceEventEmitter.addListener(RNSettings.AIRPLANE_MODE_EVENT, this._handleAirplaneModeEvent)
```
