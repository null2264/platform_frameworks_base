package com.android.server;

import android.util.Slog;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BatteryProtectionUtil {
    private static final String TAG = "BatteryProtectionUtil";
    private static String sSupportedNode = null;

    /**
     * Resolves and caches the supported sysfs node path.
     */
    private static String getSupportedNode() {
        if (sSupportedNode != null) {
            return sSupportedNode;
        }

        File node = new File("/sys/class/power_supply/battery/batt_slate_mode");
        if (node.exists() && node.canWrite()) {
            sSupportedNode = "/sys/class/power_supply/battery/batt_slate_mode";
        } else {
            sSupportedNode = "";
        }
        return sSupportedNode;
    }

    public static void setChargingEnabled(boolean enable) {
        String nodePath = getSupportedNode();
        if (nodePath.isEmpty()) {
            return;
        }

        String value = enable ? "0" : "1";

        try (FileWriter fw = new FileWriter(nodePath)) {
            fw.write(value);
            fw.flush();
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write to charging node: " + nodePath, e);
        }
    }
}