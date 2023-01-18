package skh6075.playerhome.command.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import skh6075.playerhome.PlayerHome;
import skh6075.playerhome.session.PlayerSetHomeSession;

import java.util.ArrayList;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            return null;
        }

        PlayerSetHomeSession session = PlayerHome.getInstance().getSession(player);
        return new ArrayList<>(session.getHomeList());
    }
}
