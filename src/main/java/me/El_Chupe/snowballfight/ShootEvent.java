package me.El_Chupe.snowballfight;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShootEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private Player player;
    private CustomItem type;
    private PlayerInteractEvent parentEvent;

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public ShootEvent(Player player, CustomItem type, PlayerInteractEvent parentEvent) {
        this.player = player;
        this.type = type;
        this.parentEvent = parentEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public CustomItem getType() {
        return type;
    }

    public PlayerInteractEvent getParentEvent() {
        return parentEvent;
    }
}
