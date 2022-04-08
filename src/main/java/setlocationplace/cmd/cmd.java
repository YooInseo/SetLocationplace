package setlocationplace.cmd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import setlocationplace.data.*;
import setlocationplace.events.BlockClick;

import java.util.ArrayList;
import java.util.List;

public class cmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String name;
        ConfigManager config;

        int id;
         if(args.length != 0){
            switch (args[0]){
                case "spawnpoint":
                    if(Util.isOp(player)) {

                            name = ChatColor.translateAlternateColorCodes('&',args[1]);

                            config = new ConfigManager(name);

                            config.getConfig().set("spawnpoint", player.getLocation());

                            config.saveConfig();

                            player.sendMessage("§b스폰 포인트가 §cX" + (int)player.getLocation().getX() + " §aY" + (int)player.getLocation().
                                    getY()  + " §eZ" + (int)player.getLocation().getZ() +  "§b" +
                                    "좌표로 지정 되었습니다!");
                    }
                    break;

                case "max":
                    if(Util.isOp(player)){
                        name = ChatColor.translateAlternateColorCodes('&',args[1]);
                        config = new ConfigManager(name);
                        Integer max = Integer.parseInt(args[2]);

                        if(max < (int)config.getConfig().get("min")){
                            player.sendMessage("§cmin보다 작은 max값은 설정이 불가능 합니다!");
                        } else{
                            Sign sign = Util.getSign(name);
                            config.getConfig().set("max", max);

                            config.saveConfig();
                            config.reloadConfig();
                            sign.setLine(1,  config.getConfig().get("min") + "/" + max);
                            sign.update();
                            Util.getArmorstand(name).setCustomName("§a" + config.getConfig().getInt("min") + "/" + config.getConfig().getInt("max"));

                        }
                    }
                    break;

                case "create":
                    if(Util.isOp(player)){
                        name =  ChatColor.translateAlternateColorCodes('&',args[1]);
                        config = new ConfigManager(name);

                        config.getConfig().set("name",name);
                        config.getConfig().set("pos1", BlockClick.loc.get(player.getUniqueId()));
                        config.getConfig().set("pos2", BlockClick.loc2.get(player.getUniqueId()));
                        config.getConfig().set("minY", player.getLocation().getY());
                        config.getConfig().set("min", 0);
                        config.getConfig().set("max", 10);
                        ArrayList<Location> locations = new ArrayList<>();

                        config.getConfig().set("Locations", locations);

                        config.saveConfig();

                        player.sendMessage("§a성공적으로 " + config.getConfig().get("name") + " 지역이 설정 되었습니다");
                    }

                    break;
                case "minY":
                    if(Util.isOp(player)){
                        name = ChatColor.translateAlternateColorCodes('&',args[1]);
                        config = new ConfigManager(name);
                        config.getConfig().set("minY", player.getLocation().getY());
                        config.saveConfig();
                        player.sendMessage("§a성공적으로 해당 좌표 §e" + (int)player.getLocation().getY() +  "§a로 좌표 설정이 되었습니다!");
                    }

                    break;
                case "ic":
                    if(Util.isOp(player)){
                        name = ChatColor.translateAlternateColorCodes('&',args[1]);
                        id  = Integer.parseInt(args[2]);
                        Util.createArmorStand(player,name,id);
                    }
                    break;
                case "ir":
                    if(Util.isOp(player)){
                        name = ChatColor.translateAlternateColorCodes('&',args[1]);

                        id = Integer.parseInt(args[2]);

                        config = new ConfigManager(name);
                        Location loc = (Location) config.getConfig().get(id + ".stand.loc");
                        Location loc2 = (Location) config.getConfig().get(id + ".stand2.loc");
                        Location loc3 = (Location) config.getConfig().get(id + ".stand3.loc");

                        for(Entity e : player.getWorld().getEntities()){
                            if(e.getLocation().equals(loc)){
                                e.remove();
                            } else if(e.getLocation().equals(loc2)){
                                e.remove();
                            } else if(e.getLocation().equals(loc3)){
                                e.remove();
                            }
                        }

                        config.getConfig().set(id + "","");
                        config.saveConfig();
                        config.reloadConfig();
                        player.sendMessage("§c성공적으로 아머스탠드를 삭제 하였습니다.");
                    }
                    break;
                case "add":
                    if(Util.isOp(player)) {
                        name = ChatColor.translateAlternateColorCodes('&', args[1]);
                        config = new ConfigManager(name);
                        if(config != null){
                            Location pos1 = (Location) config.getConfig().get("pos1");
                            Location pos2 = (Location) config.getConfig().get("pos2");

                            Region region = new Region(pos1,pos2);
                            if(region.locationIsInRegion(player.getLocation())){


                                List<Location> configList = (List<Location>)config.getConfig().getList("Locations");
                                configList.add(player.getLocation());
                                config.getConfig().set("Locations", configList);

                                config.saveConfig();
                                    player.sendMessage(name + "§a영역의 스폰포인트에 §7X " + (int)player.getLocation().getX() + " Y " + (int) player.getLocation().getY() + " Z " + (int) player.getLocation().getZ() + " §a좌표를 추가하였습니다.");


                             } else{
                                player.sendMessage("§c영역의 밖에 있습니다.");
                                return false;
                            }
                        } else{
                            player.sendMessage("§c해당 이름을 가진 영역이 없습니다.");
                        }
                    }
                    break;
                case "list":
                    if(Util.isOp(player)) {
                        name = ChatColor.translateAlternateColorCodes('&', args[1]);
                        config = new ConfigManager(name);
                        if(config != null){
                                List<Location> locs = (List<Location>) config.getConfig().getList("Locations");
                                for(int i = 0; i < locs.size(); i++){
                                    player.sendMessage(name + " §a영역의 좌표 §7{X" + (int)locs.get(i).getX() + "Y" + (int)locs.get(i).getY() + " Z" +(int)locs.get(i).getZ() + "} §bIndex(§l" + i + "§b)");
                                }
                            } else{
                                player.sendMessage("§c해당 이름을 가진 영역이 없습니다.");
                                return false;
                            }
                        }
                    break;
                case "remove":
                    if(Util.isOp(player)) {
                        name = ChatColor.translateAlternateColorCodes('&', args[1]);
                        config = new ConfigManager(name);
                        int index = Integer.parseInt(args[2]);

                        if(config != null){

                            List<Location> locs = (List<Location>) config.getConfig().getList("Locations");
                            locs.remove(index);
                            config.getConfig().set("Locations", locs);
                            config.saveConfig();

                            player.sendMessage("§a성공적으로 " + index + " 의 좌표를 지웠습니다");


                        } else{
                            player.sendMessage("§c해당 이름을 가진 영역이 없습니다.");
                            return false;
                        }
                    }
                    break;
                case "teleport":
                    if(Util.isOp(player)) {
                        name = ChatColor.translateAlternateColorCodes('&', args[1]);
                        config = new ConfigManager(name);
                        int index = Integer.parseInt(args[2]);

                        if(config != null){

                            List<Location> locs = (List<Location>) config.getConfig().getList("Locations");

                            player.teleport(locs.get(index));

                            player.sendMessage("§a성공적으로 " + index + " 좌표 로 텔레포트 했습니다!");


                        } else{
                            player.sendMessage("§c해당 이름을 가진 영역이 없습니다.");
                            return false;
                        }
                    }
                    break;
            }
        } else{
             if(Util.isOp(player)) {
                 player.sendMessage("§7/setplace create <name> - 해당 이름의 영역을 생성합니다.");

                 player.sendMessage("§7/setplace add <name> - 플레이어 좌표로 텔레포트 위치를 추가합니다.");
                 player.sendMessage("§7/setplace remove <name> <index> - 해당 인덱스의 좌표를 제거합니다.");
                 player.sendMessage("§7/setplace teleport <name> <index> - 해당 인덱스의 좌표로 텔레포트 합니다.");
                 player.sendMessage("§7/setplace list <name> - 해당 영역의 인덱스를 알려줍니다.");

                 player.sendMessage("§7/setplace ic name id - 해당 이름의 아머스탠드 설치");
                 player.sendMessage("§7/setplace ir name id - 해당 이름과 id의 아머스탠드 제거");
                 player.sendMessage("§7/setplace minY <name> - 플레이어 Y좌표로 최소Y설정");

                 player.sendMessage("§7/setplace max <name> <Integer> - 최대 플레이어 설정");
             }
            return false;
        }
        return false;
    }
}
