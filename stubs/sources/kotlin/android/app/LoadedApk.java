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

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.SharedLibraryInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dalvik.system.BaseDexClassLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

final class IntentReceiverLeaked extends AndroidRuntimeException {

    public IntentReceiverLeaked(String msg) {
        throw new UnsupportedOperationException("Stub!");
    }

}

final class ServiceConnectionLeaked extends AndroidRuntimeException {

    public ServiceConnectionLeaked(String msg) {
        throw new UnsupportedOperationException("Stub!");
    }

}

public final class LoadedApk {
    static final String TAG = "LoadedApk";
    static final boolean DEBUG = false;

    private final ActivityThread mActivityThread;

    final String mPackageName;

    private final ApplicationInfo mApplicationInfo;

    private final String mAppDir;

    private final String mResDir;
    private final String[] mOverlayDirs;

    private final String mDataDir;

    private final String mLibDir;

    private final File mDataDirFile;
    private final File mDeviceProtectedDataDirFile;
    private final File mCredentialProtectedDataDirFile;

    private final ClassLoader mBaseClassLoader;
    private final ClassLoader mDefaultClassLoader;
    private final boolean mSecurityViolation;
    private final boolean mIncludeCode;
    private final boolean mRegisterPackage;

    Resources mResources;

    private final ClassLoader mClassLoader;

    private final Application mApplication;
    private final String[] mSplitNames;
    private final String[] mSplitAppDirs;

    private final String[] mSplitResDirs;
    private final String[] mSplitClassLoaderNames;

    private final AppComponentFactory mAppComponentFactory;

    Application getApplication() {
        throw new UnsupportedOperationException("Stub!");
    }

    public LoadedApk(ActivityThread activityThread, ApplicationInfo aInfo,
                     CompatibilityInfo compatInfo, ClassLoader baseLoader,
                     boolean securityViolation, boolean includeCode, boolean registerPackage) {
        throw new UnsupportedOperationException("Stub!");
    }

    private static ApplicationInfo adjustNativeLibraryPaths(ApplicationInfo info) {
        throw new UnsupportedOperationException("Stub!");
    }

    LoadedApk(ActivityThread activityThread) {
        throw new UnsupportedOperationException("Stub!");
    }

    void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        throw new UnsupportedOperationException("Stub!");
    }

    private AppComponentFactory createAppFactory(ApplicationInfo appInfo, ClassLoader cl) {
        throw new UnsupportedOperationException("Stub!");
    }

    public AppComponentFactory getAppFactory() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String getPackageName() {
        throw new UnsupportedOperationException("Stub!");
    }

    public ApplicationInfo getApplicationInfo() {
        throw new UnsupportedOperationException("Stub!");
    }

    public int getTargetSdkVersion() {
        throw new UnsupportedOperationException("Stub!");
    }

    public boolean isSecurityViolation() {
        throw new UnsupportedOperationException("Stub!");
    }


    public CompatibilityInfo getCompatibilityInfo() {
        throw new UnsupportedOperationException("Stub!");
    }

    public void setCompatibilityInfo(CompatibilityInfo compatInfo) {
        throw new UnsupportedOperationException("Stub!");
    }

    private static String[] getLibrariesFor(String packageName) {
        throw new UnsupportedOperationException("Stub!");
    }

    public void updateApplicationInfo(@NonNull ApplicationInfo aInfo,
                                      @Nullable List<String> oldPaths) {
        throw new UnsupportedOperationException("Stub!");
    }


    private void setApplicationInfo(ApplicationInfo aInfo) {
        throw new UnsupportedOperationException("Stub!");
    }


    public static void makePaths(ActivityThread activityThread,
                                 ApplicationInfo aInfo,
                                 List<String> outZipPaths) {
        throw new UnsupportedOperationException("Stub!");
    }

    private static void appendSharedLibrariesLibPathsIfNeeded(
            List<SharedLibraryInfo> sharedLibraries, ApplicationInfo aInfo,
            Set<String> outSeenPaths,
            List<String> outLibPaths) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void makePaths(ActivityThread activityThread,
                                 boolean isBundledApp,
                                 ApplicationInfo aInfo,
                                 List<String> outZipPaths,
                                 List<String> outLibPaths) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * This method appends a path to the appropriate native library folder of a
     * library if this library is hosted in an APK. This allows support for native
     * shared libraries. The library API is determined based on the application
     * ABI.
     *
     * @param path            Path to the library.
     * @param applicationInfo The application depending on the library.
     * @param outLibPaths     List to which to add the native lib path if needed.
     */
    private static void appendApkLibPathIfNeeded(@NonNull String path,
                                                 @NonNull ApplicationInfo applicationInfo, @Nullable List<String> outLibPaths) {
        throw new UnsupportedOperationException("Stub!");
    }


    ClassLoader getSplitClassLoader(String splitName) throws NameNotFoundException {
        throw new UnsupportedOperationException("Stub!");
    }

    String[] getSplitPaths(String splitName) throws NameNotFoundException {
        throw new UnsupportedOperationException("Stub!");
    }

    ClassLoader createSharedLibraryLoader(SharedLibraryInfo sharedLibrary,
                                          boolean isBundledApp, String librarySearchPath, String libraryPermittedPath) {
        throw new UnsupportedOperationException("Stub!");
    }

    private List<ClassLoader> createSharedLibrariesLoaders(List<SharedLibraryInfo> sharedLibraries,
                                                           boolean isBundledApp, String librarySearchPath, String libraryPermittedPath) {
        throw new UnsupportedOperationException("Stub!");
    }

    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Stub!");
    }

    private void setupJitProfileSupport() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String getAppDir() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String getLibDir() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String getResDir() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String[] getSplitAppDirs() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String[] getSplitResDirs() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String[] getOverlayDirs() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String getDataDir() {
        throw new UnsupportedOperationException("Stub!");
    }

    public File getDataDirFile() {
        throw new UnsupportedOperationException("Stub!");
    }

    public File getDeviceProtectedDataDirFile() {
        throw new UnsupportedOperationException("Stub!");
    }

    public File getCredentialProtectedDataDirFile() {
        throw new UnsupportedOperationException("Stub!");
    }


    public AssetManager getAssets() {
        throw new UnsupportedOperationException("Stub!");
    }


    public Resources getResources() {
        throw new UnsupportedOperationException("Stub!");
    }


    public Application makeApplication(boolean forceDefaultAppClass,
                                       Instrumentation instrumentation) {
        throw new UnsupportedOperationException("Stub!");
    }

    private void rewriteRValues(ClassLoader cl, String packageName, int id) {
        throw new UnsupportedOperationException("Stub!");
    }

    public void removeContextRegistrations(Context context,
                                           String who, String what) {
        throw new UnsupportedOperationException("Stub!");
    }
}