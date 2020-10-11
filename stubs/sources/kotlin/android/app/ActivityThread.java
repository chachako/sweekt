package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

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
										  int flags){
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
}
