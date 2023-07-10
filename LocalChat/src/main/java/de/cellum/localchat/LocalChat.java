package de.cellum.localchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class LocalChat extends JavaPlugin implements Listener {
    private Set<Player> localChatPlayers;

    @Override
    public void onEnable() {
        localChatPlayers = new HashSet<>();
        getCommand("chl").setExecutor(new LocalChatCommand());
        getCommand("chg").setExecutor(new GlobalChatCommand());
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage("Done loading LocalChat Plugin! By Cellum");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (localChatPlayers.contains(player)) {
            event.setCancelled(true);
            int radius = 200;
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.getLocation().distance(player.getLocation()) <= radius) {
                    p.sendMessage("§b§l[§3Local-Chat§b§l] §r§9" +  event.getPlayer().getDisplayName() + " §3: " + "§b" + event.getMessage());
                }
            }
        }
    }

    private class LocalChatCommand implements org.bukkit.command.CommandExecutor {
        @Override
        public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von Spielern verwendet werden.");
                return true;
            }

            Player player = (Player) sender;

            if (localChatPlayers.contains(player)) {
                player.sendMessage(ChatColor.YELLOW + "Du bist bereits im lokalen Chat-Modus.");
            } else {
                localChatPlayers.add(player);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im lokalen Chat-Modus.");
            }

            return true;
        }
    }

    private class GlobalChatCommand implements org.bukkit.command.CommandExecutor {
        @Override
        public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von Spielern verwendet werden.");
                return true;
            }

            Player player = (Player) sender;

            if (localChatPlayers.contains(player)) {
                localChatPlayers.remove(player);
                player.sendMessage(ChatColor.GREEN + "Du bist nun wieder im globalen Chat-Modus.");
            } else {
                player.sendMessage(ChatColor.YELLOW + "Du bist bereits im globalen Chat-Modus.");
            }

            return true;
        }
    }
}


