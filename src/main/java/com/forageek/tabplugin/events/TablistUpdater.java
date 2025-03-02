package com.forageek.tabplugin.events;

import com.forageek.tabplugin.TabPlugin;
import com.forageek.tabplugin.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.annotation.AutoRegister;

import static com.forageek.tabplugin.settings.Settings.GET_NOTIFIED;
import static com.forageek.tabplugin.settings.Settings.GET_WARNED;

@AutoRegister
public final class TablistUpdater extends BukkitRunnable implements Listener {

	private static TablistUpdater instance; // Keep track of the task instance

	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().isEmpty()) {
			stopTask();
			return;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			String header = ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.HEADER));
			String footer = ChatColor.translateAlternateColorCodes('&', String.join("\n", Settings.FOOTER));

			header = PlaceholderAPI.setPlaceholders(player, header);
			footer = PlaceholderAPI.setPlaceholders(player, footer);

			player.setPlayerListHeader(header);
			player.setPlayerListFooter(footer);

		}
	}

	@EventHandler
	public static void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Common.setTellPrefix("[TabPlugin]");
		if (player.isOp() && GET_NOTIFIED)
			Common.tell(player, "A new update is available for TabPlugin! Disable this message in the settings.yml.");
		if (player.isOp() && GET_WARNED)
			Common.tell(player, "Don't forget to add the placeholders through /papi ecloud download <name>. Disable this message in the settings.yml" );

		if (instance == null) {
			startTask(); // Start the task when a player joins and it isn't running
		}
	}

	@EventHandler
	public static void onLeave(PlayerQuitEvent event) {
		if (Bukkit.getOnlinePlayers().size() == 1) { // The last player is leaving
			stopTask();
		}
	}

	public static void startTask() {
		if (instance != null) return; // Prevent multiple tasks from running

		instance = new TablistUpdater();
		instance.runTaskTimerAsynchronously(TabPlugin.getInstance(), 20L, 20L); // Runs every second
	}

	public static void stopTask() {
		if (instance != null) {
			instance.cancel();
			instance = null;
		}
	}

	public static void shutdown() {
		stopTask();
	}
}
