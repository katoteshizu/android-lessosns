package utilities;

import android.util.Log;

/**
 * Created by Work on 2/4/2016.
 */
public class Utilities {
    public static void logE(final String TAG, final String message, Throwable throwable) {
        Log.e(TAG, getSafeLogMessage(message), throwable);
    }

    public static void logE(final String TAG, final String message) {
        Log.e(TAG, getSafeLogMessage(message));
    }

    public static void logW(final String TAG, final String message) {
        Log.w(TAG, getSafeLogMessage(message));
    }

    public static void logV(final String TAG, final String message) {
        Log.v(TAG, getSafeLogMessage(message));
    }

    public static void logD(final String TAG, final String message) {
        Log.d(TAG, getSafeLogMessage(message));
    }

    public static void logI(final String TAG, final String message) {
        Log.i(TAG, getSafeLogMessage(message));
    }

    private static String getSafeLogMessage(String message) {
        return message != null ? message : "";
    }
}
