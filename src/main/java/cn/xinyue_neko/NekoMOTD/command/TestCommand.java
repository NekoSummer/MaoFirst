package cn.xinyue_neko.NekoMOTD.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("test")) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "FUCK YOU! MOJANG");
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "FUCK YOU! MICROSOFT");
            } else {
                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            }
        }
        return true;
    }
}
