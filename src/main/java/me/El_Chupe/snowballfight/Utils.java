package me.El_Chupe.snowballfight;

import net.minecraft.server.v1_12_R1.PacketPlayOutPosition;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static String colorEncode(String s) {
        StringBuilder builder = new StringBuilder();
        for(char character : s.toCharArray()) {
            builder.append("§");
            builder.append(character);
        }
        return builder.toString();
    }
    public static CustomItem getCustomItem(ItemStack item) {
        if(item == null) return null;
        if(!item.hasItemMeta()) return null;
        if(!item.getItemMeta().hasLore()) return null;
        for(CustomItem customItem : CustomItem.values()) {
            if(item.getItemMeta().getLore().contains(colorEncode(customItem.name())) && item.getType() == customItem.getType().getItemType()) return customItem;
        }
        return null;
    }
    public static void giveCustomItem(Player player, CustomItem type) {
        player.getInventory().addItem(getCustomItemStack(type));
    }
    public static ItemStack getCustomItemStack(CustomItem type) {
        ItemStack item = type.getType().toItemStack(1);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(new ArrayList<String>(Arrays.asList(Utils.colorEncode(type.name()))));
        meta.setDisplayName(ChatColor.RESET+type.getDisplayName());
        item.setItemMeta(meta);
        return item;
    }
    public static double random(double min, double max) {
        return min + (Math.random() * ((max - min) + 1));
    }
    public static void relativeTeleport(Player player, Vector vec, float pitch, float yaw) {
        relativeTeleport(player, vec, pitch, yaw, TeleportFlags.values());
    }
    public static void relativeTeleport(Player player, Vector vec, float pitch, float yaw, TeleportFlags... flags) {
        Class<?> craftPlayerClass;
        Class<?> entityPlayerClass;
        Class<?> playerConnectionClass;

        try {
            craftPlayerClass = ReflectionUtils.getClass("org.bukkit.craftbukkit.entity.CraftPlayer");
            entityPlayerClass = ReflectionUtils.getClass("net.minecraft.server.EntityPlayer");
            playerConnectionClass = ReflectionUtils.getClass("net.minecraft.server.PlayerConnection");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Object craftPlayer = craftPlayerClass.cast(player);
        Object entityPlayer;
        try {
            entityPlayer = craftPlayerClass.getDeclaredMethod("getHandle").invoke(craftPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        Object playerConnection;
        try {
            playerConnection = entityPlayerClass.getDeclaredField("playerConnection").get(entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        Method teleportMethod;
        try {
            teleportMethod = playerConnectionClass.getDeclaredMethod("a",
                    double.class, double.class, double.class,
                    float.class, float.class,
                    Set.class,
                    PlayerTeleportEvent.TeleportCause.class
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }

        try {
            teleportMethod.invoke(playerConnection, vec.getX(), vec.getY(), vec.getZ(), yaw, pitch, /*toNmsFlags(flags)*/new HashSet<Object>(Arrays.asList(PacketPlayOutPosition.EnumPlayerTeleportFlags.values())), PlayerTeleportEvent.TeleportCause.PLUGIN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
    }
    private static Set<Object> toNmsFlags(TeleportFlags... flags) {
        Class<?> enumPlayerTeleportFlagsClass = null;
        try {
            Class<?> packetClass = ReflectionUtils.getClass("net.minecraft.server.PacketPlayOutPosition");
            for(Class<?> clazz :packetClass.getDeclaredClasses()) {
                if(clazz.getName().equals("EnumPlayerTeleportFlags")) {
                    enumPlayerTeleportFlagsClass = clazz;
                    break;
                }
                if(enumPlayerTeleportFlagsClass == null) return null;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Method valueOfMethod;
        try {
            valueOfMethod = enumPlayerTeleportFlagsClass.getDeclaredMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        Set<Object> result = new HashSet<Object>();
        for(TeleportFlags flag : flags) {
            try {
                result.add(valueOfMethod.invoke(null, flag.name()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public enum TeleportFlags {
        X,
        Y,
        Z,
        Y_ROT,
        X_ROT
    }
}
