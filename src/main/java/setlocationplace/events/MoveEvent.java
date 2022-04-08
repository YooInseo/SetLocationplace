package setlocationplace.events;


import org.bukkit.Location;

import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import setlocationplace.data.ConfigManager;
import setlocationplace.data.Util;
import setlocationplace.data.Region;

import java.util.HashMap;
import java.util.UUID;

public class MoveEvent implements Listener {

    HashMap<UUID, Integer> check = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        ConfigManager playerconfig = new ConfigManager(player.getUniqueId().toString());
        ConfigManager area = new ConfigManager((String) playerconfig.getConfig().get("name"));
        String name = (String) area.getConfig().get("name");

        int min = area.getConfig().getInt("min");
        int max = area.getConfig().getInt("max");

        Location pos1 = (Location) area.getConfig().get("pos1");
        Location pos2 = (Location) area.getConfig().get("pos2");

        if(!(pos1 == null && pos2 == null)){
            Region region = new Region(pos1, pos2);
            if(region.locationIsInRegion(player.getLocation())){
                if(check.containsKey(player.getUniqueId())){
                    if(!check.get(player.getUniqueId()).equals(1)){
                        area.getConfig().set("min", (int)area.getConfig().get("min") + 1);
                        Sign sign = Util.getSign(name);
                        if(sign != null){
                            sign.setLine(1, min + 1 + "/" + max);
                            sign.update();
                            ArmorStand armorStand = Util.getArmorstand(name);
                            if(armorStand!=null){
                                armorStand.setCustomName("§a" + area.getConfig().getInt("min") + "/" + area.getConfig().getInt("max"));
                            }
                            player.sendMessage(name + " §a영역에 들어갔습니다." );

                        }
                            area.saveConfig();
                            area.reloadConfig();
                        }
                    } else{
                        check.put(player.getUniqueId(), 1);
                    }
                    check.put(player.getUniqueId(), 1);
            } else{
                if(check.containsKey(player.getUniqueId())){
                    if(check.get(player.getUniqueId()).equals(1)){
                        if(!check.get(player.getUniqueId()).equals(2)){
                            if(!((int)area.getConfig().get("min") < 0)){
                                Sign sign = Util.getSign(name);
                                if(sign != null){
                                    if((int)area.getConfig().get("min") != 0){
                                        area.getConfig().set("min", (int)area.getConfig().get("min") - 1);
                                        sign.setLine(1, min - 1 + "/" + max);
                                        area.saveConfig();
                                        area.reloadConfig();
                                        sign.update();
                                        player.sendMessage(name + " §7영역에서 퇴장하였습니다." );
                                        ArmorStand armorStand = Util.getArmorstand(name);

                                        armorStand.setCustomName("§a" + area.getConfig().getInt("min") + "/" + area.getConfig().getInt("max"));
                                    }
                                }
                            }
                        }
                        check.put(player.getUniqueId(), 2);
                    }
                } else{
                    check.put(player.getUniqueId(), 0);
                }
            }
        }
    }
}
