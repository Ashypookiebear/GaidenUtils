package me.remsq.gaidenUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class StatCommand implements CommandExecutor {
    private final StatManager manager;
    private final String type;

    public StatCommand(StatManager manager, String type) {
        this.manager = manager;
        this.type = type.toLowerCase();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + type + " <set|minus|reset|check> [amount|player]");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "set" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "You must provide an amount.");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[1]);
                    manager.setStat(player.getUniqueId(), type, amount);
                    player.sendMessage(ChatColor.GREEN + "Your " + type + " has been set to " + amount + ".");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid number.");
                }
            }
            case "minus" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "You must provide an amount.");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[1]);
                    manager.minusStat(player.getUniqueId(), type, amount);
                    player.sendMessage(ChatColor.GREEN + "Removed " + amount + " from your " + type + ".");
                    player.sendMessage(ChatColor.GREEN + "Current " + type + ": " + manager.getCurrent(player.getUniqueId(), type));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid number.");
                }
            }
            case "reset" -> {
                manager.resetStat(player.getUniqueId(), type);
                player.sendMessage(ChatColor.GREEN + "Your " + type + " has been reset to " + manager.getOriginal(player.getUniqueId(), type) + ".");
            }
            case "check" -> {
                if (args.length >= 2) {
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(ChatColor.YELLOW + "That player is not online.");
                        return true;
                    }
                    int current = manager.getCurrent(target.getUniqueId(), type);
                    int original = manager.getOriginal(target.getUniqueId(), type);
                    player.sendMessage(ChatColor.GREEN +  target.getName() + "'s " + type + ": "+ ChatColor.RED + current + "/" + original);
                } else {
                    int current = manager.getCurrent(player.getUniqueId(), type);
                    int original = manager.getOriginal(player.getUniqueId(), type);
                    player.sendMessage(ChatColor.GREEN + "Your " + type + ": " + ChatColor.RED +  current + "/" + original);
                }
            }
            default -> player.sendMessage(ChatColor.RED + "Unknown subcommand.");
        }

        return true;
    }
}
