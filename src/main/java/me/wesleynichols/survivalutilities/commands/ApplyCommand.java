package me.wesleynichols.survivalutilities.commands;

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

    private static final String form = "https://forms.gle/8L98ueJkJ6Znscb19";

    /**
     Opens a book explaining how to apply to the server
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookmeta = (BookMeta) book.getItemMeta();
            bookmeta.setAuthor("");
            bookmeta.setTitle("");
            TextComponent textComponent = Component.text("\n   Apply to Join\n\n", NamedTextColor.BLACK)
                    .decoration(TextDecoration.BOLD, true)
                    .append(Component.text(" ✧  ✦  ✧  ✪  ✧  ✦  ✧\n\n", NamedTextColor.DARK_PURPLE)
                            .decoration(TextDecoration.BOLD, false)
                            .append(Component.text("Staff will review your application as soon as possible - once approved you're ready to play!\n\n", NamedTextColor.BLACK)
                                    .decoration(TextDecoration.BOLD, false)
                                    .append(Component.text("  [Click to Apply]", NamedTextColor.DARK_AQUA)
                                            .decoration(TextDecoration.BOLD, true)
                                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, form))
                                    )));
            bookmeta.addPages(textComponent);
            book.setItemMeta(bookmeta);
            player.openBook(book);
            return true;
        }
        return false;
    }
}
