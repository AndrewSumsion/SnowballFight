package me.El_Chupe.snowballfight;

import org.bukkit.Bukkit;

public class ReflectionUtils {
    private String versionString;
    private static ReflectionUtils instance;

    public static ReflectionUtils getInstance() {
        if(instance == null) {
            instance = new ReflectionUtils();
        }
        return instance;
    }

    private ReflectionUtils() {
        String[] packageStrings = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        versionString = packageStrings[packageStrings.length - 1];
    }

    private Class<?> getClass1(String path) throws ClassNotFoundException {
        String cbPrefix = "org.bukkit.craftbukkit.";
        String nmsPrefix = "net.minecraft.server.";
        if (path.startsWith(cbPrefix)) {
            return Class.forName(cbPrefix + versionString + path.substring(cbPrefix.length() - 1));
        } else if (path.startsWith(nmsPrefix)) {
            return Class.forName(nmsPrefix + versionString + path.substring(nmsPrefix.length() - 1));
        } else {
            throw new IllegalArgumentException("path must be craftbukkit or nms");
        }
    }

    public String getVersionString() {
        return versionString;
    }

    public static Class<?> getClass(String path) throws ClassNotFoundException {
        return getInstance().getClass1(path);
    }
}
