package setlocationplace.events;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import setlocationplace.data.ConfigManager;
import setlocationplace.data.Util;
import setlocationplace.data.Region;

public class QuiteEvent implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        ConfigManager config = new ConfigManager(player.getUniqueId().toString());

        String name = (String) config.getConfig().get("name");

        ConfigManager area = new ConfigManager(name);

        Location pos1 = (Location) area.getConfig().get("pos1");
        Location pos2 = (Location) area.getConfig().get("pos2");

        int min = area.getConfig().getInt("min");
        int max = area.getConfig().getInt("max");

        if(!(pos1 == null && pos2 == null)) {
            Region region = new Region(pos1, pos2);
            if(region.locationIsInRegion(player.getLocation())){
                if(min != 0){
                    area.getConfig().set("min", (int)area.getConfig().get("min") - 1);
                    config.saveConfig();
                    config.reloadConfig();
                    area.saveConfig();
                    area.reloadConfig();

                    Sign sign = Util.getSign(name);

                    if(sign != null){
                        sign.setLine(1, area.getConfig().get("min")  + "/" + area.getConfig().get("max") );
                        sign.update();
                    }
                    ArmorStand armorStand = Util.getArmorstand(name);
                    if(armorStand != null)
                    armorStand.setCustomName("§a" + area.getConfig().getInt("min") + "/" + area.getConfig().getInt("max"));

                }
            }
        }
    }
}
