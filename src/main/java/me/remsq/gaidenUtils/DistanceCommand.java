package me.remsq.gaidenUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class DistanceCommand implements CommandExecutor {
    private final GaidenUtils plugin;

    public DistanceCommand(GaidenUtils plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /distance <player1> <player2>");
            return true;
        }

        Player p1 = Bukkit.getPlayerExact(args[0]);
        Player p2 = Bukkit.getPlayerExact(args[1]);

        if (p1 == null || p2 == null) {
            sender.sendMessage(ChatColor.YELLOW + "One or both players are not online.");
            return true;
        }

        Location loc1 = p1.getLocation().getBlock().getLocation();
        Location loc2 = p2.getLocation().getBlock().getLocation();

        if (!loc1.getWorld().equals(loc2.getWorld())) {
            sender.sendMessage(ChatColor.RED + "Players are not in the same world.");
            return true;
        }

        double distance = loc1.distance(loc2);
        int maxDistance = plugin.getConfig().getInt("distance.max-distance", 100);

        if (distance > maxDistance) {
            sender.sendMessage(ChatColor.YELLOW + p1.getName() + " is too far from " + p2.getName() + " (limit: " + maxDistance + " blocks).");
        } else {
            sender.sendMessage(ChatColor.GREEN +  p1.getName() + ChatColor.YELLOW + " is " + (int) distance + ChatColor.GREEN + " blocks away from " + p2.getName());
        }

        return true;
    }
}
