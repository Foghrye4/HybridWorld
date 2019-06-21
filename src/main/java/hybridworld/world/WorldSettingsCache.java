package hybridworld.world;

import static hybridworld.HybridWorldMod.LOGGER;
import static hybridworld.HybridWorldMod.MODID;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

import io.github.opencubicchunks.cubicchunks.cubicgen.CustomCubicMod;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;

/**
 * Store world settings between world creation GUI calls. Handle saving of data
 * on disk on world creation.
 */
public class WorldSettingsCache {

	private static final String VANILLA_SETTINGS_FILE = "vanilla_generator_settings.json";
	private static final String CUBIC_SETTINGS_FILE = "custom_generator_settings.json";

	public String vanillaSettings = "";
	public String customCubicSettings = "";
	public HybridWorldSettings hybridWorldSettings = new HybridWorldSettings();
	public String pendingSaveFolderLocation = "";

	public File getPresetFolder(ISaveHandler saveHandler) {
		return new File(saveHandler.getWorldDirectory(), "/data/" + MODID + "/");
	}

	public File getPresetFile(ISaveHandler saveHandler) {
		return new File(getPresetFolder(saveHandler), "vanilla_generator_settings.json");
	}

	public String loadVanillaGeneratorOptions(World world) {
		File externalGeneratorPresetFile = getPresetFile(world.getSaveHandler());
		if (externalGeneratorPresetFile.exists()) {
			try (FileReader reader = new FileReader(externalGeneratorPresetFile)) {
				CharBuffer sb = CharBuffer.allocate((int) externalGeneratorPresetFile.length());
				reader.read(sb);
				sb.flip();
				return sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public void saveCachedSettingsOnDisk(File minecraftDataDir) {
		File dataFolder = new File(minecraftDataDir, "saves/" + pendingSaveFolderLocation + "/data/");
		dataFolder.mkdirs();
		File hybridFolder = new File(dataFolder, MODID + "/");
		File cubicFile = new File(dataFolder, CustomCubicMod.MODID + "/");
		hybridFolder.mkdirs();
		cubicFile.mkdirs();
		File vanillaFile = new File(hybridFolder, VANILLA_SETTINGS_FILE);
		File hybridFile = new File(hybridFolder, HybridWorldSettings.NAME);
		cubicFile = new File(cubicFile, CUBIC_SETTINGS_FILE);
		try (FileWriter writer = new FileWriter(vanillaFile)) {
			writer.write(vanillaSettings);
			LOGGER.info("Vanilla settings file written at " + vanillaFile.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("Cannot create new directory at " + vanillaFile.getAbsolutePath());
			LOGGER.catching(e);
		}
		try (FileWriter writer = new FileWriter(cubicFile)) {
			writer.write(customCubicSettings);
			LOGGER.info("Cubic settings file written at " + cubicFile.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("Cannot create new directory at " + cubicFile.getAbsolutePath());
			LOGGER.catching(e);
		}
		try (FileWriter writer = new FileWriter(hybridFile)) {
			writer.write(HybridWorldSettings.gson().toJson(hybridWorldSettings));
			LOGGER.info("Hybrid settings file written at " + cubicFile.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("Cannot create new directory at " + cubicFile.getAbsolutePath());
			LOGGER.catching(e);
		}
	}
}
