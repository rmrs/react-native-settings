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
} from 'react-native'
import RNSettings from 'react-native-settings'

type State = {
  location_on: boolean,
  airplane_on: boolean,
}

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

    // Android only
    RNSettings.getSetting(RNSettings.AIRPLANE_MODE_SETTING).then(result => {
      if (result === RNSettings.ENABLED) {
        this.setState({ airplane_on: true })
      } else {
        this.setState({ airplane_on: false })
      }
    })

    // Android only - register to gps provider change event
    DeviceEventEmitter.addListener(
      RNSettings.GPS_PROVIDER_EVENT,
      this._handleGPSProviderEvent
    )
    // Android only - register to airplane mode change event
    DeviceEventEmitter.addListener(
      RNSettings.AIRPLANE_MODE_EVENT,
      this._handleAirplaneModeEvent
    )
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={{ marginBottom: 20 }}>
          <Text style={styles.welcome}>react-native-settings</Text>
        </View>

        <View
          style={{
            flexDirection: 'row',
            justifyContent: 'space-between',
            width: Screen.width / 1.5,
            marginBottom: 20,
          }}
        >
          <Text style={{ fontSize: 18 }}>Location:</Text>
          {this.state.location_on
            ? <Text style={{ color: 'green', fontSize: 18 }}> ON</Text>
            : <Text style={{ color: 'red', fontSize: 18 }}> OFF</Text>}

          <Button title="Change" onPress={this._openLocationSetting} />
        </View>

        <View
          style={{
            flexDirection: 'row',
            justifyContent: 'space-between',
            width: Screen.width / 1.5,
            marginBottom: 20,
          }}
        >
          <Text style={{ fontSize: 18 }}>Airplane Mode:</Text>
          {this.state.airplane_on
            ? <Text style={{ color: 'green', fontSize: 18 }}> ON</Text>
            : <Text style={{ color: 'red', fontSize: 18 }}> OFF</Text>}
          <Button title="Change" onPress={this._openAirplaneSetting} />
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    borderWidth: 6,
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
})

AppRegistry.registerComponent('example', () => example)
