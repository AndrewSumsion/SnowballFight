package me.El_Chupe.snowballfight;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public enum CustomItem {
    SNOWBALL(new MaterialData(Material.SNOW_BALL), "Snowball"),
    SHOTGUN(new MaterialData(Material.CLAY_BALL), "Snowball Shotgun"),
    GRENADE(new MaterialData(Material.SLIME_BALL), "Snow Grenade Launcher"),
    SNIPER(new MaterialData(Material.INK_SACK, (byte)12), "Snowball Sniper"),
    MACHINE_GUN(new MaterialData(Material.CLAY_BRICK), "Automatic Snowball Launcher");

    /****************************************************************************************/

    private MaterialData material;
    private String displayName;

    CustomItem(MaterialData material, String displayName) {
        this.material = material;
        this.displayName = displayName;
    }

    MaterialData getType() {
        return material;
    }

    String getDisplayName() {
        return displayName;
    }
}
