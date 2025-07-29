package me.wesleynichols.survivalutilities.model;

import java.util.List;

public class ArmorStandPose {
    private boolean arms = true; // Default true if unspecified
    private List<Integer> bodyPose;
    private List<Integer> headPose;
    private List<Integer> leftLegPose;
    private List<Integer> rightLegPose;
    private List<Integer> leftArmPose;
    private List<Integer> rightArmPose;

    public ArmorStandPose() {}

    public boolean hasArms() { return arms; }
    public void setArms(boolean arms) { this.arms = arms; }

    public List<Integer> getBodyPose() { return bodyPose; }
    public void setBodyPose(List<Integer> bodyPose) { this.bodyPose = bodyPose; }

    public List<Integer> getHeadPose() { return headPose; }
    public void setHeadPose(List<Integer> headPose) { this.headPose = headPose; }

    public List<Integer> getLeftLegPose() { return leftLegPose; }
    public void setLeftLegPose(List<Integer> leftLegPose) { this.leftLegPose = leftLegPose; }

    public List<Integer> getRightLegPose() { return rightLegPose; }
    public void setRightLegPose(List<Integer> rightLegPose) { this.rightLegPose = rightLegPose; }

    public List<Integer> getLeftArmPose() { return leftArmPose; }
    public void setLeftArmPose(List<Integer> leftArmPose) { this.leftArmPose = leftArmPose; }

    public List<Integer> getRightArmPose() { return rightArmPose; }
    public void setRightArmPose(List<Integer> rightArmPose) { this.rightArmPose = rightArmPose; }
}
