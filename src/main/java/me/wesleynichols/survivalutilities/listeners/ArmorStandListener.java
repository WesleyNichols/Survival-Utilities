package me.wesleynichols.survivalutilities.listeners;

import me.wesleynichols.survivalutilities.pose.ArmorPoseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Optional;

public class ArmorStandListener implements Listener {

    private static final String POSE_TAG_PREFIX = "pose:";

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            // Initialize pose tag to "1" when armor stand spawns
            armorStand.addScoreboardTag(POSE_TAG_PREFIX + "1");
        }
    }

    @EventHandler
    public void onArmorStandClick(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand armorStand)) return;

        Player player = event.getPlayer();

        // Only proceed if sneaking and has permission
        if (!player.isSneaking() || !player.hasPermission("survivalutil.armorpose")) return;

        event.setCancelled(true);

        // Get current pose number from scoreboard tags
        Optional<String> currentPoseTag = armorStand.getScoreboardTags()
                .stream()
                .filter(tag -> tag.startsWith(POSE_TAG_PREFIX))
                .findFirst();

        int currentPose = currentPoseTag
                .map(tag -> {
                    try {
                        return Integer.parseInt(tag.substring(POSE_TAG_PREFIX.length()));
                    } catch (NumberFormatException e) {
                        return 1; // Default to pose 1 if corrupted
                    }
                })
                .orElse(1);

        // Remove old pose tag
        currentPoseTag.ifPresent(armorStand::removeScoreboardTag);

        // Determine next pose
        int nextPose;
        if (ArmorPoseManager.isLastPose(currentPose)) {
            // Reset based on whether hands are empty or not
            nextPose = armorStand.getEquipment().getItemInMainHand().getType() == Material.AIR ? 1 : 2;
        } else {
            nextPose = currentPose + 1;
        }

        // Add new pose tag
        armorStand.addScoreboardTag(POSE_TAG_PREFIX + nextPose);

        // Apply the pose
        ArmorPoseManager.setPose(armorStand, nextPose);

        // Notify player
        player.sendActionBar(Component.text("Pose: " + nextPose, NamedTextColor.GOLD));
    }
}
