package me.remsq.gaidenUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.UUID;

public class SensoryCommand implements CommandExecutor {
    private final StatManager statManager;
    private final GaidenUtils plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public SensoryCommand(GaidenUtils plugin, StatManager statManager) {
        this.plugin = plugin;
        this.statManager = statManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /sensory <distance>");
            return true;
        }

        long now = System.currentTimeMillis();
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        int cooldownSeconds = plugin.getConfig().getInt("sensory.cooldown-seconds", 10);
        if (now - lastUsed < cooldownSeconds * 1000L) {
            long secondsLeft = (cooldownSeconds * 1000L - (now - lastUsed)) / 1000;
            player.sendMessage(ChatColor.RED + "You must wait " + secondsLeft + " seconds to use sensory again.");
            return true;
        }

        int inputDistance;
        try {
            inputDistance = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid number.");
            return true;
        }

        int maxDistance = getMaxDistance(player);
        if (inputDistance > maxDistance) {
            player.sendMessage(ChatColor.RED + "You can only sense up to " + maxDistance + " blocks.");
            return true;
        }

        Player closest = null;
        double closestDistance = Double.MAX_VALUE;
        Location origin = player.getLocation();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(player)) continue;

            double distance = target.getLocation().distance(origin);
            if (distance <= inputDistance && distance < closestDistance) {
                closest = target;
                closestDistance = distance;
            }
        }

        if (closest == null) {
            player.sendMessage(ChatColor.YELLOW + "You don't sense anyone nearby.");
            return true;
        }

        String direction = getDirectionTo(origin, closest.getLocation());
        int chakra = statManager.getCurrent(closest.getUniqueId(), "chakra");

        player.sendMessage(ChatColor.DARK_PURPLE + "You sense someone " + direction +
                ChatColor.DARK_PURPLE + ", their chakra is " +
                ChatColor.DARK_AQUA + chakra + ChatColor.DARK_PURPLE + ".");

        boolean showDistance = plugin.getConfig().getBoolean("sensory.show-distance", true);
        if (showDistance) {
            player.sendMessage(ChatColor.GRAY + "(Distance: " + (int) closestDistance + " blocks)");
        }

        cooldowns.put(player.getUniqueId(), now);
        return true;
    }

    private int getMaxDistance(Player player) {
        FileConfiguration config = plugin.getConfig();

        if (player.hasPermission("gaidenutils.sensory.master")) {
            return config.getInt("sensory.max-distance-permission.master", 300);
        } else if (player.hasPermission("gaidenutils.sensory.intermediate")) {
            return config.getInt("sensory.max-distance-permission.intermediate", 200);
        } else if (player.hasPermission("gaidenutils.sensory.novice")) {
            return config.getInt("sensory.max-distance-permission.novice", 100);
        }

        return 0;
    }

    private String getDirectionTo(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double angle = Math.toDegrees(Math.atan2(-dx, dz));
        if (angle < 0) angle += 360;

        if (angle >= 337.5 || angle < 22.5) return (ChatColor.WHITE + "South");
        if (angle < 67.5) return (ChatColor.WHITE + "Southwest");
        if (angle < 112.5) return (ChatColor.WHITE + "West");
        if (angle < 157.5) return (ChatColor.WHITE + "Northwest");
        if (angle < 202.5) return (ChatColor.WHITE + "North");
        if (angle < 247.5) return (ChatColor.WHITE + "Northeast");
        if (angle < 292.5) return (ChatColor.WHITE + "East");
        return (ChatColor.WHITE + "Southeast");
    }
}
