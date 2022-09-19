package survival.utilities.survivalutilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ApplicationBook implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("apply")) {
            if (sender instanceof Player) {
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookmeta = (BookMeta) book.getItemMeta();
                bookmeta.setTitle("Application");
                bookmeta.setAuthor("Survival");

                ArrayList<String> pages = new ArrayList<String>();

                pages.add(ChatColor.DARK_GREEN + "This is a test\n" + ChatColor.LIGHT_PURPLE + "123 testing");
                bookmeta.setPages(pages);

                book.setItemMeta(bookmeta);

                ((Player) sender).openBook(book);
            }
        }
        return false;
    }
}
