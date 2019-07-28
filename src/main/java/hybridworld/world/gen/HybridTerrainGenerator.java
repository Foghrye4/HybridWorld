package hybridworld.world.gen;

import static hybridworld.HybridWorldMod.LOGGER;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.ICubicPopulator;
import io.github.opencubicchunks.cubicchunks.core.worldgen.generator.vanilla.VanillaCompatibilityGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.CustomCubicMod;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings.IntAABB;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomTerrainGenerator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HybridTerrainGenerator extends VanillaCompatibilityGenerator {

	private static final String FILE_NAME = "custom_generator_settings.json";

	private Int2ObjectMap<CustomTerrainGenerator> cubicGeneratorAtDimension = new Int2ObjectOpenHashMap<CustomTerrainGenerator>();
	private final MutableBlockPos mbpos = new BlockPos.MutableBlockPos();
	Int2ObjectMap<List<PopulationArea>> populatorsAtDimension = new Int2ObjectOpenHashMap<List<PopulationArea>>();

	private World world;

	public HybridTerrainGenerator(IChunkGenerator vanilla, World world) {
		super(vanilla, world);
		this.world = world;
		ImmutableList<PopulationArea> list = ImmutableList.<PopulationArea>builder().build();
		populatorsAtDimension.defaultReturnValue(list);
		this.onLoad(world);
	}

	public void onLoad(World world) {
		if (world.isRemote || !(world instanceof WorldServer))
			return;
		int dimension = world.provider.getDimension();
		String settingJsonString = loadJsonStringFromSaveFolder(world, FILE_NAME);
		if (settingJsonString == null) {
			return;
		}
		CustomGeneratorSettings settings = CustomGeneratorSettings.fromJson(settingJsonString);
		settings.strongholds = false;
		for (Entry<IntAABB, CustomGeneratorSettings> entry : settings.cubeAreas.entrySet()) {
			entry.getValue().strongholds = false;
		}
		ArrayList<PopulationArea> areas = new ArrayList<PopulationArea>();
		areas.add(new PopulationArea(settings));
		this.addPopulationAreasToList(areas, settings);
		populatorsAtDimension.put(dimension, areas);
		CustomTerrainGenerator cubicGenerator = new CustomTerrainGenerator(world, world.getBiomeProvider(), settings,
				world.getSeed());
		cubicGeneratorAtDimension.put(dimension, cubicGenerator);
	}

	private void addPopulationAreasToList(List<PopulationArea> areas, CustomGeneratorSettings setting) {
		for (Entry<IntAABB, CustomGeneratorSettings> entry : setting.cubeAreas.entrySet()) {
			areas.add(new PopulationArea(entry.getKey(), entry.getValue()));
			this.addPopulationAreasToList(areas, entry.getValue());
		}
	}

    @Override
    public CubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
		if (cubeY >= 0 && cubeY < 16)
			return super.generateCube(cubeX, cubeY, cubeZ);
		int dimension = world.provider.getDimension();
		CustomTerrainGenerator cubicGenerator = cubicGeneratorAtDimension.get(dimension);
		if (cubicGenerator == null)
			return super.generateCube(cubeX, cubeY, cubeZ);
		return cubicGenerator.generateCube(cubeX, cubeY, cubeZ);
    }

	@Override
    public void populate(ICube cube) {
		List<PopulationArea> areas = populatorsAtDimension.get(world.provider.getDimension());
		for (PopulationArea area : areas) {
			area.generateIfInArea(world, world.rand, cube.getCoords(), cube.getBiome(cube.getCoords().getCenterBlockPos()));
		}
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

}
