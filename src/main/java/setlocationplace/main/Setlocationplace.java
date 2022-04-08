package setlocationplace.main;

import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import setlocationplace.cmd.cmd;
import setlocationplace.events.*;

import java.util.Arrays;

public final class Setlocationplace extends JavaPlugin implements Listener {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("setplace").setExecutor(new cmd());
        Listener[] events = {new EditSign(), new BlockClick(),new MoveEvent(),new QuiteEvent(), new JoinEvent()};
        PluginManager pm = Bukkit.getPluginManager();
        Arrays.stream(events).forEach(classes->{pm.registerEvents(classes,this);});
    }

    @Override
    public void onDisable() {
    }
}
