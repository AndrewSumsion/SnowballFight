package me.El_Chupe.snowballfight;

import me.El_Chupe.snowballfight.customitemhandlers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SnowballFight extends JavaPlugin implements Listener {
    private static SnowballFight INSTANCE;

    public static SnowballFight getInstance() {
        return INSTANCE;
    }
    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getPluginManager().registerEvents(new SnowballHandler(), this);
        Bukkit.getPluginManager().registerEvents(new GrenadeHandler(), this);
        Bukkit.getPluginManager().registerEvents(new SniperHandler(), this);
        Bukkit.getPluginManager().registerEvents(new MachineGunHandler(), this);
        Bukkit.getPluginManager().registerEvents(new ShotgunHandler(), this);

        Bukkit.getPluginManager().registerEvents(this, this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("snowball"))
        Utils.giveCustomItem((Player) sender, CustomItem.valueOf(args[0].toUpperCase()));
        return true;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        CustomItem customItem = Utils.getCustomItem(event.getItem());
        if(customItem == null) {
            return;
        }
        if(event.useItemInHand() != Event.Result.DENY) {
            event.setUseItemInHand(Event.Result.DENY);
            Bukkit.getPluginManager().callEvent(new ShootEvent(event.getPlayer(), customItem, event));
        }
    }
    @EventHandler
    public void testLeftClick(PlayerInteractEvent event) {
        if(event.getAction() != Action.LEFT_CLICK_AIR) return;
        Player player = event.getPlayer();
        Utils.relativeTeleport(player, player.getLocation().getDirection().setY(0).normalize().multiply(3), 0, 0);
    }
}
