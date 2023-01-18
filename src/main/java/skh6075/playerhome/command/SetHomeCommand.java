package skh6075.playerhome.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import skh6075.playerhome.PlayerHome;
import skh6075.playerhome.session.PlayerSetHomeSession;

import java.util.concurrent.atomic.AtomicInteger;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            return false;
        }

        if(args.length < 1){
            player.sendMessage(PlayerHome.prefix + "/셋홈 [집이름]");
            return false;
        }

        String name = args[0];
        if(name.equals("아이템받기") && player.isOp()){
            ItemStack itemStack = PlayerHome.getInstance().getHomeItem();
            itemStack.setAmount(64);
            player.getInventory().addItem(itemStack);
            return false;
        }

        PlayerSetHomeSession session = PlayerHome.getInstance().getSession(player);
        int size = session.getHomeList().size();
        if(size > 0){
            ItemStack itemStack = PlayerHome.getInstance().getHomeItem();
            itemStack.setAmount(size);
            if(!player.getInventory().containsAtLeast(itemStack, size)){
                String itemName = itemStack.getItemMeta().getDisplayName();
                player.sendMessage(PlayerHome.prefix + "셋홈 비용이 부족합니다. (필요 아이템: §f" + itemName + "§r§l§6 x" + size + "개§r§7)");
                return false;
            }

            player.getInventory().removeItem(itemStack);
        }

        session.createHome(name, player.getLocation());
        player.sendMessage(PlayerHome.prefix + "현재 위치를 [ §f" + name + "§r§7 ] 집으로 설정했습니다.");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100.0F, 1.0F);

        return false;
    }
}
