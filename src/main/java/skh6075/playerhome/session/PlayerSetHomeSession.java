package skh6075.playerhome.session;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import skh6075.playerhome.PlayerHome;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class PlayerSetHomeSession {
    private FileConfiguration sessionConfig;

    private final File sessionConfigFile;

    public PlayerSetHomeSession(Player player){
        this.sessionConfigFile = new File(PlayerHome.getSessionBaseFile(), player.getName().toLowerCase() + ".yml");

        try{
            if(this.sessionConfigFile.createNewFile()){
                player.sendMessage(PlayerHome.prefix + "홈 DB를 생성했습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.initSessionConfig();
    }

    private void initSessionConfig(){
        sessionConfig = new YamlConfiguration();
        try{
            sessionConfig.load(this.sessionConfigFile);
        }catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public boolean existsHome(String name){
        return sessionConfig.contains(name + ".created");
    }

    public void createHome(String name, Location location){
        if(existsHome(name)){
            return;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

        sessionConfig.set(name + ".created", format.format(date));
        sessionConfig.set(name + ".x", location.getX());
        sessionConfig.set(name + ".y", location.getY());
        sessionConfig.set(name + ".z", location.getZ());
        sessionConfig.set(name + ".yaw", location.getYaw());
        sessionConfig.set(name + ".pitch", location.getPitch());
        sessionConfig.set(name + ".world", location.getWorld().getName());

        this.save();
    }

    @Nullable
    public Location getHomeLocation(String name){
        if(!existsHome(name)){
            return null;
        }

        return new Location(
                Bukkit.getWorld((String) Objects.requireNonNull(sessionConfig.get(name + ".world"))),
                sessionConfig.getDouble(name + ".x"),
                sessionConfig.getDouble(name + ".y"),
                sessionConfig.getDouble(name + ".z"),
                sessionConfig.getLong(name + ".yaw"),
                sessionConfig.getLong(name + ".pitch"));
    }

    public @NotNull Set<String> getHomeList(){
        return sessionConfig.getKeys(false);
    }

    public void save(){
        try{
            sessionConfig.save(this.sessionConfigFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
