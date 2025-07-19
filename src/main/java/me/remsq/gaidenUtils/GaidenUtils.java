package me.remsq.gaidenUtils;

import me.remsq.gaidenUtils.tabcompletors.DistanceTabCompleter;
import me.remsq.gaidenUtils.tabcompletors.GaidenReloadTabCompleter;
import me.remsq.gaidenUtils.tabcompletors.SensoryTabCompleter;
import me.remsq.gaidenUtils.tabcompletors.StatTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class GaidenUtils extends JavaPlugin {



    @Override
    public void onEnable() {

        saveDefaultConfig();

        StatManager statManager = new StatManager();

        getCommand("stamina").setExecutor(new StatCommand(statManager, "stamina"));
        getCommand("chakra").setExecutor(new StatCommand(statManager, "chakra"));
        getCommand("sensory").setExecutor(new SensoryCommand(this, statManager));
        getCommand("distance").setExecutor(new DistanceCommand(this));
        getCommand("gaidenutils").setExecutor(new GaidenUtilsCommand(this));

        getCommand("distance").setTabCompleter(new DistanceTabCompleter());
        getCommand("gaidenutils").setTabCompleter(new GaidenReloadTabCompleter());
        getCommand("stamina").setTabCompleter(new StatTabCompleter());
        getCommand("chakra").setTabCompleter(new StatTabCompleter());
        getCommand("sensory").setTabCompleter(new SensoryTabCompleter());

        System.out.println("GaidenUtils has loaded!");
    }

    @Override
    public void onDisable() {
        System.out.println("GaidenUtils has been disabled.");
    }


}
