package hybridworld.world.gen;

import static hybridworld.HybridWorldMod.LOGGER;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.core.worldgen.generator.vanilla.VanillaCompatibilityGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.CustomCubicMod;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomTerrainGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;

public class HybridTerrainGenerator extends VanillaCompatibilityGenerator {

	private static final String FILE_NAME = "custom_generator_settings.json";
	private CustomTerrainGenerator cubicGenerator;
	
	public HybridTerrainGenerator(IChunkGenerator vanilla, World world) {
		super(vanilla, world);
		this.onLoad(world);
	}

	public void onLoad(World world) {
		int dimension = world.provider.getDimension();
		String settingJsonString = loadJsonStringFromSaveFolder(world, FILE_NAME);
		CustomGeneratorSettings settings = null;
		if (settingJsonString != null) {
			settings = CustomGeneratorSettings.fromJson(settingJsonString);
		} else {
			settings = DefaultGeneratorSettings.get(dimension);
			if (settings != null)
				saveJsonStringToSaveFolder(world, FILE_NAME, settings.toJson());
		}
		if (settings == null)
			return;
		this.cubicGenerator = new CustomTerrainGenerator(world, world.getBiomeProvider(), settings, world.getSeed());
	}

	@Override
	public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
		if (cubeY >= 0 && cubeY < 16)
			return super.generateCube(cubeX, cubeY, cubeZ);
		if (cubicGenerator == null)
			return super.generateCube(cubeX, cubeY, cubeZ);
		return cubicGenerator.generateCube(cubeX, cubeY, cubeZ);
	}

	@Override
	public void populate(ICube cube) {
		super.populate(cube);
		if (cubicGenerator != null)
			cubicGenerator.populate(cube);
	}

	private static File getSettingsFile(World world, String fileName) {
		File worldDirectory = world.getSaveHandler().getWorldDirectory();
		String subfolder = world.provider.getSaveFolder();
		if (subfolder == null)
			subfolder = "";
		else
			subfolder += "/";
		File settings = new File(worldDirectory, "./" + subfolder + "data/" + CustomCubicMod.MODID + "/" + fileName);
		return settings;
	}

	public static String loadJsonStringFromSaveFolder(World world, String fileName) {
		File settings = getSettingsFile(world, fileName);
		if (settings.exists()) {
			try (FileReader reader = new FileReader(settings)) {
				CharBuffer sb = CharBuffer.allocate((int) settings.length());
				reader.read(sb);
				sb.flip();
				return sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("No settings provided at path:" + settings.toString());
		}
		return null;
	}
	
	public static void saveJsonStringToSaveFolder(World world, String fileName, String json) {
		File settings = getSettingsFile(world, fileName);
		settings.getParentFile().mkdirs();
		try (FileWriter writer = new FileWriter(settings)) {
			writer.write(json);
			CustomCubicMod.LOGGER.info("Default generator settings saved at " + settings.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Override
	public BlockPos getClosestStructure(String name, BlockPos pos, boolean findUnexplored) {
		BlockPos vanillaStructurePos = super.getClosestStructure(name, pos, findUnexplored);
		if (cubicGenerator == null || (!name.equals("Strongholds") && !name.equals("CubicStrongholds")))
			return vanillaStructurePos;
		BlockPos cubicPos = cubicGenerator.getClosestStructure("Strongholds", pos, findUnexplored);
		if (vanillaStructurePos == null) {
			return cubicPos;
		}
		if (cubicPos == null) {
			return vanillaStructurePos;
		} else if (pos.distanceSq(cubicPos) < pos.distanceSq(vanillaStructurePos)) {
			return cubicPos;
		}
		return vanillaStructurePos;
	}
}
