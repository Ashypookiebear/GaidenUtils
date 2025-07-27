package me.remsq.gaidenUtils.tabcompletors;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;


public class SensoryTabCompleter implements TabCompleter {
    private final Set<UUID>  warnedPlayers = new HashSet<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (!args[0].matches("\\d*")) {
                if (sender instanceof org.bukkit.entity.Player player) {
                    if (!warnedPlayers.contains(player.getUniqueId())) {
                        sender.sendMessage(ChatColor.RED + "You must type a number value.");
                        warnedPlayers.add(player.getUniqueId());
                    }
                }
                return Collections.emptyList();
            } else {
                if (sender instanceof org.bukkit.entity.Player player) {
                    warnedPlayers.remove(player.getUniqueId());
                }
            }

        }
        return Collections.emptyList();
    }
}
