package IAmNotJustJess.destroyTheMonument.commands;

import IAmNotJustJess.destroyTheMonument.gui.TestGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            TestGui.openTestGUI(player);
        }
        return false;
    }
}
