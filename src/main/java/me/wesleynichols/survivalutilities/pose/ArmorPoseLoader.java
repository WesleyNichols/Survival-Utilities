package me.wesleynichols.survivalutilities.pose;

import me.wesleynichols.survivalutilities.model.ArmorStandPose;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArmorPoseLoader {

    /**
     * Loads all armor stand poses from the config section "armor.standPose"
     * Keyed by pose number (Integer).
     */
    public static Map<Integer, ArmorStandPose> loadPoses(FileConfiguration config) {
        Map<Integer, ArmorStandPose> poses = new HashMap<>();

        if (!config.contains("armor.standPose")) return poses;

        for (String key : Objects.requireNonNull(config.getConfigurationSection("armor.standPose")).getKeys(false)) {
            try {
                int poseNum = Integer.parseInt(key);
                ArmorStandPose pose = new ArmorStandPose();

                pose.setArms(config.getBoolean("armor.standPose." + key + ".arms", true));

                pose.setBodyPose(safeIntegerList(config.getIntegerList("armor.standPose." + key + ".bodyPose")));
                pose.setHeadPose(safeIntegerList(config.getIntegerList("armor.standPose." + key + ".headPose")));
                pose.setLeftLegPose(safeIntegerList(config.getIntegerList("armor.standPose." + key + ".leftLegPose")));
                pose.setRightLegPose(safeIntegerList(config.getIntegerList("armor.standPose." + key + ".rightLegPose")));
                pose.setLeftArmPose(safeIntegerList(config.getIntegerList("armor.standPose." + key + ".leftArmPose")));
                pose.setRightArmPose(safeIntegerList(config.getIntegerList("armor.standPose." + key + ".rightArmPose")));

                poses.put(poseNum, pose);
            } catch (NumberFormatException e) {
                // ignore invalid keys, optionally log warning
            }
        }

        return poses;
    }

    private static List<Integer> safeIntegerList(List<Integer> list) {
        if (list == null || list.size() < 3) {
            return List.of(0, 0, 0);
        }
        return list;
    }
}
