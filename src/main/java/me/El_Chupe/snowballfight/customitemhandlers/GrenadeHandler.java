package me.El_Chupe.snowballfight.customitemhandlers;

import me.El_Chupe.snowballfight.CustomItem;
import me.El_Chupe.snowballfight.ShootEvent;
import me.El_Chupe.snowballfight.SnowballFight;
import me.El_Chupe.snowballfight.Utils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GrenadeHandler implements Listener {
    public GrenadeHandler() {
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if(!entity.hasMetadata("Grenade")) return;
        if(event.getHitEntity() != null) {
            if(event.getHitEntity() instanceof Damageable) {
                ((Damageable) event.getHitEntity()).damage(10);
            }
        }
        event.getEntity().getWorld().createExplosion(
                event.getEntity().getLocation().getX(),
                event.getEntity().getLocation().getY(),
                event.getEntity().getLocation().getZ(),
                2.75F,
                false,
                false
        );
        List<Vector> vectors = new ArrayList<Vector>();
        for(int i=0; i < 99; i++) {
            double x, y, z;
            x = Utils.random(-10, 10);
            z = Utils.random(-10, 10);
            y = Utils.random(1, 20);
            vectors.add(new Vector(x, y, z).normalize());
        }
        for(Vector vector : vectors) {
            Entity snowball = event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation().add(-0.5 + (Math.random() * ((0.5 - -0.5) + 1)), 0, -0.5 + (Math.random() * ((0.5 - -0.5) + 1))), EntityType.SNOWBALL);
            snowball.setVelocity(vector);
            snowball.setMetadata("Snowball", new FixedMetadataValue(SnowballFight.getInstance(), true));
            snowball.setFireTicks(200);
        }
    }

    @EventHandler
    private void onRightClick(ShootEvent event) {
        CustomItem customItem = event.getType();
        if(customItem != CustomItem.GRENADE) return;
        event.getPlayer().launchProjectile(Snowball.class).setMetadata("Grenade", new FixedMetadataValue(SnowballFight.getInstance(), true));
    }

}
