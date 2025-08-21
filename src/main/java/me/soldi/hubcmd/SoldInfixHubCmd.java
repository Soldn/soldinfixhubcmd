package me.soldi.hubcmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoldInfixHubCmd extends JavaPlugin implements CommandExecutor {

    private String targetServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        targetServer = getConfig().getString("server-name", "lobby");
        this.getCommand("hub").setExecutor(this);

        // Регистрируем канал для BungeeCord
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getLogger().info("SoldInfixHubCmd включен. Целевой сервер: " + targetServer);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок.");
            return true;
        }

        Player player = (Player) sender;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(targetServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
        player.sendMessage(ChatColor.GREEN + "Перемещаю на " + targetServer + "...");
        return true;
    }
}
