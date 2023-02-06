package survival.utilities.survivalutilities.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import survival.utilities.survivalutilities.util.ArmorPoseUtil;

public class ArmorPoseListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof ArmorStand armorStand) {
            armorStand.addScoreboardTag("1");
        }
    }

    @EventHandler
    public void onArmorStandClick(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand armorStand) {
            Player player = event.getPlayer();
            if (!player.isSneaking()) return;

            if (!player.hasPermission("survivalutil.armorpose")) return;

            event.setCancelled(true);

            String pose = armorStand.getScoreboardTags().iterator().next();
            armorStand.removeScoreboardTag(pose);

            if (ArmorPoseUtil.isLastPose(pose)) {
                if (armorStand.getEquipment().getItemInMainHand().getType() == Material.AIR) {
                    pose = "1";
                } else {
                    pose = "2";
                }
            } else {
                pose = String.valueOf(Integer.parseInt(pose) + 1);
            }

            armorStand.addScoreboardTag(pose);
            ArmorPoseUtil.setPose(armorStand, pose);

            player.sendActionBar(Component.text(ArmorPoseUtil.getActionBarMessage() + pose));
        }
    }
}
