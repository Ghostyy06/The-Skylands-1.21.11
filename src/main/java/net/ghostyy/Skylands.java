package net.ghostyy;

import net.fabricmc.api.ModInitializer;

import net.ghostyy.effect.SkylandsEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skylands implements ModInitializer {
	public static final String MOD_ID = "skylands";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		SkylandsEffects.init();

	}
}