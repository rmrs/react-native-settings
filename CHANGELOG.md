# Changelog

## [1.0.0](https://github.com/rmrs/react-native-settings/compare/v1.0.0...v1.0.0) (2022-11-05)


### Features

* add captioning setting ([b9abb47](https://github.com/rmrs/react-native-settings/commit/b9abb47f39ae730bec08672c7e51652207532a8a))
* official v1 release ([c7c9a0c](https://github.com/rmrs/react-native-settings/commit/c7c9a0c8755c20bd19a780040ac1f6141afbc844))


### Bug Fixes

* call getSystemService on main thread for Android &lt;= 5 ([6631981](https://github.com/rmrs/react-native-settings/commit/6631981b32afa0634bca9f25ba8b5b5197db4aae))
* Check hasActiveCatalystInstance when trying to sendEvent ([#47](https://github.com/rmrs/react-native-settings/issues/47)) ([dd7c7c6](https://github.com/rmrs/react-native-settings/commit/dd7c7c6d714905fd6286dfd25c858f553b706c2b))
* update android build gradle to fix compile warning ([2624bbf](https://github.com/rmrs/react-native-settings/commit/2624bbfe610772578053e995b921217a37d474f5))
* use getSystemService workaround for versions &lt; Android 6 ([#45](https://github.com/rmrs/react-native-settings/issues/45)) ([9407afd](https://github.com/rmrs/react-native-settings/commit/9407afd9637647c3aa3412231e65cd45f89840c2))


### Miscellaneous Chores

* release 1.0.0 ([#114](https://github.com/rmrs/react-native-settings/issues/114)) ([c7c9a0c](https://github.com/rmrs/react-native-settings/commit/c7c9a0c8755c20bd19a780040ac1f6141afbc844))

## [1.0.0](https://github.com/rmrs/react-native-settings/compare/v0.2.3...v1.0.0) (2022-11-05)


### Features

* official v1 release ([c7c9a0c](https://github.com/rmrs/react-native-settings/commit/c7c9a0c8755c20bd19a780040ac1f6141afbc844))


### Miscellaneous Chores

* release 1.0.0 ([#114](https://github.com/rmrs/react-native-settings/issues/114)) ([c7c9a0c](https://github.com/rmrs/react-native-settings/commit/c7c9a0c8755c20bd19a780040ac1f6141afbc844))

## v0.0.1-alpha7 (October 19, 2018)

### Android

* Event listeners must be registered explicitly in `MainApplication.java` instead of in the manifest. ([@erezrokah](https://github.com/erezrokah) in [#24](https://github.com/rmrs/react-native-settings/pull/24))
