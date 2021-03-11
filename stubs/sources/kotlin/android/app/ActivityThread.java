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

package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ActivityThread {
    public static ActivityThread currentActivityThread() {
        throw new UnsupportedOperationException("Stub!");
    }

    public static Application currentApplication() {
        throw new UnsupportedOperationException("Stub!");
    }

    public static String currentPackageName() {
        throw new UnsupportedOperationException("Stub!");
    }

    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo) {
        throw new UnsupportedOperationException("Stub!");
    }

    public String getProcessName() {
        throw new UnsupportedOperationException("Stub!");
    }

    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo,
                                          int flags) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static String currentProcessName() {
        throw new UnsupportedOperationException("Stub!");
    }

    public final LoadedApk getPackageInfo(ApplicationInfo ai, CompatibilityInfo compatInfo,
                                          int flags) {
        throw new UnsupportedOperationException("Stub!");
    }

    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo,
                                          int flags, int userId) {
        throw new UnsupportedOperationException("Stub!");
    }

    public final LoadedApk peekPackageInfo(String packageName, boolean includeCode) {
        throw new UnsupportedOperationException("Stub!");
    }

    public Application getApplication() {
        throw new UnsupportedOperationException("Stub!");
    }

    public Context getSystemContext() {
        throw new UnsupportedOperationException("Stub!");
    }

    static final class AppBindData {
        AppBindData() {
        }
        LoadedApk info;
        String processName;
        ApplicationInfo appInfo;
        List<ProviderInfo> providers;
        ComponentName instrumentationName;
        Bundle instrumentationArgs;
        int debugMode;
        boolean enableBinderTracking;
        boolean trackAllocation;
        boolean restrictedBackupMode;
        boolean persistent;
        Configuration config;
        CompatibilityInfo compatInfo;
        String buildSerial;
        long[] disabledCompatChanges;

        @NotNull
        @Override
        public String toString() {
            throw new UnsupportedOperationException("Stub!");
        }
    }

}
