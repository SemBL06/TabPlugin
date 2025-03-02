package com.forageek.tabplugin;



import com.forageek.bstats.Metrics;
import com.forageek.tabplugin.events.TablistUpdater;
import com.forageek.tabplugin.updatechecker.UpdateChecker;

import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;


public final class TabPlugin extends SimplePlugin {

	private static final int SpigotID = 110126;
	private static final int bstatsPluginID = 18616;

	@Override
	protected void onPluginStart() {

	}


	@Override
	protected void onReloadablesStart() {

		new UpdateChecker(this, SpigotID).checkForUpdates();

		new Metrics(this, bstatsPluginID);

		if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			Common.logFramed("TabPlugin needs the PlaceholderAPI to work!", "Please install the plugin here:", "https://www.spigotmc.org/resources/placeholderapi.6245/");
			Bukkit.getPluginManager().disablePlugin(this);
		}

	}

	@Override
	public void onPluginStop() {

		TablistUpdater.shutdown();
		Common.logFramed("", "Succesfully Stopped!", "");

	}

	public static TabPlugin getInstance() {
		return (TabPlugin) SimplePlugin.getInstance();
	}
}
