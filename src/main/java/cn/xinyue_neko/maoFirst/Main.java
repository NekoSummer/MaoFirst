package cn.xinyue_neko.maoFirst;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener {

    public final String TITLE = "FirstPlugin";
    public YamlConfiguration conf;
    private static final String DEFAULT_TEXT = "&l&bWelcome @p join our server!";

    /** 死了都要try */
    public void confRegister() {
        /*
         *  创建文件
         */
        this.getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(), "config.yml");

        this.conf = YamlConfiguration.loadConfiguration(f);
        this.conf.addDefault("welcome-text", DEFAULT_TEXT);

        this.conf.options().copyDefaults(true);
        try {
            this.conf.save(f);
        } catch (IOException e) {
            this.getServer().getConsoleSender().sendRawMessage("[" + TITLE + "]" + ChatColor.RED + " ERROR, " + e.getMessage());
            this.conf = new YamlConfiguration();
        }
    }

    @Override
    public void onEnable() {
        /*
         * 注册
         */
        this.confRegister();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[" + TITLE + "]" + ChatColor.AQUA + " Hello");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String r = DEFAULT_TEXT;
        Object obj = this.conf.get("welcome-text");
        if (obj instanceof String && !((String) obj).trim().isEmpty()) {
            r = (String) obj;
        }
        String msg = ChatColor.translateAlternateColorCodes('&', r).replace("@p", p.getName());
        e.setJoinMessage(msg);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "FUCK YOU! MOJANG");
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "FUCK YOU! MICROSOFT");
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
        }
        return true;
    }
}
