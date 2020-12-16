package android.os;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Map;

public final class ServiceManager {
    private static final String TAG = "ServiceManager";
    private static final Object sLock = new Object();

    private static IServiceManager sServiceManager;

    private static final Map<String, IBinder> sCache = new ArrayMap<String, IBinder>();
    private static final int SLOW_LOG_INTERVAL_MS = 5000;
    private static final int STATS_LOG_INTERVAL_MS = 5000;
    private static final long GET_SERVICE_SLOW_THRESHOLD_US_CORE =
            SystemProperties.getInt("debug.servicemanager.slow_call_core_ms", 10) * 1000;
    private static final long GET_SERVICE_SLOW_THRESHOLD_US_NON_CORE =
            SystemProperties.getInt("debug.servicemanager.slow_call_ms", 50) * 1000;
    private static final int GET_SERVICE_LOG_EVERY_CALLS_CORE =
            SystemProperties.getInt("debug.servicemanager.log_calls_core", 100);
    private static final int GET_SERVICE_LOG_EVERY_CALLS_NON_CORE =
            SystemProperties.getInt("debug.servicemanager.log_calls", 200);
    private static int sGetServiceAccumulatedUs;
    private static int sGetServiceAccumulatedCallCount;
    private static long sLastStatsLogUptime;
    private static long sLastSlowLogUptime;
    private static long sLastSlowLogActualTime;

    interface Stats {
        int GET_SERVICE = 0;
        int COUNT = GET_SERVICE + 1;
    }

    public ServiceManager() {
    }

    private static IServiceManager getIServiceManager() {
        throw new UnsupportedOperationException("Stub!");
    }

    public static IBinder getService(String name) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static IBinder getServiceOrThrow(String name) throws ServiceNotFoundException {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void addService(String name, IBinder service) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void addService(String name, IBinder service, boolean allowIsolated) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void addService(String name, IBinder service, boolean allowIsolated,
                                  int dumpPriority) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static IBinder checkService(String name) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static boolean isDeclared(@NonNull String name) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static String[] getDeclaredInstances(@NonNull String iface) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static native IBinder waitForService(@NonNull String name);

    public static IBinder waitForDeclaredService(@NonNull String name) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static String[] listServices() {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void initServiceCache(Map<String, IBinder> cache) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static class ServiceNotFoundException extends Exception {
        public ServiceNotFoundException(String name) {
            super("No service published for: " + name);
        }
    }

    private static IBinder rawGetService(String name) throws RemoteException {
        throw new UnsupportedOperationException("Stub!");
    }
}