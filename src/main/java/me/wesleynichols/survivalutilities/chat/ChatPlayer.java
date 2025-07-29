package me.wesleynichols.survivalutilities.chat;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class ChatPlayer {

    private final UUID uuid;
    private final ChatManager manager;
    private final Queue<Double> messageTimes = new LinkedList<>();

    private int muteCount = 0;

    public ChatPlayer(UUID uuid, ChatManager manager) {
        this.uuid = uuid;
        this.manager = manager;
    }

    public void recordMessage() {
        double now = System.currentTimeMillis() / 1000D;

        if (!messageTimes.isEmpty() && now - messageTimes.peek() > manager.getResetThreshold()) {
            messageTimes.clear(); // Reset if expired
        }

        messageTimes.add(now);

        if (messageTimes.size() >= manager.getMaxMessages()) {
            checkSpam();
        }
    }

    public void checkSpam() {
        Double[] times = messageTimes.toArray(new Double[0]);
        double sum = 0;

        for (int i = 0; i < times.length - 1; i++) {
            sum += times[i + 1] - times[i];
        }

        double avgDelay = sum / (times.length - 1);
        if (avgDelay < manager.getThreshold()) {
            manager.mutePlayer(uuid, muteCount++);
            messageTimes.clear();
        } else {
            messageTimes.poll(); // Slide window
        }
    }

    public boolean handleMuteExpiration(double timeLeft) {
        if (timeLeft < -manager.getThreshold()) {
            manager.unmutePlayer(uuid);
            muteCount = 0;
            messageTimes.clear();
            return false;
        } else if (muteCount < 3 && timeLeft <= 0) {
            manager.mutePlayer(uuid, muteCount++);
            return false;
        }

        return true; // Still muted
    }
}
