package survival.utilities.survivalutilities.util;

import io.papermc.paper.math.Rotations;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;

import java.util.List;
import java.util.Objects;

public class ArmorPoseUtil {

    public static FileConfiguration config;

    public static boolean isLastPose(String pose) {
        return !config.contains("armor.standPose." + (Integer.parseInt(pose)+1));
    }

    public static void setPose(ArmorStand armorStand, String pose) {
        List<Integer> bodyPose = getBodyPose(pose);
        List<Integer> headPose = getHeadPose(pose);
        List<Integer> leftLegPose = getLeftLegPose(pose);
        List<Integer> rightLegPose = getRightLegPose(pose);
        if (getArms(pose)) {
            List<Integer> leftArmPose = getLeftArmPose(pose);
            List<Integer> rightArmPose = getRightArmPose(pose);
            armorStand.setLeftArmRotations(Rotations.ofDegrees(leftArmPose.get(0), leftArmPose.get(1), leftArmPose.get(2)));
            armorStand.setRightArmRotations(Rotations.ofDegrees(rightArmPose.get(0), rightArmPose.get(1), rightArmPose.get(2)));
            armorStand.setArms(true);
        } else {
            armorStand.setLeftArmRotations(Rotations.ofDegrees(339, 0, 346));
            armorStand.setRightArmRotations(Rotations.ofDegrees(339, 0, 15));
            armorStand.setArms(false);
        }
        armorStand.setBodyRotations(Rotations.ofDegrees(bodyPose.get(0), bodyPose.get(1), bodyPose.get(2)));
        armorStand.setHeadRotations(Rotations.ofDegrees(headPose.get(0), headPose.get(1), headPose.get(2)));
        armorStand.setLeftLegRotations(Rotations.ofDegrees(leftLegPose.get(0), leftLegPose.get(1), leftLegPose.get(2)));
        armorStand.setRightLegRotations(Rotations.ofDegrees(rightLegPose.get(0), rightLegPose.get(1), rightLegPose.get(2)));
    }

    public static String getActionBarMessage() {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("messages.action-bar-message")));
    }

    public static boolean getArms(String pose) {
        return !config.contains("armor.standPose." + pose + ".arms") || config.getBoolean("armor.standPose." + pose + ".arms");
    }

    public static List<Integer> getBodyPose(String pose) {
        return config.getIntegerList("armor.standPose." + pose + ".bodyPose");
    }

    public static List<Integer> getHeadPose(String pose) {
        return config.getIntegerList("armor.standPose." + pose + ".headPose");
    }

    public static List<Integer> getLeftLegPose(String pose) {
        return config.getIntegerList("armor.standPose." + pose + ".leftLegPose");
    }

    public static List<Integer> getRightLegPose(String pose) {
        return config.getIntegerList("armor.standPose." + pose + ".rightLegPose");
    }

    public static List<Integer> getLeftArmPose(String pose) {
        return config.getIntegerList("armor.standPose." + pose + ".leftArmPose");
    }

    public static List<Integer> getRightArmPose(String pose) {
        return config.getIntegerList("armor.standPose." + pose + ".rightArmPose");
    }
}
