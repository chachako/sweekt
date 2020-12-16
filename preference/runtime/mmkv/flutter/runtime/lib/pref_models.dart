import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

part 'mmkv.dart';

part 'system.dart';

const _kChannel = const MethodChannel('meowbase.flutter.plugins/flupref');

abstract class FluprefModel {
  static const _KEY = 'key';
  static const _VALUE = 'value';
  static const _SYNCHRONOUS = 'synchronous';

  var _init = false;

  /// Preference file name
  String name;

  /// native (Editor.commit) all properties in this pref by default instead of native (Editor.apply)
  bool commitAllProperties = false;

  @protected
  Future<bool> providePref();

  /// Make sure already initialized of native platform
  Future<void> _ensureInitialization() async {
    if (_init) return;
    _init = await providePref();
  }

  /// Return the native value
  Future<T> decode<T>(String key, {
    T defaultValue,
    bool synchronous,
  }) async {
    await _ensureInitialization();
    final Map<String, dynamic> params = {
      _KEY: key,
      _VALUE: defaultValue,
      _SYNCHRONOUS: synchronous,
    };
    switch (T) {
      case bool:
        return _kChannel.invokeMethod('getBoolean', params);
      case int:
        return _kChannel.invokeMethod('getInt', params);
      case double:
        return _kChannel.invokeMethod('getDouble', params);
      case String:
        return _kChannel.invokeMethod('getString', params);
      case Uint8List:
        return _kChannel.invokeMethod('getByteArray', params);
      case List:
        return _kChannel.invokeMethod('getStringSet', params);
      default:
        throw TypeError();
    }
  }

  /// Return whether the native call is successful
  Future<bool> encode<T>(String key, Future<T> value, {
    bool synchronous,
  }) async {
    await _ensureInitialization();
    final Map<String, dynamic> params = {
      _KEY: key,
      _VALUE: await value,
      _SYNCHRONOUS: synchronous,
    };
    switch (T) {
      case bool:
        return _kChannel.invokeMethod('putBoolean', params);
      case int:
        return _kChannel.invokeMethod('putInt', params);
      case double:
        return _kChannel.invokeMethod('putDouble', params);
      case String:
        return _kChannel.invokeMethod('putString', params);
      case Uint8List:
        return _kChannel.invokeMethod('putByteArray', params);
      case List:
        return _kChannel.invokeMethod('putStringSet', params);
      default:
        throw TypeError();
    }
  }

  Future<bool> remove(String key, {
    bool synchronous,
  }) async {
    await _ensureInitialization();
    final Map<String, dynamic> params = {
      _KEY: key,
      _SYNCHRONOUS: synchronous,
    };
    return _kChannel.invokeMethod('remove', params);
  }

  Future<bool> clear(bool synchronous) async {
    await _ensureInitialization();
    final Map<String, dynamic> params = {
      _SYNCHRONOUS: synchronous,
    };
    return _kChannel.invokeMethod('clear', params);
  }
}

Future<T> pref<T>(T value) => Future.value(value);
