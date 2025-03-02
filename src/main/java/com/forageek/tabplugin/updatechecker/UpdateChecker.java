package com.forageek.tabplugin.updatechecker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {
	private final JavaPlugin plugin;
	private final int resourceId; // The ID of your plugin on SpigotMC
	public static boolean isLatestVersion;

	public UpdateChecker(JavaPlugin plugin, int resourceId) {
		this.plugin = plugin;
		this.resourceId = resourceId;
	}

	public void checkForUpdates() {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try {
				// Spigot API URL for checking updates
				URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setReadTimeout(5000);
				connection.setConnectTimeout(5000);

				try (Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()))) {
					if (scanner.hasNext()) {
						String latestVersion = scanner.next(); // Get the latest version from SpigotMC
						String currentVersion = plugin.getDescription().getVersion();

						if (!latestVersion.equalsIgnoreCase(currentVersion)) {
							plugin.getLogger().info("A new update is available: v" + latestVersion);
							plugin.getLogger().info("Download it at: https://www.spigotmc.org/resources/" + resourceId);
							isLatestVersion = false;
						} else {
							plugin.getLogger().info("Your plugin is up to date! (v" + currentVersion + ")");
							isLatestVersion = true;
						}
					}
				}
			} catch (IOException e) {
				plugin.getLogger().warning("Could not check for updates: " + e.getMessage());
			}
		});
	}
}
