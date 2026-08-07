public class BatteryProtectionUtil {
    /**
     * Resolves and caches the supported sysfs node path.
     */
    private static String getSupportedNode() {
        File node = new File("/sys/class/power_supply/battery/batt_slate_mode");
        if (node.exists() && node.canWrite()) {
            return "/sys/class/power_supply/battery/batt_slate_mode";
        }
        
        return null;
    }

    public static void setChargingEnabled(boolean enable) {
        String nodePath = getSupportedNode();
        if (nodePath == null) {
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