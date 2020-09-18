part of 'pref_models.dart';

abstract class SharedPrefModel extends FluprefModel {
  /// see native android.content.Context
  static const MODE_PRIVATE = 0x0000;
  @deprecated
  static const MODE_WORLD_READABLE = 0x0001;
  @deprecated
  static const MODE_WORLD_WRITEABLE = 0x0002;
  @deprecated
  static const MODE_MULTI_PROCESS = 0x0004;

  /// Preference read/write mode
  int mode = MODE_PRIVATE;

  @override
  Future<bool> providePref() => _kChannel.invokeMethod("provideSharedPref", [
        name,
        mode,
        commitAllProperties,
      ]);
}
