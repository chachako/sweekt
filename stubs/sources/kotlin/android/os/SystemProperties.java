/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.os;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SystemProperties {
    public static final int PROP_NAME_MAX = Integer.MAX_VALUE;
    public static final int PROP_VALUE_MAX = 91;

    @NonNull
    public static String get(@NonNull String key) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Get the String value for the given {@code key}.
     *
     * @param key the key to lookup
     * @param def the default value in case the property is not set or empty
     * @return if the {@code key} isn't found, return {@code def} if it isn't null, or an empty
     * string otherwise
     */
    @NonNull
    public static String get(@NonNull String key, @Nullable String def) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static int getInt(@NonNull String key, int def) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static long getLong(@NonNull String key, long def) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static boolean getBoolean(@NonNull String key, boolean def) {
        throw new UnsupportedOperationException("Stub!");
    }


    public static void set(@NonNull String key, @Nullable String val) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void addChangeCallback(@NonNull Runnable callback) {
        throw new UnsupportedOperationException("Stub!");
    }


    public static void removeChangeCallback(@NonNull Runnable callback) {
        throw new UnsupportedOperationException("Stub!");
    }

    private static void callChangeCallbacks() {
        throw new UnsupportedOperationException("Stub!");
    }

    public static void reportSyspropChanged() {
        throw new UnsupportedOperationException("Stub!");
    }

    public static @NonNull
    String digestOf(@NonNull String... keys) {
        throw new UnsupportedOperationException("Stub!");
    }

    private SystemProperties() {
        throw new UnsupportedOperationException("Stub!");
    }

    @Nullable
    public static Handle find(@NonNull String name) {
        throw new UnsupportedOperationException("Stub!");
    }

    public static final class Handle {
        @NonNull public String get() {
            throw new UnsupportedOperationException("Stub!");
        }

        public int getInt(int def) {
            throw new UnsupportedOperationException("Stub!");
        }

        public long getLong(long def) {
            throw new UnsupportedOperationException("Stub!");
        }

        public boolean getBoolean(boolean def) {
            throw new UnsupportedOperationException("Stub!");
        }

        private Handle(long nativeHandle) {
            throw new UnsupportedOperationException("Stub!");
        }

    }
}