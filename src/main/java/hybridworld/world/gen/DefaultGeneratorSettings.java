package hybridworld.world.gen;

import java.util.Arrays;

import javax.annotation.Nullable;

import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings.StandardOreConfig;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class DefaultGeneratorSettings {
	
	private static final int NETHER = -1;
	private static final int OVERWORLD = 0;
	private static final int END = 1;

	/** @return null for non-vanilla dimensions */
	@Nullable
	public static CustomGeneratorSettings get(int dimension) {
		switch(dimension) {
		case OVERWORLD:
			return CustomGeneratorSettings.defaults();
		case NETHER:
			return createForNether();
		case END:
			return createForEnd();
		}
		return null;
	}
	
    public static CustomGeneratorSettings createForNether() {
        CustomGeneratorSettings settings = new CustomGeneratorSettings();
        settings.waterLevel = 0;
        settings.caves = true;

        settings.strongholds = false;
        settings.alternateStrongholdsPositions = false;
        settings.villages = false;

        settings.mineshafts = true;
        settings.temples = false;

        settings.oceanMonuments = false;
        settings.woodlandMansions = false;

        settings.ravines = true;
        settings.dungeons = true;

        settings.dungeonCount = 1;
        settings.waterLakes = false;
        
        settings.lavaLakes = true;
        settings.lavaLakeRarity = 1;
        settings.aboveSeaLavaLakeRarity = 1;
        settings.lavaOceans = true;

        settings.biome = Biome.getIdForBiome(Biomes.HELL);
        settings.biomeSize = 4;
        settings.riverSize = 4;

        settings.heightVariationFactor = 20000;
        settings.specialHeightVariationFactorBelowAverageY = 64f;
        settings.heightVariationOffset = 1536;
        settings.heightFactor = -1638;
        settings.heightOffset = 53248;

        settings.depthNoiseFactor = 1.02f;
        settings.depthNoiseOffset = -0.03f;
        settings.depthNoiseFrequencyX = 1f/229;
        settings.depthNoiseFrequencyZ = 1f/229;
        settings.depthNoiseOctaves = 4;

        settings.selectorNoiseFactor = 12.8f;
        settings.selectorNoiseOffset = -0.03f;
        settings.selectorNoiseFrequencyX = 1f/116;
        settings.selectorNoiseFrequencyY = 1f/26;
        settings.selectorNoiseFrequencyZ = 1f/116;
        settings.selectorNoiseOctaves = 3;

        settings.lowNoiseFactor = 1.0f;
        settings.lowNoiseOffset = -0.03f;
        settings.lowNoiseFrequencyX = 1f/72;
        settings.lowNoiseFrequencyY = 1f/80;
        settings.lowNoiseFrequencyZ = 1f/72;
        settings.lowNoiseOctaves = 3;

        settings.highNoiseFactor = 1;
        settings.highNoiseOffset = -0.3f;
        settings.highNoiseFrequencyX = 1f/77;
        settings.highNoiseFrequencyY = 1f/82;
        settings.highNoiseFrequencyZ = 1f/77;
        settings.highNoiseOctaves = 2;
        settings.replacerConfig.setDefault(new ResourceLocation("cubicgen:terrain_fill_block"), Blocks.NETHERRACK.getDefaultState());
        settings.replacerConfig.setDefault(new ResourceLocation("cubicgen:ocean_block"), Blocks.LAVA.getDefaultState());
        
        {
            settings.standardOres.addAll(Arrays.asList(
                    StandardOreConfig.builder()
                    		.genInBlockstates(Blocks.NETHERRACK.getDefaultState())
                            .block(Blocks.QUARTZ_ORE.getDefaultState())
                            .size(17).attempts(10).probability(1f / (256f / ICube.SIZE)).create(),
                    StandardOreConfig.builder()
            				.genInBlockstates(Blocks.NETHERRACK.getDefaultState())
                            .block(Blocks.GLOWSTONE.getDefaultState())
                            .size(6).attempts(8).probability(2f / (256f / ICube.SIZE)).create(),
                    StandardOreConfig.builder()
            				.genInBlockstates(Blocks.NETHERRACK.getDefaultState())
                    		.block(Blocks.SOUL_SAND.getDefaultState())
                    		.size(33).attempts(10).probability(1f / (256f / ICube.SIZE)).create(),
                    StandardOreConfig.builder()
            				.genInBlockstates(Blocks.NETHERRACK.getDefaultState())
                    		.block(Blocks.GRAVEL.getDefaultState())
                    		.size(33).attempts(10).probability(1f / (256f / ICube.SIZE)).create(),
                    StandardOreConfig.builder()
            				.genInBlockstates(Blocks.NETHERRACK.getDefaultState())
                    		.block(Blocks.LAVA.getDefaultState())
                    		.size(1).attempts(10).probability(1f / (256f / ICube.SIZE)).create(),
                    StandardOreConfig.builder()
                    		.genInBlockstates(Blocks.NETHERRACK.getDefaultState())
                    		.block(Blocks.MAGMA.getDefaultState())
                    		.size(33).attempts(10).probability(4f / (256f / ICube.SIZE)).create()
                    ));
        }
        return settings;
    }
    
    public static CustomGeneratorSettings createForEnd() {
        CustomGeneratorSettings settings = new CustomGeneratorSettings();
        settings.waterLevel = Integer.MIN_VALUE;
        settings.caves = true;

        settings.strongholds = false;
        settings.alternateStrongholdsPositions = false;
        settings.villages = false;

        settings.mineshafts = false;
        settings.temples = false;

        settings.oceanMonuments = false;
        settings.woodlandMansions = false;

        settings.ravines = true;
        settings.dungeons = false;

        settings.dungeonCount = 7;
        settings.waterLakes = false;
        
        settings.lavaLakes = false;
        settings.lavaLakeRarity = 1;
        settings.aboveSeaLavaLakeRarity = 1;
        settings.lavaOceans = false;

        settings.biome = Biome.getIdForBiome(Biomes.SKY);
        settings.biomeSize = 4;
        settings.riverSize = 4;

        settings.heightVariationFactor = 9216;
        settings.specialHeightVariationFactorBelowAverageY = 64f;
        settings.heightVariationOffset = 1536;
        settings.heightFactor = 0.64f;// height scale
        settings.heightOffset = 160;// sea level

        settings.depthNoiseFactor = 1f;
        settings.depthNoiseOffset = 0.5f;
        settings.depthNoiseFrequencyX = 1f/220;
        settings.depthNoiseFrequencyZ = 1f/220;
        settings.depthNoiseOctaves = 4;

        settings.selectorNoiseFactor = 12f;
        settings.selectorNoiseOffset = -0.4f;
        settings.selectorNoiseFrequencyX = 1f/21;
        settings.selectorNoiseFrequencyY = 1f/120;
        settings.selectorNoiseFrequencyZ = 1f/21;
        settings.selectorNoiseOctaves = 3;

        settings.lowNoiseFactor = 1f;
        settings.lowNoiseOffset = -0.3f;
        settings.lowNoiseFrequencyX = 1f/72;
        settings.lowNoiseFrequencyY = 1f/80;
        settings.lowNoiseFrequencyZ = 1f/72;
        settings.lowNoiseOctaves = 3;

        settings.highNoiseFactor = 1f;
        settings.highNoiseOffset = -0.6f;
        settings.highNoiseFrequencyX = 1f/76;
        settings.highNoiseFrequencyY = 1f/82;
        settings.highNoiseFrequencyZ = 1f/76;
        settings.highNoiseOctaves = 1;
        settings.replacerConfig.setDefault(new ResourceLocation("cubicgen:terrain_fill_block"), Blocks.END_STONE.getDefaultState());
        settings.replacerConfig.setDefault(new ResourceLocation("cubicgen:ocean_block"), Blocks.AIR.getDefaultState());
        
        return settings;
    }
}
