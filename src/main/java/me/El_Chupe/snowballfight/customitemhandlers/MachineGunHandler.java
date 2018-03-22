package me.El_Chupe.snowballfight.customitemhandlers;

import me.El_Chupe.snowballfight.CustomItem;
import me.El_Chupe.snowballfight.ShootEvent;
import me.El_Chupe.snowballfight.SnowballFight;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class MachineGunHandler implements Listener {
    public MachineGunHandler() {
    }

    @EventHandler
    public void onShoot(ShootEvent event) {
        if(event.getType() != CustomItem.MACHINE_GUN) return;
        final Player player = event.getPlayer();
        final BukkitTask task = Bukkit.getScheduler().runTaskTimer(SnowballFight.getInstance(), new Runnable() {
            public void run() {
                player.launchProjectile(Snowball.class, player.getEyeLocation().getDirection().normalize().multiply(2));
            }
        }, 0, 1);
        Bukkit.getScheduler().runTaskLater(SnowballFight.getInstance(), new Runnable() {
            public void run() {
                task.cancel();
            }
        }, 4);
    }
}
