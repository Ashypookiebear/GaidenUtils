package me.remsq.gaidenUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GaidenUtilsCommand implements CommandExecutor {
    private final GaidenUtils plugin;

    public GaidenUtilsCommand(GaidenUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("gaidenutils.reload")) {
                plugin.reloadConfig();
                sender.sendMessage("§aGaidenUtils config reloaded.");
            } else {
                sender.sendMessage("You do not have permission to reload the config.");
            }
            return true;
        }

        sender.sendMessage("§eUsage: /gaidenutils reload");
        return true;
    }
}
