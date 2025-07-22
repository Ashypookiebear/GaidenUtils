package me.remsq.gaidenUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GaidenUtilsCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public GaidenUtilsCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("gaidenutils.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /gaidenutils <reload|debug on|debug off>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "[GaidenUtils] Config reloaded.");
                return true;

            case "debug":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /gaidenutils debug <on|off>");
                    return true;
                }

                boolean enableDebug;
                if (args[1].equalsIgnoreCase("on")) {
                    enableDebug = true;
                } else if (args[1].equalsIgnoreCase("off")) {
                    enableDebug = false;
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid option. Use /gaidenutils debug <on|off>");
                    return true;
                }

                plugin.getConfig().set("sensory.debug", enableDebug);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.AQUA + "[GaidenUtils] Debug mode is now " + (enableDebug ? "ON" : "OFF"));
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
                return true;
        }
    }
}