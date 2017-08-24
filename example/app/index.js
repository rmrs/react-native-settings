// @flow
import React, { Component } from 'react'
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  DeviceEventEmitter,
  Button,
  Dimensions,
  Platform,
  Alert,
} from 'react-native'
import RNSettings from 'react-native-settings'

type State = {
  location_on: boolean,
  airplane_on: boolean,
}

type Props = {}

const Screen = {
  width: Dimensions.get('window').width,
  height: Dimensions.get('window').height,
}

export default class example extends Component<void, void, State> {
  state: State

  constructor(props: Props) {
    super(props)
    this.state = { location_on: false, airplane_on: false }
  }

  _handleGPSProviderEvent = e => {
    if (e[RNSettings.LOCATION_SETTING] === RNSettings.ENABLED) {
      this.setState({ location_on: true })
    } else {
      this.setState({ location_on: false })
    }
  }

  _handleAirplaneModeEvent = e => {
    if (e[RNSettings.AIRPLANE_MODE_SETTING] === RNSettings.ENABLED) {
      this.setState({ airplane_on: true })
    } else {
      this.setState({ airplane_on: false })
    }
  }

  _openLocationSetting = () => {
    if (Platform.OS === 'ios') {
      Alert.alert(
        'Not supported!',
        'Not supported on IOS just yet. Stay tuned ~_~'
      )
      return
    }
    RNSettings.openSetting(
      RNSettings.ACTION_LOCATION_SOURCE_SETTINGS
    ).then(result => {
      if (result === RNSettings.ENABLED) {
        this.setState({ location_on: true })
      } else {
        this.setState({ location_on: false })
      }
    })
  }
  _openAirplaneSetting = () => {
    if (Platform.OS === 'ios') {
      Alert.alert(
        'Not supported!',
        'Not supported on IOS just yet. Stay tuned ~_~'
      )
      return
    }
    RNSettings.openSetting(
      RNSettings.ACTION_AIRPLANE_MODE_SETTINGS
    ).then(result => {
      if (result === RNSettings.ENABLED) {
        this.setState({ airplane_on: true })
      } else {
        this.setState({ airplane_on: false })
      }
    })
  }

  componentDidMount() {
    RNSettings.getSetting(RNSettings.LOCATION_SETTING).then(result => {
      if (result === RNSettings.ENABLED) {
        this.setState({ location_on: true })
      } else {
        this.setState({ location_on: false })
      }
    })

    if (Platform.OS === 'android') {
      RNSettings.getSetting(RNSettings.AIRPLANE_MODE_SETTING).then(result => {
        if (result === RNSettings.ENABLED) {
          this.setState({ airplane_on: true })
        } else {
          this.setState({ airplane_on: false })
        }
      })

      // Register to gps provider change event
      DeviceEventEmitter.addListener(
        RNSettings.GPS_PROVIDER_EVENT,
        this._handleGPSProviderEvent
      )
      // Register to airplane mode change event
      DeviceEventEmitter.addListener(
        RNSettings.AIRPLANE_MODE_EVENT,
        this._handleAirplaneModeEvent
      )
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={{ marginTop: 20, marginBottom: 20 }}>
          <Text style={styles.welcome}>react-native-settings</Text>
        </View>
        <SettingRow
          name="Location"
          on={this.state.location_on}
          onPress={this._openLocationSetting}
          onPressAvailable={Platform.os !== 'ios'}
        />
        <SettingRow
          name="Airplane Mode"
          on={this.state.airplacne_on}
          onAvailable={Platform.os !== 'ios'}
          onPress={this._openAirplaneSetting}
          onPressAvailable={Platform.os !== 'ios'}
        />

        <Text style={{ marginTop: 40 }}> * Not supported yet on iOS.</Text>
      </View>
    )
  }
}

type SettingRowProps = {
  name: string,
  on: boolean,
  onAvailable: boolean,
  onPress: () => void,
  onPressAvailable: boolean,
}

SettingRow = (props: SettingRowProp) => {
  let status = <Text />
  const is_not_available = Platform.OS === 'ios' && !props.onAvailable
  if (props.onAvailable) {
    status = <Text style={{ color: 'black', fontSize: 18 }}> N/A*</Text>
  } else {
    status = props.on
      ? <Text style={{ color: 'green', fontSize: 18 }}> ON</Text>
      : <Text style={{ color: 'red', fontSize: 18 }}> OFF</Text>
  }

  return (
    <View style={styles.settingRowContainer}>
      <Text style={{ fontSize: 18 }}>
        {props.name}:
      </Text>
      {status}
      <Button
        title={props.onPressAvailable ? 'N/A*' : 'Change'}
        onPress={props.onPress}
      />
    </View>
  )
}

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
})

AppRegistry.registerComponent('example', () => example)
