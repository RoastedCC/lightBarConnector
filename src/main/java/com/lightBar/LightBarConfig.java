package com.lightBar;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("LightBarPlugin")
public interface LightBarConfig extends Config
{
	@ConfigItem(
		keyName = "COM PORT",
		name = "COM port",
		description = ""
	)
	default String COMP(){
		return "COM1";
	}
}
