package me.wesleynichols.survivalutilities.listeners;

import me.wesleynichols.survivalutilities.util.ArmorPoseUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorPoseListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if(event.getEntity() instanceof ArmorStand armorStand) {
            armorStand.addScoreboardTag("1");
        }
    }

    @EventHandler
    public void onArmorStandClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand armorStand) {
            Player player = event.getPlayer();
            if (!player.isSneaking() || !player.hasPermission("survivalutil.armorpose")) return;

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

            player.sendActionBar(Component.text("Pose: " + pose, NamedTextColor.GOLD));
        }
    }
}
