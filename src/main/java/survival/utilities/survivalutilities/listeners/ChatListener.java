package survival.utilities.survivalutilities.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import survival.utilities.survivalutilities.SurvivalUtilities;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        timeCheck:
        {
            final long now = System.currentTimeMillis() / 1000L;
            final long[] messages = data.get(new NamespacedKey(SurvivalUtilities.getInstance(), "chatHist"), PersistentDataType.LONG_ARRAY);
            final int messageDelay = 1;

            assert messages != null;
            if (now - messages[0] < messageDelay) {
                player.sendMessage(Component.text("You're chatting too fast!"));
                event.setCancelled(true);
                return;

            }
            data.set(new NamespacedKey(SurvivalUtilities.getInstance(), "lastChat"), PersistentDataType.LONG, now);
        }

        repeatCheck:
        {

        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        final long now = System.currentTimeMillis() / 1000L;
        player.getPersistentDataContainer().set(new NamespacedKey(SurvivalUtilities.getInstance(), "chatHist"), PersistentDataType.LONG_ARRAY, new long[]{now});
    }
}
