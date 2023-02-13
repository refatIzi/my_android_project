package com.example.testedit;

import android.content.Context;
import android.os.Build;

class Common
{

    static String getEngineRootDirectory(Context aContext)
    {
        //return Common.ensureStringEndsWithForwardslash((Objects.requireNonNull(aContext.getExternalFilesDir(null))).getAbsolutePath());
        return Common.ensureStringEndsWithForwardslash(aContext.getFilesDir().getPath());

    }

    /*
     * Determines if the device this application is running on is an emulator
     *
     * @return true if device is emulator, false otherwise
     */
    public static boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
              || Build.FINGERPRINT.startsWith("generic")
              || Build.FINGERPRINT.startsWith("unknown")
              || Build.HARDWARE.contains("goldfish")
              || Build.HARDWARE.contains("ranchu")
              || Build.MODEL.contains("google_sdk")
              || Build.MODEL.contains("Emulator")
              || Build.MODEL.contains("Android SDK built for x86")
              || Build.MANUFACTURER.contains("Genymotion")
              || Build.PRODUCT.contains("sdk_google")
              || Build.PRODUCT.contains("google_sdk")
              || Build.PRODUCT.contains("sdk")
              || Build.PRODUCT.contains("sdk_x86")
              || Build.PRODUCT.contains("sdk_gphone64_arm64")
              || Build.PRODUCT.contains("vbox86p")
              || Build.PRODUCT.contains("emulator")
              || Build.PRODUCT.contains("simulator");
    }

    static boolean is64bitProcessor()
    {
        // The SUPPORTED_ABIS is a String Array with all of the architectures the CPU supports, we are specifically looking to see
        // if this one supports 64bit
        return (Build.SUPPORTED_64_BIT_ABIS.length > 0);
    }

    static String ensureStringEndsWithForwardslash(String aString)
    {
        if (!aString.endsWith("/"))
        {
            return (aString + "/");
        }

        return aString;
    }

}
