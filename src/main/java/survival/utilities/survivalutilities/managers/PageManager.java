package survival.utilities.survivalutilities.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageManager {

    public static int commandsPerPage = 8;
    private static final String miniTextBack = "<green><click:run_command:/help num><hover:show_text:'<grey>Click to go to page num'> << </hover></click></green>";
    private static final String miniTextNext = "<green><click:run_command:/help num><hover:show_text:'<grey>Click to go to page num'> >> </hover></click></green>";
    public static String chatPrefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "BeeBox" + ChatColor.GOLD + "]" + ChatColor.YELLOW + " > " + ChatColor.WHITE;

    private static final List<Component> errorMessage = new ArrayList<>(Collections.singleton(Component.text(chatPrefix + ChatColor.RED + "That page doesn't exist!")));

    public static List<Component> getPage(int pageNumber, String configName) {
        if (pageNumber < 1) {
            return errorMessage;
        }
        List<String> linesStr = SurvivalUtilities.getInstance().getConfig().getStringList(configName);
        if (commandsPerPage * pageNumber > linesStr.size() + commandsPerPage - 2) {
            return errorMessage;
        }
        List<Component> lines = new ArrayList<>();
        String header = linesStr.get(0);
        if (header.contains(" << ") && header.contains(" >> ")) {
            header = pageNumber > 1 ? header.replace(" << ", miniTextBack.replaceAll("num",
                    String.valueOf(pageNumber - 1))) : header.replace(" << ", "---");

            header = pageNumber < (linesStr.size() - 3)/commandsPerPage + 1 ? header.replace(" >> ",
                    miniTextNext.replace("num", String.valueOf(pageNumber + 1))) :
                    header.replace(" >> ", "---");
        }
        Component headerComp = MiniMessage.miniMessage().deserialize(header);
        for (String s : linesStr) {
            lines.add(LegacyComponentSerializer.legacyAmpersand().deserialize(s));
        }
        List<Component> finalLines = new ArrayList<>(Collections.singleton(headerComp));
        finalLines.addAll(lines.subList(commandsPerPage * (pageNumber - 1) + 1, Math.min((commandsPerPage * pageNumber) + 1, lines.size() - 1)));
        finalLines.add(lines.get(lines.size() - 1));
        return finalLines;
    }
}
