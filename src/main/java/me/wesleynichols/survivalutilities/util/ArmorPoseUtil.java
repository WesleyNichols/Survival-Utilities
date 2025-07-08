package me.wesleynichols.survivalutilities.util;

import io.papermc.paper.math.Rotations;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;

import java.util.List;

public class ArmorPoseUtil {

    public static FileConfiguration config;

    public static void setConfig(FileConfiguration configuration) {
        config = configuration;
    }

    public static boolean isLastPose(int poseNumber) {
        return !config.contains("armor.standPose." + (poseNumber + 1));
    }

    public static void setPose(ArmorStand armorStand, int poseNumber) {
        List<Integer> bodyPose = safePoseList(getBodyPose(poseNumber));
        List<Integer> headPose = safePoseList(getHeadPose(poseNumber));
        List<Integer> leftLegPose = safePoseList(getLeftLegPose(poseNumber));
        List<Integer> rightLegPose = safePoseList(getRightLegPose(poseNumber));

        if (getArms(poseNumber)) {
            List<Integer> leftArmPose = safePoseList(getLeftArmPose(poseNumber));
            List<Integer> rightArmPose = safePoseList(getRightArmPose(poseNumber));
            armorStand.setLeftArmRotations(Rotations.ofDegrees(leftArmPose.get(0), leftArmPose.get(1), leftArmPose.get(2)));
            armorStand.setRightArmRotations(Rotations.ofDegrees(rightArmPose.get(0), rightArmPose.get(1), rightArmPose.get(2)));
            armorStand.setArms(true);
        } else {
            // Default fallback rotations
            armorStand.setLeftArmRotations(Rotations.ofDegrees(339, 0, 346));
            armorStand.setRightArmRotations(Rotations.ofDegrees(339, 0, 15));
            armorStand.setArms(false);
        }

        armorStand.setBodyRotations(Rotations.ofDegrees(bodyPose.get(0), bodyPose.get(1), bodyPose.get(2)));
        armorStand.setHeadRotations(Rotations.ofDegrees(headPose.get(0), headPose.get(1), headPose.get(2)));
        armorStand.setLeftLegRotations(Rotations.ofDegrees(leftLegPose.get(0), leftLegPose.get(1), leftLegPose.get(2)));
        armorStand.setRightLegRotations(Rotations.ofDegrees(rightLegPose.get(0), rightLegPose.get(1), rightLegPose.get(2)));
    }

    public static boolean getArms(int poseNumber) {
        // If "arms" key doesn't exist, default to true
        return !config.contains("armor.standPose." + poseNumber + ".arms") ||
                config.getBoolean("armor.standPose." + poseNumber + ".arms");
    }

    private static List<Integer> safePoseList(List<Integer> list) {
        if (list == null || list.size() < 3) {
            return List.of(0, 0, 0);
        }
        return list;
    }

    public static List<Integer> getBodyPose(int poseNumber) {
        return config.getIntegerList("armor.standPose." + poseNumber + ".bodyPose");
    }

    public static List<Integer> getHeadPose(int poseNumber) {
        return config.getIntegerList("armor.standPose." + poseNumber + ".headPose");
    }

    public static List<Integer> getLeftLegPose(int poseNumber) {
        return config.getIntegerList("armor.standPose." + poseNumber + ".leftLegPose");
    }

    public static List<Integer> getRightLegPose(int poseNumber) {
        return config.getIntegerList("armor.standPose." + poseNumber + ".rightLegPose");
    }

    public static List<Integer> getLeftArmPose(int poseNumber) {
        return config.getIntegerList("armor.standPose." + poseNumber + ".leftArmPose");
    }

    public static List<Integer> getRightArmPose(int poseNumber) {
        return config.getIntegerList("armor.standPose." + poseNumber + ".rightArmPose");
    }
}
