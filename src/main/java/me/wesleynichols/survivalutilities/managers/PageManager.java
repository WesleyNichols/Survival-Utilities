package me.wesleynichols.survivalutilities.managers;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class PageManager {

    private static final int COMMANDS_PER_PAGE = 8;

    private static final String NAV_BACK_TEMPLATE =
            "<green><click:run_command:/help %d><hover:show_text:'<grey>Click to go to page %d'> << </hover></click></green>";

    private static final String NAV_NEXT_TEMPLATE =
            "<green><click:run_command:/help %d><hover:show_text:'<grey>Click to go to page %d'> >> </hover></click></green>";

    private static final List<Component> ERROR_PAGE = List.of(
            SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("That page doesn't exist!", NamedTextColor.RED))
    );

    /**
     * Returns a formatted list of components representing a help page.
     *
     * @param pageNumber Page to display (1-indexed).
     * @param configKey  Key in the config containing the help entries.
     * @return List of components for the specified page, or an error page if invalid.
     */
    public static List<Component> getPage(int pageNumber, String configKey) {
        List<String> rawLines = SurvivalUtilities.getInstance().getConfig().getStringList(configKey);

        if (rawLines.size() < 2) {
            return ERROR_PAGE; // Require at least header + footer
        }

        int contentLineCount = rawLines.size() - 2; // exclude header and footer
        int maxPages = (int) Math.ceil(contentLineCount / (double) COMMANDS_PER_PAGE);

        if (pageNumber < 1 || pageNumber > maxPages) {
            return ERROR_PAGE;
        }

        String rawHeader = rawLines.getFirst();
        String rawFooter = rawLines.getLast();
        List<String> contentLines = rawLines.subList(1, rawLines.size() - 1);

        Component header = MiniMessage.miniMessage().deserialize(
                buildHeader(rawHeader, pageNumber, maxPages)
        );
        List<Component> content = parseContentLines(contentLines, pageNumber);
        Component footer = deserializeLine(rawFooter);

        List<Component> result = new ArrayList<>();
        result.add(header);
        result.addAll(content);
        result.add(footer);
        return result;
    }

    public static List<Component> getPageFromList(List<Component> lines, int pageNumber, String title, String baseCommand) {
        final int totalPages = (int) Math.ceil(lines.size() / (double) COMMANDS_PER_PAGE);
        if (pageNumber < 1 || pageNumber > totalPages) {
            return List.of(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("That page doesn't exist!", NamedTextColor.RED)));
        }

        int start = (pageNumber - 1) * COMMANDS_PER_PAGE;
        int end = Math.min(start + COMMANDS_PER_PAGE, lines.size());

        List<Component> components = new ArrayList<>();

        // Build header with navigation
        String back = (pageNumber > 1)
                ? String.format(NAV_BACK_TEMPLATE, pageNumber - 1, pageNumber - 1).replace("/help", "/" + baseCommand)
                : "---";
        String next = (pageNumber < totalPages)
                ? String.format(NAV_NEXT_TEMPLATE, pageNumber + 1, pageNumber + 1).replace("/help", "/" + baseCommand)
                : "---";

        String headerMini = "<gold>" + title + " (Page " + pageNumber + "/" + totalPages + ") " + back + " <gray>|</gray> " + next;
        components.add(MiniMessage.miniMessage().deserialize(headerMini));

        // Add content
        for (int i = start; i < end; i++) {
            components.add(lines.get(i));
        }

        return components;
    }

    private static String buildHeader(String headerTemplate, int currentPage, int maxPages) {
        String backButton = (currentPage > 1)
                ? String.format(NAV_BACK_TEMPLATE, currentPage - 1, currentPage - 1)
                : "---";

        String nextButton = (currentPage < maxPages)
                ? String.format(NAV_NEXT_TEMPLATE, currentPage + 1, currentPage + 1)
                : "---";

        return headerTemplate.replace(" << ", backButton).replace(" >> ", nextButton);
    }

    private static List<Component> parseContentLines(List<String> contentLines, int pageNumber) {
        List<Component> pageContent = new ArrayList<>();

        int startIndex = (pageNumber - 1) * COMMANDS_PER_PAGE;
        int endIndex = Math.min(startIndex + COMMANDS_PER_PAGE, contentLines.size());

        for (int i = startIndex; i < endIndex; i++) {
            pageContent.add(deserializeLine(contentLines.get(i)));
        }

        return pageContent;
    }

    private static Component deserializeLine(String line) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(line);
    }

}
