package setlocationplace.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import setlocationplace.data.*;

import java.util.*;

public class BlockClick implements Listener {

    public static HashMap<UUID, Location> loc = new HashMap<>();
    public static HashMap<UUID, Location> loc2 = new HashMap<>();
    public static String name = "";

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Player player = event.getPlayer();

        EquipmentSlot e = event.getHand(); //Get the hand of the event and set it to 'e'.
        if (event.hasBlock()) {
            Location loc = event.getClickedBlock().getLocation();

            if (event.getAction() == Action.LEFT_CLICK_BLOCK ) {
                if (e.equals(EquipmentSlot.HAND) && Util.isStick(player)) { //If the event is fired by HAND (main hand)

                    this.loc.put(player.getUniqueId(), loc);
                    player.sendMessage("§7첫번째 좌표가 스폰 포인트가 §cX" + (int) loc.getX() + " §aY" + (int) loc.
                            getY() + " §eZ" + (int) loc.getZ() + "§7" +
                            "좌표로 지정 되었습니다!");
                    event.setCancelled(true);
                }
            } else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                if (e.equals(EquipmentSlot.HAND) && Util.isStick(player)) { //If the event is fired by HAND (main hand)

                    this.loc2.put(player.getUniqueId(), loc);

                    player.sendMessage("§b두번째 좌표가 스폰 포인트가 §cX" + (int) loc.getX() + " §aY" + (int) loc.
                            getY() + " §eZ" + (int) loc.getZ() + "§b" +
                            "좌표로 지정 되었습니다!");

                    event.setCancelled(true);
                } else if(event.getClickedBlock().getType() == Material.SIGN ||event.getClickedBlock().getType() == Material.SIGN_POST ||event.getClickedBlock().getType() == Material.WALL_SIGN){

                    Sign sign = (Sign) event.getClickedBlock().getState();

                    name = sign.getLine(0);

                    int min = 0;
                    int max = 0;

                    ConfigManager manager = new ConfigManager(name);

                    List<Location> locs = (List<Location>) manager.getConfig().getList("Locations");


                    min = manager.getConfig().getInt("min");
                    max = manager.getConfig().getInt("max");
                    if(min == max){
                        player.sendMessage("§c인원이 꽉찼습니다");
                    } else{
                        ConfigManager manager2 = new ConfigManager( player.getUniqueId().toString());


                        /**
                         *  지역 안에 있는 표지판을 클릭시 다른 지역으로 이동
                         *  클릭한 표지판이 첫번째라면 null검사를 통해 예외처리
                         *  클릭한 표지판이 두번째라면 표지판 위치를 불러와 첫번째로 들어간 지역에 위치하는지 체크
                         *  동시에 해당 표지판 이름으로 다음 지역 파일을 불러옴.
                         */


                        Location pos1 = (Location) manager.getConfig().get("pos1");
                        Location pos2 = (Location) manager.getConfig().get("pos2");

                        Region region = new Region(pos1,pos2);

                        player.sendMessage(region.locationIsInRegion(sign.getLocation())+  "");

                        player.teleport( locs.get(Util.SelectRandomint(locs.size())));

                        manager2.getConfig().set("name", name);
                        manager2.saveConfig();

                        manager.getConfig().set("min", min  );
                        manager.saveConfig();
                        manager.reloadConfig();

                        sign.setLine(1, min  + "/" + max);
                        sign.update();

                        ArmorStand armorStand = Util.getArmorstand(name);
                        if(armorStand != null)
                        armorStand.setCustomName("§a" + manager.getConfig().getInt("min") + "/" + manager.getConfig().getInt("max"));

                        player.sendMessage(name + " §a지역으로 이동되었습니다!");

                    }
                }
            }
        }
    }
}
