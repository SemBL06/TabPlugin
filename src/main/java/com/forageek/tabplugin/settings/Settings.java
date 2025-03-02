package com.forageek.tabplugin.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.List;

public class Settings extends YamlStaticConfig {
	@Override
	protected void onLoad() {
		this.loadConfiguration("settings.yml");
		init();
	}

	public static String[] HEADER;
	public static String[] FOOTER;

	public static Integer UPDATE_DELAY;
	public static Boolean GET_WARNED;
	public static Boolean GET_NOTIFIED;

	private static void init() {
		// Properly reading a multi-line header
		List<String> headerList = getStringList("Header");
		HEADER = headerList.toArray(new String[0]);

		List<String> footerList = getStringList("Footer");
		FOOTER = footerList.toArray(new String[0]);

		UPDATE_DELAY = getInteger("Update_Delay");
		GET_WARNED = getBoolean("Get_Warned");
		GET_NOTIFIED = getBoolean("Get_Notified");

	}
}
