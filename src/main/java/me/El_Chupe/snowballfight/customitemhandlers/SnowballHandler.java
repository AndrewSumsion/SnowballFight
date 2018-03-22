package me.El_Chupe.snowballfight.customitemhandlers;

import me.El_Chupe.snowballfight.CustomItem;
import me.El_Chupe.snowballfight.ShootEvent;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SnowballHandler implements Listener {
    public SnowballHandler() {
    }

    @EventHandler
    public void onBlockRightClick(ShootEvent event) {
        CustomItem customItem = event.getType();
        if(customItem != CustomItem.SNOWBALL) return;
        event.getParentEvent().setCancelled(true);
        event.getPlayer().launchProjectile(Snowball.class);
    }
}
