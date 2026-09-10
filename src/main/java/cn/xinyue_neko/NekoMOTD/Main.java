package cn.xinyue_neko.NekoMOTD;

import cn.xinyue_neko.NekoMOTD.command.TestCommand;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    String heading;
    List<String> motd;

    /** 死了都要try */
    public void confRegister() {
        /*
         *  创建文件
         */
        this.getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(), "config.yml");

        this.conf = YamlConfiguration.loadConfiguration(f);
        this.conf.addDefault("welcome-text", DEFAULT_TEXT);

        this.conf.addDefault("heading", "&bNekoMOTD Default heading"); //抄的SuperMotd
        this.conf.addDefault("motd-list", Arrays.asList("&aMOTD random line 1", "&aMOTD random line 2", "&aMOTD random line 3"));

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
        this.getCommand("test").setExecutor(new TestCommand());
        this.heading = this.conf.getString("heading").replace('&', ChatColor.COLOR_CHAR);
        this.motd = this.conf.getStringList("motd-list");
        this.getServer().getConsoleSender().sendMessage("[" + TITLE + "]" + ChatColor.AQUA + " Setting MOTD Heading to: " + this.heading);
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

    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        Random r = new Random();
        String randStr = this.motd.get(r.nextInt(motd.size())).replace('&', ChatColor.COLOR_CHAR);
        e.setMotd(heading + "\n" + randStr);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadmotd")) {
            if(sender.hasPermission("supermotd.reloadmotd")){
                sender.sendMessage(ChatColor.GREEN + "Reloading MOTD Config...");
                reloadConfig();
                heading = this.conf.getString("heading").replace("&", "§");
                motd = this.conf.getStringList("motd-list");
                sender.sendMessage("New MOTD Heading: " + heading);
                return true;
            }
        }
        return true;
    }
}
