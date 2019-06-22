package hybridworld.world.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.ICubicPopulator;
import io.github.opencubicchunks.cubicchunks.cubicgen.common.biome.CubicBiome;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings.IntAABB;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PopulationArea {
	private final IntAABB area;
    private final Map<Biome, ICubicPopulator> populators = new HashMap<>();
	
	public PopulationArea(CustomGeneratorSettings settingsIn) {
		this(new IntAABB(-300000, -300000, -300000, 300000, 300000, 300000), settingsIn);
	}
	
	public PopulationArea(IntAABB areaIn, CustomGeneratorSettings settingsIn) {
		area = areaIn;
        for (Biome biome : ForgeRegistries.BIOMES) {
            CubicBiome cubicBiome = CubicBiome.getCubic(biome);
            populators.put(biome, cubicBiome.getDecorator(settingsIn));
        }
	}

	public void generateIfInArea(World world, Random random, CubePos pos, Biome biome) {
		if (area.contains(pos.getX(), pos.getY(), pos.getZ())) {
			populators.get(biome).generate(world, random, pos, biome);
		}
	}
}