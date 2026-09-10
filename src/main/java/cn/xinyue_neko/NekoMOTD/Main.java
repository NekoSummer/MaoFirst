package cn.xinyue_neko.NekoMOTD;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    public final String TITLE = "NekoMOTD";
    public YamlConfiguration conf;
    private static final String DEFAULT_TEXT = "&b&lWelcome @p join our server!";

    public String heading;
    public List<String> motd;

    public Main() {

    }

    /** 死了都要try */
    public void confRegister() {
        /*
         *  创建文件
         */
        this.getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(), "config.yml");
        conf = YamlConfiguration.loadConfiguration(f);
        conf.addDefault("welcome-text", DEFAULT_TEXT);

        conf.addDefault("heading", "&bNekoMOTD Default heading"); //抄的SuperMotd
        conf.addDefault("motd-list", Arrays.asList("&aMOTD random line 1", "&aMOTD random line 2", "&aMOTD random line 3"));

        conf.options().copyDefaults(true);

        try {
            conf.save(f);
        } catch (IOException e) {
            this.getServer().getConsoleSender().sendRawMessage("[" + TITLE + "]" + ChatColor.RED + " ERROR, " + e.getMessage());
            conf = new YamlConfiguration();
        }
    }

    @Override
    public void onEnable() {
        /*
         * 注册
         */
        this.confRegister();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("test").setExecutor(new TestCommand());
        heading = conf.getString("heading").replace('&', ChatColor.COLOR_CHAR);
        motd = conf.getStringList("motd-list");
        this.getServer().getConsoleSender().sendMessage("[" + TITLE + "]" + ChatColor.AQUA + " Setting MOTD Heading to: " + this.heading);
    }



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String r = DEFAULT_TEXT;
        Object obj = conf.get("welcome-text");
        if (obj instanceof String && !((String) obj).trim().isEmpty()) {
            r = (String) obj;
        }
        String msg = ChatColor.translateAlternateColorCodes('&', r)
                .replace("@p", p.getName())
                .replace("@n", "\n");
        e.setJoinMessage(msg);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        Random r = new Random();
        String randStr = motd.get(r.nextInt(motd.size())).replace('&', ChatColor.COLOR_CHAR);
        e.setMotd(heading + "\n" + randStr);
    }

}
