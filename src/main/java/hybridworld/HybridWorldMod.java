package hybridworld;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import hybridworld.world.gen.HybridTerrainGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubeGeneratorsRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = HybridWorldMod.MODID, name = HybridWorldMod.NAME, version = HybridWorldMod.VERSION, dependencies = HybridWorldMod.DEPENCIES)
public class HybridWorldMod {
	public static final String MODID = "hybridworld";	
	public static final String NAME = "Hybrid world";
	public static final String VERSION = "0.0.3";
	public static final String DEPENCIES = "required:cubicchunks@[0.0.938.0,);required:cubicgen@[0.0.54.0,);required:forge@[14.23.3.2658,)";
	HybridTerrainGenerator customCubicPopulator = new HybridTerrainGenerator();

	public static Logger LOGGER;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();
		MinecraftForge.EVENT_BUS.register(customCubicPopulator);
		CubeGeneratorsRegistry.registerForCompatibilityGenerator(customCubicPopulator);
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
