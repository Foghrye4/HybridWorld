package hybridworld.world.gen;

import static hybridworld.HybridWorldMod.LOGGER;

import java.util.Random;

import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.ICubicPopulator;
import io.github.opencubicchunks.cubicchunks.core.worldgen.generator.vanilla.VanillaCompatibilityGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomTerrainGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HybridTerrainGenerator implements ICubicPopulator {
	CustomTerrainGenerator cubicGenerator;
	VanillaCompatibilityGenerator vanillaGenerator;

	public HybridTerrainGenerator() {}
	
	@SubscribeEvent
	public void onWorldLoadEvent(WorldEvent.Load event) {
		World world = event.getWorld();
		if (world.isRemote || !(world instanceof WorldServer))
			return;
		String settingJsonString = CustomGeneratorSettings.loadJsonStringFromSaveFolder(world.getSaveHandler());
		if (settingJsonString == null) {
			LOGGER.error(
					"Can't load settings from path:" + CustomGeneratorSettings.getPresetFile(world.getSaveHandler()));
			settingJsonString = "";
		}
		CustomGeneratorSettings cubicSettings = CustomGeneratorSettings.fromJson(settingJsonString);
		cubicGenerator = new CustomTerrainGenerator(world, cubicSettings, world.getSeed());
	}

	@Override
	public void generate(World arg0, Random arg1, CubePos arg2, Biome arg3) {
		cubicGenerator.generateCube(arg2.getX(), arg2.getY(), arg2.getZ());
	}
}
