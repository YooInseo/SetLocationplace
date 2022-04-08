package setlocationplace.data;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Util {
    public static int SelectRandomint(int i) {
        int index = (int) ((Math.random() * i + 1));
        if (index != 0) {
            index -= 1;
        }
        return index;
    }
    
    public static boolean isOp(Player player) {
        if (!player.isOp()) {
            player.sendMessage("§c해당 명령어는 관리자만 사용 가능합니다!");
            return false;
        }
        return player.isOp();
    }

    public static boolean isStick(Player player) {
        if (player.getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
            return true;
        } else {
            return false;
        }
    }
    public static Sign getSign(String name){
        World world = Bukkit.getServer().getWorlds().get(0);
        for (Chunk chunk : world.getLoadedChunks())
        {
            for (BlockState blockState : chunk.getTileEntities())
            {
                if (blockState instanceof Sign)
                {
                    Sign sign = (Sign) blockState;
                    if(sign.getLine(0).equalsIgnoreCase(name)){
                        return sign;
                    }
                }
            }
        }
        return null;
    }

    public static ArmorStand getArmorstand(String name ){
        World world = Bukkit.getServer().getWorld("world");
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand) {
                    ArmorStand armorStand = (ArmorStand) entity;
                    if (armorStand.getName().equalsIgnoreCase(name)) {
                        if (!armorStand.equals(null)) {
                            ArmorStand armorStand2 = (ArmorStand) armorStand.getNearbyEntities(0, .5, 0).get(1);
                            return armorStand2;
                        } else {
                            System.out.println(name + " 해당 지역 아머스탠드가 존재 하지 않습니다 <이 기능은 스킵 됩니다>");
                        }
                    }
                }
            }
        return null;
    }

    public static void createArmorStand(Player player, String name,int id){
        boolean spawn = false;

        ConfigManager config = new ConfigManager(name);
        World world = player.getWorld();
        for(Entity e : world.getEntities()){
            if(!e.getLocation().equals(player.getLocation()) && !e.getLocation().equals(player.getLocation().subtract(0,.25,0)) && ! e.getLocation().equals(player.getLocation().subtract(0,.50,0))){
                 spawn =  true;
            }
        }
        if(spawn == true){
            player.sendMessage("§b성공적으로 아머스탠드 생성이 완료 되었습니다!");

            ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            ArmorStand stand2 = player.getWorld().spawn(stand.getLocation().subtract(0,.25,0),  ArmorStand.class);
            ArmorStand stand3 = player.getWorld().spawn(stand2.getLocation().subtract(0,.25,0),  ArmorStand.class);
            stand.setCustomNameVisible(true);
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&',name));
            stand.setGravity(false);
            stand.setVisible(false);

            stand2.setCustomNameVisible(true);
            stand3.setCustomName("§a" + config.getConfig().get("min") + "/" + config.getConfig().get("max"));

            stand2.setCustomName("§e총 인원수");
            stand2.setGravity(false);
            stand2.setVisible(false);

            stand3.setCustomNameVisible(true);
            stand3.setGravity(false);
            stand3.setVisible(false);

            config.getConfig().set(id + ".stand.loc", stand.getLocation());
            config.getConfig().set(id + ".stand.name" , stand.getCustomName());
            config.getConfig().set(id + ".stand2.loc", stand2.getLocation());
            config.getConfig().set(id + ".stand2.name" , stand2.getCustomName());
            config.getConfig().set(id + ".stand3.loc", stand3.getLocation());
            config.getConfig().set(id + ".stand3.name" , stand3.getCustomName());

            config.saveConfig();
            config.reloadConfig();
        }
    }
    public static void Cooltime(){

    }
}
