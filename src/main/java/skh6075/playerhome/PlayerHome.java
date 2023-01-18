package skh6075.playerhome;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import skh6075.playerhome.command.HomeCommand;
import skh6075.playerhome.command.SetHomeCommand;
import skh6075.playerhome.command.completer.HomeTabCompleter;
import skh6075.playerhome.session.PlayerSetHomeSession;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public final class PlayerHome extends JavaPlugin implements Listener {
    public final static String prefix = "§l§6 ➤§r§7 ";

    private static PlayerHome instance;

    private static File sessionBaseFile;

    private static ItemStack homeItem;

    private final HashMap<String, PlayerSetHomeSession> sessions = new HashMap<>();

    public static PlayerHome getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        sessionBaseFile = new File(getDataFolder(), "session");
        boolean value = sessionBaseFile.mkdirs();
        if(value){
            getLogger().log(Level.INFO, sessionBaseFile.getAbsolutePath() + " 세션 폴더를 생성했습니다.");
        }

        homeItem = new ItemStack(Material.HEART_OF_THE_SEA, 1);

        ItemMeta itemMeta = homeItem.getItemMeta();
        itemMeta.setDisplayName("§r§l§g[특별]§r§f 집 슬롯 확장권");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§r§f―――――――――――――――――――");
        lore.add("§r§7 셋홈에 필요한 아이템입니다.");
        lore.add("§r§f /셋홈 §7명령어로 홈 설정이 가능합니다.");
        lore.add("§r§f―――――――――――――――――――");
        lore.add("");
        lore.add("§r§g 2023년 01월 17일 부터 사용가능");
        itemMeta.setLore(lore);
        homeItem.setItemMeta(itemMeta);

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("home")).setTabCompleter(new HomeTabCompleter());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());

        getServer().getPluginManager().registerEvents(this, this);
    }

    public static File getSessionBaseFile() {
        return sessionBaseFile;
    }

    public ItemStack getHomeItem(){
        return homeItem;
    }

    public PlayerSetHomeSession getSession(Player player){
        return sessions.getOrDefault(player.getName().toLowerCase(), new PlayerSetHomeSession(player));
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(sessions.containsKey(player.getName().toLowerCase())){
            getSession(player).save();
            sessions.remove(player.getName().toLowerCase());
        }
    }
}
