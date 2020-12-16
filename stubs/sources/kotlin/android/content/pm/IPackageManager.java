/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: frameworks/base/core/java/android/content/pm/IPackageManager.aidl
 */
package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.IBinder;

import java.util.List;

/**
 * See {@link PackageManager} for documentation on most of the APIs
 * here.
 * <p>
 */
public interface IPackageManager extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    abstract class Stub extends android.os.Binder implements android.content.pm.IPackageManager {
        private static final java.lang.String DESCRIPTOR = "android.content.pm.IPackageManager";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an android.content.pm.IPackageManager interface,
         * generating a proxy if needed.
         */
        public static android.content.pm.IPackageManager asInterface(android.os.IBinder obj) {
            throw new UnsupportedOperationException("Stub!");
        }


        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) {
            throw new UnsupportedOperationException("Stub!");
        }

        private static class Proxy implements android.content.pm.IPackageManager {
            @Override
            public boolean isPackageAvailable(String packageName, int userId) {
                return false;
            }

            @Override
            public PackageInfo getPackageInfo(String packageName, int flags, int userId) {
                return null;
            }

            @Override
            public int getPackageUid(String packageName, int userId) {
                return 0;
            }

            @Override
            public int[] getPackageGids(String packageName) {
                return new int[0];
            }

            @Override
            public String[] currentToCanonicalPackageNames(String[] names) {
                return new String[0];
            }

            @Override
            public String[] canonicalToCurrentPackageNames(String[] names) {
                return new String[0];
            }

            @Override
            public PermissionInfo getPermissionInfo(String name, int flags) {
                return null;
            }

            @Override
            public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) {
                return null;
            }

            @Override
            public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) {
                return null;
            }

            @Override
            public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
                return null;
            }

            @Override
            public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) {
                return null;
            }

            @Override
            public ActivityInfo getActivityInfo(ComponentName className, int flags, int userId) {
                return null;
            }

            @Override
            public boolean activitySupportsIntent(ComponentName className, Intent intent, String resolvedType) {
                return false;
            }

            @Override
            public ActivityInfo getReceiverInfo(ComponentName className, int flags, int userId) {
                return null;
            }

            @Override
            public ServiceInfo getServiceInfo(ComponentName className, int flags, int userId) {
                return null;
            }

            @Override
            public ProviderInfo getProviderInfo(ComponentName className, int flags, int userId) {
                return null;
            }

            @Override
            public int checkPermission(String permName, String pkgName) {
                return 0;
            }

            @Override
            public int checkUidPermission(String permName, int uid) {
                return 0;
            }

            @Override
            public boolean addPermission(PermissionInfo info) {
                return false;
            }

            @Override
            public void removePermission(String name) {

            }

            @Override
            public void grantPermission(String packageName, String permissionName) {

            }

            @Override
            public void revokePermission(String packageName, String permissionName) {

            }

            @Override
            public boolean isProtectedBroadcast(String actionName) {
                return false;
            }

            @Override
            public int checkSignatures(String pkg1, String pkg2) {
                return 0;
            }

            @Override
            public int checkUidSignatures(int uid1, int uid2) {
                return 0;
            }

            @Override
            public String[] getPackagesForUid(int uid) {
                return new String[0];
            }

            @Override
            public String getNameForUid(int uid) {
                return null;
            }

            @Override
            public int getUidForSharedUser(String sharedUserName) {
                return 0;
            }

            @Override
            public int getFlagsForUid(int uid) {
                return 0;
            }

            @Override
            public boolean isUidPrivileged(int uid) {
                return false;
            }

            @Override
            public String[] getAppOpPermissionPackages(String permissionName) {
                return new String[0];
            }

            @Override
            public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public boolean canForwardTo(Intent intent, String resolvedType, int sourceUserId, int targetUserId) {
                return false;
            }

            @Override
            public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller, Intent[] specifics, String[] specificTypes, Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) {
                return null;
            }

            @Override
            public ParceledListSlice getInstalledPackages(int flags, int userId) {
                return null;
            }

            @Override
            public ParceledListSlice getPackagesHoldingPermissions(String[] permissions, int flags, int userId) {
                return null;
            }

            @Override
            public ParceledListSlice getInstalledApplications(int flags, int userId) {
                return null;
            }

            @Override
            public List<ApplicationInfo> getPersistentApplications(int flags) {
                return null;
            }

            @Override
            public ProviderInfo resolveContentProvider(String name, int flags, int userId) {
                return null;
            }

            @Override
            public void querySyncProviders(List<String> outNames, List<ProviderInfo> outInfo) {

            }

            @Override
            public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
                return null;
            }

            @Override
            public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) {
                return null;
            }

            @Override
            public List<InstrumentationInfo> queryInstrumentation(String targetPackage, int flags) {
                return null;
            }

            @Override
            public void installPackage(String originPath, IPackageInstallObserver2 observer, int flags, String installerPackageName, VerificationParams verificationParams, String packageAbiOverride) {

            }

            @Override
            public void installPackageAsUser(String originPath, IPackageInstallObserver2 observer, int flags, String installerPackageName, VerificationParams verificationParams, String packageAbiOverride, int userId) {

            }

            @Override
            public void finishPackageInstall(int token) {

            }

            @Override
            public void setInstallerPackageName(String targetPackage, String installerPackageName) {

            }

            @Override
            public void deletePackageAsUser(String packageName, IPackageDeleteObserver observer, int userId, int flags) {

            }

            @Override
            public void deletePackage(String packageName, IPackageDeleteObserver2 observer, int userId, int flags) {

            }

            @Override
            public String getInstallerPackageName(String packageName) {
                return null;
            }

            @Override
            public void addPackageToPreferred(String packageName) {

            }

            @Override
            public void removePackageFromPreferred(String packageName) {

            }

            @Override
            public List<PackageInfo> getPreferredPackages(int flags) {
                return null;
            }

            @Override
            public void resetPreferredActivities(int userId) {

            }

            @Override
            public ResolveInfo getLastChosenActivity(Intent intent, String resolvedType, int flags) {
                return null;
            }

            @Override
            public void setLastChosenActivity(Intent intent, String resolvedType, int flags, IntentFilter filter, int match, ComponentName activity) {

            }

            @Override
            public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) {

            }

            @Override
            public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) {

            }

            @Override
            public void clearPackagePreferredActivities(String packageName) {

            }

            @Override
            public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) {
                return 0;
            }

            @Override
            public void addPersistentPreferredActivity(IntentFilter filter, ComponentName activity, int userId) {

            }

            @Override
            public void clearPackagePersistentPreferredActivities(String packageName, int userId) {

            }

            @Override
            public void addCrossProfileIntentFilter(IntentFilter intentFilter, String ownerPackage, int ownerUserId, int sourceUserId, int targetUserId, int flags) {

            }

            @Override
            public void clearCrossProfileIntentFilters(int sourceUserId, String ownerPackage, int ownerUserId) {

            }

            @Override
            public ComponentName getHomeActivities(List<ResolveInfo> outHomeCandidates) {
                return null;
            }

            @Override
            public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) {

            }

            @Override
            public int getComponentEnabledSetting(ComponentName componentName, int userId) {
                return 0;
            }

            @Override
            public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) {

            }

            @Override
            public int getApplicationEnabledSetting(String packageName, int userId) {
                return 0;
            }

            @Override
            public void setPackageStoppedState(String packageName, boolean stopped, int userId) {

            }

            @Override
            public void freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer) {

            }

            @Override
            public void freeStorage(long freeStorageSize, IntentSender pi) {

            }

            @Override
            public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) {

            }

            @Override
            public void clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) {

            }

            @Override
            public void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) {

            }

            @Override
            public String[] getSystemSharedLibraryNames() {
                return new String[0];
            }

            @Override
            public FeatureInfo[] getSystemAvailableFeatures() {
                return new FeatureInfo[0];
            }

            @Override
            public boolean hasSystemFeature(String name) {
                return false;
            }

            @Override
            public void enterSafeMode() {

            }

            @Override
            public boolean isSafeMode() {
                return false;
            }

            @Override
            public void systemReady() {

            }

            @Override
            public boolean hasSystemUidErrors() {
                return false;
            }

            @Override
            public void performBootDexOpt() {

            }

            @Override
            public boolean performDexOptIfNeeded(String packageName, String instructionSet) {
                return false;
            }

            @Override
            public void forceDexOpt(String packageName) {

            }

            @Override
            public void updateExternalMediaStatus(boolean mounted, boolean reportStatus) {

            }

            @Override
            public PackageCleanItem nextPackageToClean(PackageCleanItem lastPackage) {
                return null;
            }

            @Override
            public void movePackage(String packageName, IPackageMoveObserver observer, int flags) {

            }

            @Override
            public boolean addPermissionAsync(PermissionInfo info) {
                return false;
            }

            @Override
            public boolean setInstallLocation(int loc) {
                return false;
            }

            @Override
            public int getInstallLocation() {
                return 0;
            }

            @Override
            public int installExistingPackageAsUser(String packageName, int userId) {
                return 0;
            }

            @Override
            public void verifyPendingInstall(int id, int verificationCode) {

            }

            @Override
            public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {

            }

            @Override
            public VerifierDeviceIdentity getVerifierDeviceIdentity() {
                return null;
            }

            @Override
            public boolean isFirstBoot() {
                return false;
            }

            @Override
            public boolean isOnlyCoreApps() {
                return false;
            }

            @Override
            public boolean isUpgrade() {
                return false;
            }

            @Override
            public void setPermissionEnforced(String permission, boolean enforced) {

            }

            @Override
            public boolean isPermissionEnforced(String permission) {
                return false;
            }

            @Override
            public boolean isStorageLow() {
                return false;
            }

            @Override
            public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, int userId) {
                return false;
            }

            @Override
            public boolean getApplicationHiddenSettingAsUser(String packageName, int userId) {
                return false;
            }

            @Override
            public IPackageInstaller getPackageInstaller() {
                return null;
            }

            @Override
            public boolean setBlockUninstallForUser(String packageName, boolean blockUninstall, int userId) {
                return false;
            }

            @Override
            public boolean getBlockUninstallForUser(String packageName, int userId) {
                return false;
            }

            @Override
            public KeySet getKeySetByAlias(String packageName, String alias) {
                return null;
            }

            @Override
            public KeySet getSigningKeySet(String packageName) {
                return null;
            }

            @Override
            public boolean isPackageSignedByKeySet(String packageName, KeySet ks) {
                return false;
            }

            @Override
            public boolean isPackageSignedByKeySetExactly(String packageName, KeySet ks) {
                return false;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        }

    }

    boolean isPackageAvailable(java.lang.String packageName, int userId) throws android.os.RemoteException;

    android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException;

    int getPackageUid(java.lang.String packageName, int userId) throws android.os.RemoteException;

    int[] getPackageGids(java.lang.String packageName) throws android.os.RemoteException;

    java.lang.String[] currentToCanonicalPackageNames(java.lang.String[] names) throws android.os.RemoteException;

    java.lang.String[] canonicalToCurrentPackageNames(java.lang.String[] names) throws android.os.RemoteException;

    android.content.pm.PermissionInfo getPermissionInfo(java.lang.String name, int flags) throws android.os.RemoteException;

    java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String group, int flags) throws android.os.RemoteException;

    android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String name, int flags) throws android.os.RemoteException;

    java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) throws android.os.RemoteException;

    android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException;

    android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName className, int flags, int userId) throws android.os.RemoteException;

    boolean activitySupportsIntent(android.content.ComponentName className, android.content.Intent intent, java.lang.String resolvedType) throws android.os.RemoteException;

    android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName className, int flags, int userId) throws android.os.RemoteException;

    android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName className, int flags, int userId) throws android.os.RemoteException;

    android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName className, int flags, int userId) throws android.os.RemoteException;

    int checkPermission(java.lang.String permName, java.lang.String pkgName) throws android.os.RemoteException;

    int checkUidPermission(java.lang.String permName, int uid) throws android.os.RemoteException;

    boolean addPermission(android.content.pm.PermissionInfo info) throws android.os.RemoteException;

    void removePermission(java.lang.String name) throws android.os.RemoteException;

    void grantPermission(java.lang.String packageName, java.lang.String permissionName) throws android.os.RemoteException;

    void revokePermission(java.lang.String packageName, java.lang.String permissionName) throws android.os.RemoteException;

    boolean isProtectedBroadcast(java.lang.String actionName) throws android.os.RemoteException;

    int checkSignatures(java.lang.String pkg1, java.lang.String pkg2) throws android.os.RemoteException;

    int checkUidSignatures(int uid1, int uid2) throws android.os.RemoteException;

    java.lang.String[] getPackagesForUid(int uid) throws android.os.RemoteException;

    java.lang.String getNameForUid(int uid) throws android.os.RemoteException;

    int getUidForSharedUser(java.lang.String sharedUserName) throws android.os.RemoteException;

    int getFlagsForUid(int uid) throws android.os.RemoteException;

    boolean isUidPrivileged(int uid) throws android.os.RemoteException;

    java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) throws android.os.RemoteException;

    android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    boolean canForwardTo(android.content.Intent intent, java.lang.String resolvedType, int sourceUserId, int targetUserId) throws android.os.RemoteException;

    java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    java.util.List<android.content.pm.ResolveInfo> queryIntentActivityOptions(android.content.ComponentName caller, android.content.Intent[] specifics, java.lang.String[] specificTypes, android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    java.util.List<android.content.pm.ResolveInfo> queryIntentReceivers(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    android.content.pm.ResolveInfo resolveService(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    java.util.List<android.content.pm.ResolveInfo> queryIntentContentProviders(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;

    /**
     * This implements getInstalledPackages via a "last returned row"
     * mechanism that is not exposed in the API. This is to get around the IPC
     * limit that kicks in when flags are included that bloat up the data
     * returned.
     */
    android.content.pm.ParceledListSlice getInstalledPackages(int flags, int userId) throws android.os.RemoteException;

    /**
     * This implements getPackagesHoldingPermissions via a "last returned row"
     * mechanism that is not exposed in the API. This is to get around the IPC
     * limit that kicks in when flags are included that bloat up the data
     * returned.
     */
    android.content.pm.ParceledListSlice getPackagesHoldingPermissions(java.lang.String[] permissions, int flags, int userId) throws android.os.RemoteException;

    /**
     * This implements getInstalledApplications via a "last returned row"
     * mechanism that is not exposed in the API. This is to get around the IPC
     * limit that kicks in when flags are included that bloat up the data
     * returned.
     */
    android.content.pm.ParceledListSlice getInstalledApplications(int flags, int userId) throws android.os.RemoteException;

    /**
     * Retrieve all applications that are marked as persistent.
     *
     * @return A List<applicationInfo> containing one entry for each persistent
     * application.
     */
    java.util.List<android.content.pm.ApplicationInfo> getPersistentApplications(int flags) throws android.os.RemoteException;

    android.content.pm.ProviderInfo resolveContentProvider(java.lang.String name, int flags, int userId) throws android.os.RemoteException;

    /**
     * Retrieve sync information for all content providers.
     *
     * @param outNames Filled in with a list of the root names of the content
     *                 providers that can sync.
     * @param outInfo  Filled in with a list of the ProviderInfo for each
     *                 name in 'outNames'.
     */
    void querySyncProviders(java.util.List<java.lang.String> outNames, java.util.List<android.content.pm.ProviderInfo> outInfo) throws android.os.RemoteException;

    java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, int flags) throws android.os.RemoteException;

    android.content.pm.InstrumentationInfo getInstrumentationInfo(android.content.ComponentName className, int flags) throws android.os.RemoteException;

    java.util.List<android.content.pm.InstrumentationInfo> queryInstrumentation(java.lang.String targetPackage, int flags) throws android.os.RemoteException;

    void installPackage(java.lang.String originPath, android.content.pm.IPackageInstallObserver2 observer, int flags, java.lang.String installerPackageName, android.content.pm.VerificationParams verificationParams, java.lang.String packageAbiOverride) throws android.os.RemoteException;

    void installPackageAsUser(java.lang.String originPath, android.content.pm.IPackageInstallObserver2 observer, int flags, java.lang.String installerPackageName, android.content.pm.VerificationParams verificationParams, java.lang.String packageAbiOverride, int userId) throws android.os.RemoteException;

    void finishPackageInstall(int token) throws android.os.RemoteException;

    void setInstallerPackageName(java.lang.String targetPackage, java.lang.String installerPackageName) throws android.os.RemoteException;

    /**
     * @deprecated rawr, don't call AIDL methods directly!
     */
    void deletePackageAsUser(java.lang.String packageName, android.content.pm.IPackageDeleteObserver observer, int userId, int flags) throws android.os.RemoteException;

    /**
     * Delete a package for a specific user.
     *
     * @param packageName The fully qualified name of the package to delete.
     * @param observer    a callback to use to notify when the package deletion in finished.
     * @param userId      the id of the user for whom to delete the package
     * @param flags       - possible values
     */
    void deletePackage(java.lang.String packageName, android.content.pm.IPackageDeleteObserver2 observer, int userId, int flags) throws android.os.RemoteException;

    java.lang.String getInstallerPackageName(java.lang.String packageName) throws android.os.RemoteException;

    void addPackageToPreferred(java.lang.String packageName) throws android.os.RemoteException;

    void removePackageFromPreferred(java.lang.String packageName) throws android.os.RemoteException;

    java.util.List<android.content.pm.PackageInfo> getPreferredPackages(int flags) throws android.os.RemoteException;

    void resetPreferredActivities(int userId) throws android.os.RemoteException;

    android.content.pm.ResolveInfo getLastChosenActivity(android.content.Intent intent, java.lang.String resolvedType, int flags) throws android.os.RemoteException;

    void setLastChosenActivity(android.content.Intent intent, java.lang.String resolvedType, int flags, android.content.IntentFilter filter, int match, android.content.ComponentName activity) throws android.os.RemoteException;

    void addPreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId) throws android.os.RemoteException;

    void replacePreferredActivity(android.content.IntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId) throws android.os.RemoteException;

    void clearPackagePreferredActivities(java.lang.String packageName) throws android.os.RemoteException;

    int getPreferredActivities(java.util.List<android.content.IntentFilter> outFilters, java.util.List<android.content.ComponentName> outActivities, java.lang.String packageName) throws android.os.RemoteException;

    void addPersistentPreferredActivity(android.content.IntentFilter filter, android.content.ComponentName activity, int userId) throws android.os.RemoteException;

    void clearPackagePersistentPreferredActivities(java.lang.String packageName, int userId) throws android.os.RemoteException;

    void addCrossProfileIntentFilter(android.content.IntentFilter intentFilter, java.lang.String ownerPackage, int ownerUserId, int sourceUserId, int targetUserId, int flags) throws android.os.RemoteException;

    void clearCrossProfileIntentFilters(int sourceUserId, java.lang.String ownerPackage, int ownerUserId) throws android.os.RemoteException;

    /**
     * Report the set of 'Home' activity candidates, plus (if any) which of them
     * is the current "always use this one" setting.
     */
    android.content.ComponentName getHomeActivities(java.util.List<android.content.pm.ResolveInfo> outHomeCandidates) throws android.os.RemoteException;

    /**
     * As per {@link android.content.pm.PackageManager#setComponentEnabledSetting}.
     */
    void setComponentEnabledSetting(android.content.ComponentName componentName, int newState, int flags, int userId) throws android.os.RemoteException;

    /**
     * As per {@link android.content.pm.PackageManager#getComponentEnabledSetting}.
     */
    int getComponentEnabledSetting(android.content.ComponentName componentName, int userId) throws android.os.RemoteException;

    /**
     * As per {@link android.content.pm.PackageManager#setApplicationEnabledSetting}.
     */
    void setApplicationEnabledSetting(java.lang.String packageName, int newState, int flags, int userId, java.lang.String callingPackage) throws android.os.RemoteException;

    /**
     * As per {@link android.content.pm.PackageManager#getApplicationEnabledSetting}.
     */
    int getApplicationEnabledSetting(java.lang.String packageName, int userId) throws android.os.RemoteException;

    /**
     * Set whether the given package should be considered stopped, making
     * it not visible to implicit intents that filter out stopped packages.
     */
    void setPackageStoppedState(java.lang.String packageName, boolean stopped, int userId) throws android.os.RemoteException;

    /**
     * Free storage by deleting LRU sorted list of cache files across
     * all applications. If the currently available free storage
     * on the device is greater than or equal to the requested
     * free storage, no cache files are cleared. If the currently
     * available storage on the device is less than the requested
     * free storage, some or all of the cache files across
     * all applications are deleted (based on last accessed time)
     * to increase the free storage space on the device to
     * the requested value. There is no guarantee that clearing all
     * the cache files from all applications will clear up
     * enough storage to achieve the desired value.
     *
     * @param freeStorageSize The number of bytes of storage to be
     *                        freed by the system. Say if freeStorageSize is XX,
     *                        and the current free storage is YY,
     *                        if XX is less than YY, just return. if not free XX-YY number
     *                        of bytes if possible.
     * @param observer        call back used to notify when
     *                        the operation is completed
     */
    void freeStorageAndNotify(long freeStorageSize, android.content.pm.IPackageDataObserver observer) throws android.os.RemoteException;

    /**
     * Free storage by deleting LRU sorted list of cache files across
     * all applications. If the currently available free storage
     * on the device is greater than or equal to the requested
     * free storage, no cache files are cleared. If the currently
     * available storage on the device is less than the requested
     * free storage, some or all of the cache files across
     * all applications are deleted (based on last accessed time)
     * to increase the free storage space on the device to
     * the requested value. There is no guarantee that clearing all
     * the cache files from all applications will clear up
     * enough storage to achieve the desired value.
     *
     * @param freeStorageSize The number of bytes of storage to be
     *                        freed by the system. Say if freeStorageSize is XX,
     *                        and the current free storage is YY,
     *                        if XX is less than YY, just return. if not free XX-YY number
     *                        of bytes if possible.
     * @param pi              IntentSender call back used to
     *                        notify when the operation is completed.May be null
     *                        to indicate that no call back is desired.
     */
    void freeStorage(long freeStorageSize, android.content.IntentSender pi) throws android.os.RemoteException;

    /**
     * Delete all the cache files in an applications cache directory
     *
     * @param packageName The package name of the application whose cache
     *                    files need to be deleted
     * @param observer    a callback used to notify when the deletion is finished.
     */
    void deleteApplicationCacheFiles(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) throws android.os.RemoteException;

    /**
     * Clear the user data directory of an application.
     *
     * @param packageName The package name of the application whose cache
     *                    files need to be deleted
     * @param observer    a callback used to notify when the operation is completed.
     */
    void clearApplicationUserData(java.lang.String packageName, android.content.pm.IPackageDataObserver observer, int userId) throws android.os.RemoteException;

    /**
     * Get package statistics including the code, data and cache size for
     * an already installed package
     *
     * @param packageName The package name of the application
     * @param userHandle  Which user the size should be retrieved for
     * @param observer    a callback to use to notify when the asynchronous
     *                    retrieval of information is complete.
     */
    void getPackageSizeInfo(java.lang.String packageName, int userHandle, android.content.pm.IPackageStatsObserver observer) throws android.os.RemoteException;

    /**
     * Get a list of shared libraries that are available on the
     * system.
     */
    java.lang.String[] getSystemSharedLibraryNames() throws android.os.RemoteException;

    /**
     * Get a list of features that are available on the
     * system.
     */
    android.content.pm.FeatureInfo[] getSystemAvailableFeatures() throws android.os.RemoteException;

    boolean hasSystemFeature(java.lang.String name) throws android.os.RemoteException;

    void enterSafeMode() throws android.os.RemoteException;

    boolean isSafeMode() throws android.os.RemoteException;

    void systemReady() throws android.os.RemoteException;

    boolean hasSystemUidErrors() throws android.os.RemoteException;

    /**
     * Ask the package manager to perform boot-time dex-opt of all
     * existing packages.
     */
    void performBootDexOpt() throws android.os.RemoteException;

    /**
     * Ask the package manager to perform dex-opt (if needed) on the given
     * package and for the given instruction set if it already hasn't done
     * so.
     * <p>
     * If the supplied instructionSet is null, the package manager will use
     * the packages default instruction set.
     * <p>
     * In most cases, apps are dexopted in advance and this function will
     * be a no-op.
     */
    boolean performDexOptIfNeeded(java.lang.String packageName, java.lang.String instructionSet) throws android.os.RemoteException;

    void forceDexOpt(java.lang.String packageName) throws android.os.RemoteException;

    /**
     * Update status of external media on the package manager to scan and
     * install packages installed on the external media. Like say the
     * MountService uses this to call into the package manager to update
     * status of sdcard.
     */
    void updateExternalMediaStatus(boolean mounted, boolean reportStatus) throws android.os.RemoteException;

    android.content.pm.PackageCleanItem nextPackageToClean(android.content.pm.PackageCleanItem lastPackage) throws android.os.RemoteException;

    void movePackage(java.lang.String packageName, android.content.pm.IPackageMoveObserver observer, int flags) throws android.os.RemoteException;

    boolean addPermissionAsync(android.content.pm.PermissionInfo info) throws android.os.RemoteException;

    boolean setInstallLocation(int loc) throws android.os.RemoteException;

    int getInstallLocation() throws android.os.RemoteException;

    int installExistingPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException;

    void verifyPendingInstall(int id, int verificationCode) throws android.os.RemoteException;

    void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) throws android.os.RemoteException;

    android.content.pm.VerifierDeviceIdentity getVerifierDeviceIdentity() throws android.os.RemoteException;

    boolean isFirstBoot() throws android.os.RemoteException;

    boolean isOnlyCoreApps() throws android.os.RemoteException;

    boolean isUpgrade() throws android.os.RemoteException;

    void setPermissionEnforced(java.lang.String permission, boolean enforced) throws android.os.RemoteException;

    boolean isPermissionEnforced(java.lang.String permission) throws android.os.RemoteException;

    /**
     * Reflects current DeviceStorageMonitorService state
     */
    boolean isStorageLow() throws android.os.RemoteException;

    boolean setApplicationHiddenSettingAsUser(java.lang.String packageName, boolean hidden, int userId) throws android.os.RemoteException;

    boolean getApplicationHiddenSettingAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException;

    android.content.pm.IPackageInstaller getPackageInstaller() throws android.os.RemoteException;

    boolean setBlockUninstallForUser(java.lang.String packageName, boolean blockUninstall, int userId) throws android.os.RemoteException;

    boolean getBlockUninstallForUser(java.lang.String packageName, int userId) throws android.os.RemoteException;

    android.content.pm.KeySet getKeySetByAlias(java.lang.String packageName, java.lang.String alias) throws android.os.RemoteException;

    android.content.pm.KeySet getSigningKeySet(java.lang.String packageName) throws android.os.RemoteException;

    boolean isPackageSignedByKeySet(java.lang.String packageName, android.content.pm.KeySet ks) throws android.os.RemoteException;

    boolean isPackageSignedByKeySetExactly(java.lang.String packageName, android.content.pm.KeySet ks) throws android.os.RemoteException;
}