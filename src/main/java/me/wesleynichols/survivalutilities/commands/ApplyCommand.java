package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.commands.template.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;


public class ApplyCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        String formUrl = SurvivalUtilities.getInstance().getConfig().getString("apply-form");

        if (formUrl == null || formUrl.isEmpty()) {
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Application form URL is not set in the config.", NamedTextColor.RED)));
            return true;
        }

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookmeta = (BookMeta) book.getItemMeta();
        bookmeta.setAuthor("");
        bookmeta.setTitle("");

        TextComponent textComponent = Component.text("\n   Apply to Join\n\n", NamedTextColor.BLACK)
                .decoration(TextDecoration.BOLD, true)
                .append(Component.text(" ✧  ✦  ✧  ✪  ✧  ✦  ✧\n\n", NamedTextColor.DARK_PURPLE)
                        .decoration(TextDecoration.BOLD, false)
                        .append(Component.text("Staff will review your application as soon as possible - once approved you're ready to play!\n\n", NamedTextColor.BLACK)
                                .append(Component.text("  [Click to Apply]", NamedTextColor.DARK_AQUA)
                                        .decoration(TextDecoration.BOLD, true))))
                                        .clickEvent(ClickEvent.openUrl(formUrl));

        bookmeta.addPages(textComponent);
        book.setItemMeta(bookmeta);
        player.openBook(book);
        return true;
    }
}
