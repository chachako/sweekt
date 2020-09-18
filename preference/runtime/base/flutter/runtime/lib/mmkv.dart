part of 'pref_models.dart';

abstract class MmkvModel extends FluprefModel {
  static const SINGLE_PROCESS_MODE = 1;
  static const MULTI_PROCESS_MODE = 2;

  /// Preference single/multi process mode
  int processMode = SINGLE_PROCESS_MODE;

  /// Preference file encrypted key
  String cryptKey;

  /// Preference file save path (must be a folder)
  String savePath;

  @override
  Future<bool> providePref() =>
      _kChannel.invokeMethod("provideMmkv", [
        name,
        processMode,
        commitAllProperties,
        cryptKey,
        savePath,
      ]);
}
