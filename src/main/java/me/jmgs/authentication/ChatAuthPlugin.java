/*
 * Copyright (c) 2025 Madmovies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jmgs.authentication;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChatAuthPlugin extends JavaPlugin implements Listener {

    private File settingsFile;
    private FileConfiguration settingsConfig;
    private Set<Player> authenticatedPlayers;
    private Set<Player> waitingForCode;
    private String authCode;

    @Override
    public void onEnable() {
        authenticatedPlayers = new HashSet<>();
        waitingForCode = new HashSet<>();

        createSettingsFile();
        loadSettings();

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("authreload").setExecutor(new AuthReloadCommand(this));
        getCommand("auth-exit").setExecutor(new AuthExitCommand());

        getLogger().info("Chat Authentication Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        authenticatedPlayers.clear();
        waitingForCode.clear();
        getLogger().info("Chat Authentication Plugin has been disabled!");
    }

    private void createSettingsFile() {
        settingsFile = new File(getDataFolder(), "settings.yml");

        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            try {
                settingsFile.createNewFile();
                settingsConfig = YamlConfiguration.loadConfiguration(settingsFile);
                settingsConfig.set("auth-code", "1234");
                settingsConfig.save(settingsFile);
                getLogger().info("settings.yml created with default auth-code: 1234");
            } catch (IOException e) {
                getLogger().severe("Could not create settings.yml!");
                e.printStackTrace();
            }
        }
    }

    public void loadSettings() {
        settingsConfig = YamlConfiguration.loadConfiguration(settingsFile);
        authCode = settingsConfig.getString("auth-code", "1234");
        getLogger().info("Settings loaded.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        waitingForCode.add(player);

        getServer().getScheduler().runTaskLater(this, () -> {
            showAuthenticationDialog(player);
        }, 10L);
    }

    private void showAuthenticationDialog(Player player) {
        player.sendMessage("");
        player.sendMessage("§8§m                                                                ");
        player.sendMessage("");
        player.sendMessage("          §6§l§o✦ Server Authentication ✦");
        player.sendMessage("");
        player.sendMessage("   §7입장 코드를 입력하여 서버에 접속하세요");
        player.sendMessage("");

        // Join 버튼 (클릭 가능한 텍스트)
        Component joinButton = Component.text("     [  Join  ]")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("채팅창에 코드를 입력하세요")))
                .clickEvent(ClickEvent.suggestCommand(""));

        // Exit 버튼
        Component exitButton = Component.text("     [  Exit  ]")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("서버에서 나가기")))
                .clickEvent(ClickEvent.runCommand("/auth-exit"));

        player.sendMessage("   §7▶ §e채팅창에 입장 코드를 입력하세요");
        player.sendMessage("");
        player.sendMessage(joinButton);
        player.sendMessage(exitButton);
        player.sendMessage("");
        player.sendMessage("§8§m                                                                ");
        player.sendMessage("");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (waitingForCode.contains(player)) {
            event.setCancelled(true);

            String input = event.getMessage().trim();

            getServer().getScheduler().runTask(this, () -> {
                if (checkAuthCode(input)) {
                    authenticatePlayer(player);
                    waitingForCode.remove(player);
                } else {
                    player.sendMessage("");
                    player.sendMessage("§c✘ 코드가 올바르지 않습니다!");
                    player.sendMessage("§e다시 시도해주세요.");
                    player.playSound(player.getLocation(), "entity.villager.no", 1.0f, 1.0f);

                    getServer().getScheduler().runTaskLater(this, () -> {
                        showAuthenticationDialog(player);
                    }, 40L);
                }
            });
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (waitingForCode.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        authenticatedPlayers.remove(player);
        waitingForCode.remove(player);
    }

    public boolean checkAuthCode(String input) {
        return authCode.equals(input);
    }

    public void authenticatePlayer(Player player) {
        authenticatedPlayers.add(player);
        player.sendMessage("");
        player.sendMessage("§a§l✔ 인증 성공!");
        player.sendMessage("§7서버에 오신 것을 환영합니다.");
        player.sendMessage("");
        player.sendTitle("§a§l인증 완료", "§7환영합니다!", 10, 40, 10);
        player.playSound(player.getLocation(), "entity.player.levelup", 1.0f, 1.0f);
    }
}
