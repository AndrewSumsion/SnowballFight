package me.El_Chupe.snowballfight.customitemhandlers;

import me.El_Chupe.snowballfight.CustomItem;
import me.El_Chupe.snowballfight.ShootEvent;
import me.El_Chupe.snowballfight.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class SniperHandler implements Listener {
    public SniperHandler() {
    }

    @EventHandler
    public void onShoot(ShootEvent event) {
        if(event.getType() != CustomItem.SNIPER) return;
        Player player = event.getPlayer();
        Damageable entity = getTarget(event.getPlayer());
        if(entity != null) {
            if(entity.getType() == EntityType.PLAYER) return;
            double newHealth = entity.getHealth() - 18;
            if(newHealth < 0) newHealth = 0;
            entity.setHealth(newHealth);
            entity.damage(0, player);
            drawLine(player, entity.getLocation());
        } else {
            drawLine(player);
        }
    }

    private void drawLine(Player player, Location to) {

        Location eyeLocation = player.getEyeLocation();
        Vector direction = to.subtract(eyeLocation).toVector();
        direction = direction.normalize().multiply(new Vector(2, 2, 2));
        int i = 0;
        while(player.getWorld().getBlockAt(eyeLocation).getType() == Material.AIR || player.getWorld().getBlockAt(eyeLocation).getType() == Material.WATER || player.getWorld().getBlockAt(eyeLocation).getType() == Material.STATIONARY_WATER) {
            eyeLocation.add(direction);
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                sendParticlePacket(player1, eyeLocation);
            }
            if(i>99){
                break;
            }
            i++;
        }
    }

    private void drawLine(Player player) {

        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        direction = direction.normalize().multiply(new Vector(2, 2, 2));
        int i = 0;
        while(player.getWorld().getBlockAt(eyeLocation).getType() == Material.AIR) {
            eyeLocation.add(direction);
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                sendParticlePacket(player1, eyeLocation);
            }
            if(i>99){
                break;
            }
            i++;
        }
    }

    private void sendParticlePacket(Player player, Location location) {
        /*
        String versionString = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        Class<?> packetClass;
        Class<?> enumParticleClass;
        Class<?> craftPlayerClass;
        Class<?> entityPlayerClass;
        Class<?> playerConnectionClass;
        Class<?> genericPacketClass;
        try {
            packetClass = Class.forName("net.minecraft.server."+versionString+".PacketPlayOutWorldParticles");
            enumParticleClass = Class.forName("net.minecraft.server."+versionString+".EnumParticle");
            craftPlayerClass = Class.forName("org.bukkit.craftbukkit."+versionString+".entity.CraftPlayer");
            entityPlayerClass = Class.forName("net.minecraft.server."+versionString+".EntityPlayer");
            playerConnectionClass = Class.forName("net.minecraft.server."+versionString+".PlayerConnection");
            genericPacketClass = Class.forName("net.minecraft.server."+versionString+".Packet");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Field particleField;
        try {
            particleField = enumParticleClass.getField("REDSTONE");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        Object particle;
        try {
            particle = particleField.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        Constructor constructor;
        try {
            constructor = packetClass.getDeclaredConstructor(enumParticleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }

        Object packet;
        try {
            packet = constructor.newInstance(particle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), 0F, 0F, 1F, 0F, 0, new int[]{1});
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
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

        try {
            playerConnectionClass.getMethod("sendPacket", genericPacketClass).invoke(playerConnection, packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        */
        player.spawnParticle(Particle.SPELL_WITCH, location.getX(), location.getY(), location.getZ(), 0, 0, 0, 0, 1);
    }

    private Damageable getTarget(Player player) {
        Damageable result = null;
        double shortestDistance = 0;
        for(Entity entity : player.getNearbyEntities(100, 100, 100)) {
            if(!(entity instanceof Damageable)) continue;
            Vector toEntity = entity.getLocation().toVector().subtract(player.getLocation().toVector());
            Vector direction = player.getLocation().getDirection();
            double dot = toEntity.normalize().dot(direction);

            double distance = entity.getLocation().distance(player.getLocation());
            double accuracy;

            if(distance < 4) {
                accuracy = 1 - (1 / 120);
            } else {
                accuracy = 1 - (1 / (30 * distance));
            }


            if(dot > accuracy) {
                if((shortestDistance == 0 || shortestDistance > distance) && ((Damageable) entity).getHealth() > 0) {
                    shortestDistance = distance;
                    result = (Damageable) entity;
                }
            }
        }
        return result;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(Utils.getCustomItem(player.getInventory().getItemInMainHand()) != CustomItem.SNIPER) return;
        if(event.isSneaking() && !player.isFlying()) {
            player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
            // setWalkSpeed(event.getPlayer(), -10F);
            setWalkSpeed(player, -0.5F);
        } else if(player.getInventory().getHelmet() != null) if(player.getInventory().getHelmet().getType() == Material.PUMPKIN) {
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            setWalkSpeed(event.getPlayer(), 0.2F);
        }

    }
    private void setWalkSpeed(Player player, float speed) {
        if(speed > 1 || speed < -1) {
            setWalkSpeedReflection(player, speed);
            return;
        }
        player.setWalkSpeed(speed);
    }
    private void setWalkSpeedReflection(Player player, float speed) {
        String[] packageItems = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        String versionString = packageItems[packageItems.length - 1];
        Class<?> craftPlayerClass;
        Class<?> entityPlayerClass;
        Class<?> playerAbilitiesClass;
        try {
            craftPlayerClass = Class.forName(Bukkit.getServer().getClass().getPackage().getName()+".entity.CraftPlayer");
            entityPlayerClass = Class.forName("net.minecraft.server."+versionString+".EntityPlayer");
            playerAbilitiesClass = Class.forName("net.minecraft.server."+versionString+".PlayerAbilities");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Object craftPlayer = craftPlayerClass.cast(player);
        Object entityPlayer = null;
        try {
            entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object playerAbilities = null;
        try {
            playerAbilities = entityPlayerClass.getField("abilities").get(entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Field walkSpeedField = null;
        try {
            walkSpeedField = playerAbilitiesClass.getDeclaredField("walkSpeed");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            walkSpeedField.setFloat(playerAbilities, speed / 2f);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            entityPlayerClass.getMethod("updateAbilities");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
