package setlocationplace.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import setlocationplace.data.ConfigManager;
import setlocationplace.data.Util;


public class EditSign implements Listener {
    @EventHandler
    public void Change(SignChangeEvent event) {
        Player player = event.getPlayer();
        String name = ChatColor.translateAlternateColorCodes('&',event.getLine(0));
        int min = 0;
        int max = 0;

        ConfigManager manager = new ConfigManager(name);
        if(Util.isOp(player)){
            if(name.equalsIgnoreCase((String) manager.getConfig().get("name"))){
                min = manager.getConfig().getInt("min");
                max = manager.getConfig().getInt("max");
                event.setLine(0, name);
                event.setLine(1, min + "/" + max);



                player.sendMessage("§b성공적으로 표지판 설치가 되었습니다!");
            }
        }
    }

}
