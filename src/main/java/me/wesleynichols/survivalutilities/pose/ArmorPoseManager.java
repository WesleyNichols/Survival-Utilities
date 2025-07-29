package me.wesleynichols.survivalutilities.pose;

import io.papermc.paper.math.Rotations;
import me.wesleynichols.survivalutilities.model.ArmorStandPose;
import org.bukkit.entity.ArmorStand;

import java.util.List;
import java.util.Map;

public class ArmorPoseManager {

    private static Map<Integer, ArmorStandPose> poses;

    /**
     * Set all loaded poses for use.
     */
    public static void setPoses(Map<Integer, ArmorStandPose> poseMap) {
        poses = poseMap;
    }

    /**
     * Returns true if there is no pose for the next pose number.
     */
    public static boolean isLastPose(int poseNumber) {
        return poses == null || !poses.containsKey(poseNumber + 1);
    }

    /**
     * Applies the given pose number to the armor stand.
     */
    public static void setPose(ArmorStand armorStand, int poseNumber) {
        if (poses == null || !poses.containsKey(poseNumber)) {
            return; // no pose to apply
        }

        ArmorStandPose pose = poses.get(poseNumber);

        List<Integer> bodyPose = pose.getBodyPose();
        List<Integer> headPose = pose.getHeadPose();
        List<Integer> leftLegPose = pose.getLeftLegPose();
        List<Integer> rightLegPose = pose.getRightLegPose();

        if (pose.hasArms()) {
            List<Integer> leftArmPose = pose.getLeftArmPose();
            List<Integer> rightArmPose = pose.getRightArmPose();

            armorStand.setLeftArmRotations(Rotations.ofDegrees(
                    leftArmPose.get(0), leftArmPose.get(1), leftArmPose.get(2)));

            armorStand.setRightArmRotations(Rotations.ofDegrees(
                    rightArmPose.get(0), rightArmPose.get(1), rightArmPose.get(2)));

            armorStand.setArms(true);
        } else {
            // Default fallback rotations if arms disabled
            armorStand.setLeftArmRotations(Rotations.ofDegrees(339, 0, 346));
            armorStand.setRightArmRotations(Rotations.ofDegrees(339, 0, 15));
            armorStand.setArms(false);
        }

        armorStand.setBodyRotations(Rotations.ofDegrees(
                bodyPose.get(0), bodyPose.get(1), bodyPose.get(2)));

        armorStand.setHeadRotations(Rotations.ofDegrees(
                headPose.get(0), headPose.get(1), headPose.get(2)));

        armorStand.setLeftLegRotations(Rotations.ofDegrees(
                leftLegPose.get(0), leftLegPose.get(1), leftLegPose.get(2)));

        armorStand.setRightLegRotations(Rotations.ofDegrees(
                rightLegPose.get(0), rightLegPose.get(1), rightLegPose.get(2)));
    }
}
