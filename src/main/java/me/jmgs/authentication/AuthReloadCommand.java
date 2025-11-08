package me.jmgs.authentication;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AuthReloadCommand implements CommandExecutor {

    private final ChatAuthPlugin plugin;

    public AuthReloadCommand(ChatAuthPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("auth.reload")) {
            sender.sendMessage("§c권한이 없습니다!");
            return true;
        }

        plugin.loadSettings();
        sender.sendMessage("§a설정이 리로드되었습니다!");
        return true;
    }
}