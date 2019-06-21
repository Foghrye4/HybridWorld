package hybridworld;

import org.apache.logging.log4j.Logger;

import hybridworld.world.HybridWorldType;
import hybridworld.world.WorldSettingsCache;
import hybridworld.world.gen.HybridTerrainGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubeGeneratorsRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = HybridWorldMod.MODID, name = HybridWorldMod.NAME, version = HybridWorldMod.VERSION, dependencies = HybridWorldMod.DEPENCIES)
public class HybridWorldMod {
	public static final String MODID = "hybridworld";
	public static final String NAME = "Hybrid world";
	public static final String VERSION = "0.0.1";
	public static final String DEPENCIES = "required:cubicchunks@[0.0.938.0,);required:cubicgen@[0.0.39.0,);required:forge@[14.23.3.2658,)";
	@SidedProxy(clientSide = MODID+".ClientProxy", serverSide = MODID+".ServerProxy")
	public static ServerProxy proxy= new ServerProxy();
	HybridTerrainGenerator customCubicPopulator = new HybridTerrainGenerator();

	public static Logger LOGGER;
	public static WorldSettingsCache worldSettingsCache = new WorldSettingsCache();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();
		proxy.init();
		HybridWorldType.create();
		CubeGeneratorsRegistry.registerForCompatibilityGenerator(customCubicPopulator);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
	}
}
