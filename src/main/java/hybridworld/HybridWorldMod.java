package hybridworld;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import hybridworld.world.gen.HybridTerrainGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubeGeneratorsRegistry;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.VanillaCompatibilityGeneratorProviderBase;
import io.github.opencubicchunks.cubicchunks.core.worldgen.generator.vanilla.VanillaCompatibilityGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = HybridWorldMod.MODID, name = HybridWorldMod.NAME, version = HybridWorldMod.VERSION, dependencies = HybridWorldMod.DEPENCIES)
public class HybridWorldMod {
	public static final String MODID = "hybridworld";	
	public static final String NAME = "Hybrid world";
	public static final String VERSION = "0.1.0";
	public static final String DEPENCIES = "required:cubicchunks@[0.0.973.0,);required:cubicgen@[0.0.54.0,);required:forge@[14.23.3.2658,)";

	public static Logger LOGGER;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();
		MinecraftForge.EVENT_BUS.register(new Object() {
		    @SubscribeEvent
		    public void registerVanillaCompatibilityGeneratorProvider(RegistryEvent.Register<VanillaCompatibilityGeneratorProviderBase> event) {
		        event.getRegistry().register(new VanillaCompatibilityGeneratorProviderBase() {

		            @Override
		            public ICubeGenerator provideGenerator(IChunkGenerator vanillaChunkGenerator, World world) {
		                return new HybridTerrainGenerator(vanillaChunkGenerator, world);
		            }
		        }.setRegistryName(new ResourceLocation(MODID, "hybrid"))
		                .setUnlocalizedName("hybrid.gui.worldmenu.type"));
		    }
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
	}
	
	@NetworkCheckHandler
	public boolean checkModLists(Map<String, String> modList, Side sideIn) {
		return true;
	}
}
