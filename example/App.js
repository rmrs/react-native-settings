/**
 * @flow
 * @format
 */
import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  DeviceEventEmitter,
  Button,
  Dimensions,
  Platform,
  Alert,
} from 'react-native';
import RNSettings from 'react-native-settings';

type State = {
  location_on: boolean,
  airplane_on: boolean,
};

const Screen = {
  width: Dimensions.get('window').width,
  height: Dimensions.get('window').height,
};

export default class example extends Component<void, void, State> {
  state: State;

  state = { locationOn: false, airplaneOn: false };

  componentDidMount() {
    RNSettings.getSetting(RNSettings.LOCATION_SETTING).then(result => {
      if (result === RNSettings.ENABLED) {
        this.setState({ locationOn: true });
      } else {
        this.setState({ locationOn: false });
      }
    });

    if (Platform.OS === 'android') {
      RNSettings.getSetting(RNSettings.AIRPLANE_MODE_SETTING).then(result => {
        if (result === RNSettings.ENABLED) {
          this.setState({ airplaneOn: true });
        } else {
          this.setState({ airplaneOn: false });
        }
      });

      // Register to gps provider change event
      DeviceEventEmitter.addListener(
        RNSettings.GPS_PROVIDER_EVENT,
        this.handleGPSProviderEvent,
      );
      // Register to airplane mode change event
      DeviceEventEmitter.addListener(
        RNSettings.AIRPLANE_MODE_EVENT,
        this.handleAirplaneModeEvent,
      );
    }
  }

  handleGPSProviderEvent = e => {
    if (e[RNSettings.LOCATION_SETTING] === RNSettings.ENABLED) {
      this.setState({ locationOn: true });
    } else {
      this.setState({ locationOn: false });
    }
  };

  handleAirplaneModeEvent = e => {
    if (e[RNSettings.AIRPLANE_MODE_SETTING] === RNSettings.ENABLED) {
      this.setState({ airplaneOn: true });
    } else {
      this.setState({ airplaneOn: false });
    }
  };

  openLocationSetting = () => {
    if (Platform.OS === 'ios') {
      Alert.alert(
        'Not supported!',
        'Not supported on IOS just yet. Stay tuned ~_~',
      );
      return;
    }
    RNSettings.openSetting(RNSettings.ACTION_LOCATION_SOURCE_SETTINGS).then(
      result => {
        if (result === RNSettings.ENABLED) {
          this.setState({ locationOn: true });
        } else {
          this.setState({ locationOn: false });
        }
      },
    );
  };

  openAirplaneSetting = () => {
    if (Platform.OS === 'ios') {
      Alert.alert(
        'Not supported!',
        'Not supported on IOS just yet. Stay tuned ~_~',
      );
      return;
    }
    RNSettings.openSetting(RNSettings.ACTION_AIRPLANE_MODE_SETTINGS).then(
      result => {
        if (result === RNSettings.ENABLED) {
          this.setState({ airplaneOn: true });
        } else {
          this.setState({ airplaneOn: false });
        }
      },
    );
  };

  render() {
    const asterisk =
      Platform.OS === 'ios' ? (
        <Text style={{ marginTop: 40 }}> * Not supported yet on iOS.</Text>
      ) : (
        <Text />
      );

    const { locationOn, airplaneOn } = this.state;

    return (
      <View style={styles.container}>
        <View style={{ marginTop: 20, marginBottom: 20 }}>
          <Text style={styles.welcome}>react-native-settings</Text>
        </View>
        <SettingRow
          name="Location"
          on={locationOn}
          onAvailable
          onPress={this.openLocationSetting}
          onPressAvailable={Platform.OS !== 'ios'}
        />
        <SettingRow
          name="Airplane Mode"
          on={airplaneOn}
          onAvailable={Platform.OS !== 'ios'}
          onPress={this.openAirplaneSetting}
          onPressAvailable={Platform.OS !== 'ios'}
        />
        {asterisk}
      </View>
    );
  }
}

type SettingRowProps = {
  name: string,
  on: boolean,
  onAvailable: boolean,
  onPress: () => void,
  onPressAvailable: boolean,
};

const SettingRow = ({
  name,
  onAvailable,
  onPressAvailable,
  onPress,
  on,
}: SettingRowProps) => {
  let status = <Text />;

  if (onAvailable) {
    status = on ? (
      <Text style={{ color: 'green', fontSize: 18 }}> ON</Text>
    ) : (
      <Text style={{ color: 'red', fontSize: 18 }}> OFF</Text>
    );
  } else {
    status = <Text style={{ color: 'black', fontSize: 18 }}> N/A*</Text>;
  }

  return (
    <View style={styles.settingRowContainer}>
      <Text style={{ fontSize: 18 }}>{name}:</Text>
      {status}
      <Button title={onPressAvailable ? 'Change' : 'N/A*'} onPress={onPress} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  settingRowContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    width: Screen.width / 1.5,
    marginBottom: 20,
  },
});
