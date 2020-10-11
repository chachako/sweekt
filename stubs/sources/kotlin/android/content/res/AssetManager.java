package android.content.res;

import java.io.IOException;
import java.io.InputStream;

public final class AssetManager {
    public final int addAssetPath(String path) {
        throw new UnsupportedOperationException("Stub!");
    }

    public void close() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String[] list(String path) throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public InputStream open(String fileName) throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public InputStream open(String fileName, int accessMode) throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public static AssetManager getSystem() {
        throw new UnsupportedOperationException("Stub!");
    }

    public String[] getApkPaths() {
        throw new UnsupportedOperationException("Stub!");
    }

    public int findCookieForPath(String path) {
        throw new UnsupportedOperationException("Stub!");
    }

    public AssetFileDescriptor openFd(String fileName) throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public InputStream openNonAsset(String fileName) throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public InputStream openNonAsset(String fileName, int accessMode)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public InputStream openNonAsset(int cookie, String fileName)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public InputStream openNonAsset(int cookie, String fileName, int accessMode)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public AssetFileDescriptor openNonAssetFd(String fileName)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public AssetFileDescriptor openNonAssetFd(int cookie, String fileName)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public XmlResourceParser openXmlResourceParser(String fileName)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public XmlResourceParser openXmlResourceParser(int cookie, String fileName)
            throws IOException {
        throw new UnsupportedOperationException("Stub!");
    }

    public String[] getLocales() {
        throw new UnsupportedOperationException("Stub!");
    }

    public void setConfiguration(int mcc, int mnc, String locale, int orientation,
                                 int touchscreen, int density, int keyboard, int keyboardHidden, int navigation,
                                 int screenWidth, int screenHeight, int smallestScreenWidthDp, int screenWidthDp,
                                 int screenHeightDp, int screenLayout, int uiMode, int colorMode, int majorVersion) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static native int getGlobalAssetCount();

    public static native String getAssetAllocations();

    public static native int getGlobalAssetManagerCount();
}
