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
