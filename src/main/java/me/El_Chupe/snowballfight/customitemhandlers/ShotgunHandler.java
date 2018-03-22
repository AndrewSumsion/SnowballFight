package me.El_Chupe.snowballfight.customitemhandlers;

import me.El_Chupe.snowballfight.CustomItem;
import me.El_Chupe.snowballfight.ShootEvent;
import me.El_Chupe.snowballfight.Utils;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class ShotgunHandler implements Listener {
    public ShotgunHandler() {
    }

    @EventHandler
    public void onShoot(ShootEvent event) {
        if(event.getType() != CustomItem.SHOTGUN) return;
        for(int i=0;i<14;i++) {
            Vector direction = event.getPlayer().getEyeLocation().getDirection().normalize().multiply(35);
            direction.add(new Vector(Utils.random(-1, 1), Utils.random(-1, 1), Utils.random(-1, 1)));
            event.getPlayer().launchProjectile(Snowball.class, direction.normalize().multiply(1.5));
        }
    }
}
