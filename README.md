
# react-native-settings

## Getting started

`$ npm install react-native-settings --save`

### Mostly automatic installation

`$ react-native link react-native-settings`

#### Android
In your manifest file under:

``` xml
<application>
```
addd the following:

``` xml
<receiver android:name="io.rumors.reactnativesettings.GpsLocationReceiver">
  <intent-filter>
      <action android:name="android.location.PROVIDERS_CHANGED" />
      <category android:name="android.intent.category.DEFAULT" />
  </intent-filter>
</receiver>`
```
### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-settings` and add `RNSettings.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNSettings.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNSettingsPackage;` to the imports at the top of the file
  - Add `new RNSettingsPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-settings'
  	project(':react-native-settings').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-settings/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-settings')
  	```

## Usage
#### Android and iOS

##### Getting a setting:
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

#### Android only:
##### Open settings application in a specific setting
```javascript
import RNSettings from 'react-native-settings'

RNSettings.openSetting(RNSettings.ACTION_LOCATION_SOURCE_SETTINGS).
then((result) => {
if (result === RNSettings.ENABLED) {
  console.log('location is enabled')
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

DeviceEventEmitter.addListener(RNSettings.GPS_PROVIDER_EVENT, this._handleGPSProviderEvent)
```
