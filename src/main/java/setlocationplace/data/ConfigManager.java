package setlocationplace.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import setlocationplace.events.BlockClick;
import setlocationplace.main.Setlocationplace;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {
    private Setlocationplace plugin = Setlocationplace.getPlugin(Setlocationplace.class);

    public FileConfiguration areaconfig;
    public File areafile;
    private String name;

    public ConfigManager(String name){
        this.name = name;
        saveDefualtConfig();
    }
    public void reloadConfig(){
        if(this.areaconfig == null)
            this.areafile = new File(this.plugin.getDataFolder(), name + ".yml");
        this.areaconfig = YamlConfiguration.loadConfiguration(this.areafile);

        InputStream inputStream = this.plugin.getResource(name + ".yml");
        if(inputStream != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            this.areaconfig.setDefaults(config);
        }
    }

    public FileConfiguration getConfig() {
        if(this.areaconfig == null) reloadConfig();
        return areaconfig;
    }

    public void saveConfig(){
        if(this.areaconfig == null || this.areafile == null) return;
        try {
            getConfig().save(this.areafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveDefualtConfig(){
        if(this.areafile == null)
            this.areafile = new File(this.plugin.getDataFolder(), name + ".yml");
        if(!this.areafile.exists()){
            areafile = new File(this.plugin.getDataFolder(), name + ".yml");
            try {
                areafile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





//
//    public void setUP(String name, Player player){
//        if(!plugin.getDataFolder().exists()){
//            plugin.getDataFolder().mkdir();
//        }
//        areafile = new File(plugin.getDataFolder(), name + ".yml");
//
//        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + name + " 영역의 파일이 생성 되었습니다. ");
//        if(!areafile.exists()){
//            try {
//                areafile.createNewFile();
//                areaconfig = YamlConfiguration.loadConfiguration(areafile);
//                areaconfig.set("name",name);
//                areaconfig.set("pos1.x", BlockClick.loc.get(player.getUniqueId()).getX());
//                areaconfig.set("pos1.y", BlockClick.loc.get(player.getUniqueId()).getY());
//                areaconfig.set("pos1.z", BlockClick.loc.get(player.getUniqueId()).getZ());
//
//                areaconfig.set("pos2.x", BlockClick.loc2.get(player.getUniqueId()).getX());
//                areaconfig.set("pos2.y", BlockClick.loc2.get(player.getUniqueId()).getY());
//                areaconfig.set("pos2.z", BlockClick.loc2.get(player.getUniqueId()).getZ());
//                areaconfig.set("minY", player.getLocation().getY());
//                areaconfig.set("min", 0);
//                areaconfig.set("max", 10);
//
//                saveareas();
//                reloadarea();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public Object get(String name, String path){
//        areafile = new File(plugin.getDataFolder(), name + ".yml");
//        areaconfig = YamlConfiguration.loadConfiguration(areafile);
//        return areaconfig.get(path);
//    }
//
//    public void SetPlayer(String name, Player player){
//        areafile = new File(plugin.getDataFolder(), name + ".yml");
//        areaconfig = YamlConfiguration.loadConfiguration(areafile);
//        areaconfig.set("min" ,  (int) get(name,"min")+ 1 );
//        plugin.getConfig().options().copyDefaults(true);
//        saveareas();
//    }
//    public File getAreafile() {
//        return areafile;
//    }
//
//    public FileConfiguration getAreaconfig() {
//        try {
//            areaconfig.load(areafile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidConfigurationException e) {
//            e.printStackTrace();
//        }
//        return areaconfig;
//    }
//    public void saveareas(){
//        try {
//            areaconfig.save(areafile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void reloadarea(){
//        areaconfig = YamlConfiguration.loadConfiguration(areafile);
//
//    }
}
