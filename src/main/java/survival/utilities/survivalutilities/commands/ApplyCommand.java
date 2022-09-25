package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;


public class ApplyCommand implements CommandExecutor {

    public static String getCommand = "apply";
    private static final String form = "https://forms.gle/q1oyyqkPhz9d12YZ7";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("apply")) {
            if (sender instanceof Player) {
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookmeta = (BookMeta) book.getItemMeta();
                bookmeta.setAuthor("");
                bookmeta.setTitle("");

                TextComponent textComponent = Component.text("\n   Apply to Join\n\n")
                        .color(NamedTextColor.BLACK)
                        .decoration(TextDecoration.BOLD, true)
                        .append(
                                Component.text(" ✧  ✦  ✧  ✪  ✧  ✦  ✧\n\n")
                                        .color(NamedTextColor.DARK_PURPLE)
                                        .decoration(TextDecoration.BOLD, false)
                                        .append(
                                                Component.text("Staff will review your application as soon as possible - once approved you're ready to play!\n\n")
                                                        .color(NamedTextColor.BLACK)
                                                        .decoration(TextDecoration.BOLD, false))
                                        .append(
                                                Component.text("  [Click to Apply]")
                                                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, form))
                                                        .color(NamedTextColor.DARK_AQUA)
                                                        .decoration(TextDecoration.BOLD, true)
                                        ));

                bookmeta.addPages(textComponent);
                book.setItemMeta(bookmeta);

                ((Player) sender).openBook(book);
            }
        }
        return true;
    }
}
