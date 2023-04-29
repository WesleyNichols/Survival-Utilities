package me.wesleynichols.survivalutilities.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.wesleynichols.survivalutilities.managers.ChatManager.*;

public class ChatPlayer {

    private int numTimesMuted;
    List<Double> chatTimes;
    private final UUID playerUUID;

    public ChatPlayer(UUID uuid) {
        this.numTimesMuted = 0;
        this.chatTimes = new ArrayList<>();
        this.playerUUID = uuid;
    }

    public void checkThreshold() {
        double sumDiff = 0;
        for (int i = 0; i < maxMessages - 1; i++) {
            sumDiff += chatTimes.get(i + 1) - chatTimes.get(i);
        }
        if (sumDiff/4D < threshold) {
            // UUID and the time their "mute" ends
            preventChat.put(playerUUID, System.currentTimeMillis()/1000D + muteTime + numTimesMuted);
            numTimesMuted++;
        } else {
            chatTimes = new ArrayList<>();
        }
    }

    public boolean checkMuteTime(double timeLeft) {
        if (timeLeft < -threshold) {
            preventChat.remove(playerUUID);
            numTimesMuted = 0;
            chatTimes = new ArrayList<>();
            return false;
        } else if (numTimesMuted < 3 && timeLeft <= 0) {
            preventChat.put(playerUUID, System.currentTimeMillis()/1000D + muteTime + numTimesMuted);
            numTimesMuted++;
            return false;
        }
        return true;
    }

    public void checkMessage() {
        if (numTimesMuted > 0) return;
        double time = System.currentTimeMillis()/1000D;
        int size = chatTimes.size();
        if (size > 0 && time - chatTimes.get(size - 1) > resetArrThreshold) {
            chatTimes = new ArrayList<>();
        }
        chatTimes.add(time);
        if (size == maxMessages - 1) {
            checkThreshold();
        }
    }
}
