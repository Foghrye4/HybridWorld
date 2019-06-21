package hybridworld.world;

import hybridworld.HybridWorldMod;
import hybridworld.client.gui.CustomizeHybridWorldGui;
import hybridworld.world.gen.HybridTerrainGenerator;
import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HybridWorldType extends WorldType implements ICubicWorldType {

	private HybridWorldType() {
		super("Hybrid");
	}

	public static HybridWorldType create() {
		return new HybridWorldType();
	}

	@Override
	public IntRange calculateGenerationHeightRange(WorldServer world) {
		CustomGeneratorSettings opts = CustomGeneratorSettings.load(world);
		return new IntRange(0, (int) opts.actualHeight);
	}

	@Override
	public boolean hasCubicGeneratorForWorld(World world) {
		return world.provider.getClass() == WorldProviderSurface.class;
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
        if (world.isRemote)
            return new BiomeProviderSingle(Biomes.PLAINS);
        HybridWorldSettings settings = HybridWorldSettings.load(world);
        WorldType vanilla = WorldType.parseWorldType(settings.worldTypeVanilla);
		return vanilla.getBiomeProvider(world);
	}
	
	@Override
	public ICubeGenerator createCubeGenerator(World world) {
        HybridWorldSettings settings = HybridWorldSettings.load(world);
        WorldType vanilla = WorldType.parseWorldType(settings.worldTypeVanilla);
		return new HybridTerrainGenerator(world, vanilla.getChunkGenerator(world, HybridWorldMod.worldSettingsCache.loadVanillaGeneratorOptions(world)));
	}
	
	@Override
    public void onGUICreateWorldPress() {
		HybridWorldMod.worldSettingsCache.saveCachedSettingsOnDisk(HybridWorldMod.proxy.getMinecraftFolder());
	}
	
	@Override
    public boolean isCustomizable() {
        return true;
    }
	
    @SideOnly(Side.CLIENT)
    public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
            mc.displayGuiScreen(new CustomizeHybridWorldGui(guiCreateWorld));
    }
}
