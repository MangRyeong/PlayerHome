package skh6075.playerhome.command;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import skh6075.playerhome.PlayerHome;
import skh6075.playerhome.session.PlayerSetHomeSession;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            return false;
        }

        if(args.length < 1){
            player.sendMessage(PlayerHome.prefix + "/홈 [집이름]");
            return false;
        }

        String name = args[0];

        PlayerSetHomeSession session = PlayerHome.getInstance().getSession(player);
        if(!session.existsHome(name)){
            player.sendMessage(PlayerHome.prefix + "찾을 수 없는 집입니다.");
            return false;
        }

        Location location = session.getHomeLocation(name);
        if(location == null){
            player.sendMessage(PlayerHome.prefix + "플러그인 오류");
            return false;
        }

        player.teleport(location);
        player.sendMessage(PlayerHome.prefix + "§f" + name + "§r§7 집으로 이동했습니다.");
        player.getWorld().playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 100.0F, 1.0F);

        return true;
    }
}
