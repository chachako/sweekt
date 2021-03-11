/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

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
