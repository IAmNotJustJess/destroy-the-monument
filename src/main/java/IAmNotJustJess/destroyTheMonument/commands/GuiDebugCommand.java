package IAmNotJustJess.destroyTheMonument.commands;

import IAmNotJustJess.destroyTheMonument.guis.TestGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuiDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(sender instanceof Player player) {
            TestGui.openTestGUI(player);
            return true;
        }
        return false;
    }
}
