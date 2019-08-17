package hybridworld.world.gen;

import java.lang.reflect.Field;

import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.event.InitCubicStructureGeneratorEvent;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.structure.feature.CubicStrongholdGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StructureInitEventHandler {
	
	@SubscribeEvent
	public void onStructureInitEvent(InitCubicStructureGeneratorEvent event) {
		if(event.getOriginalGen() instanceof CubicStrongholdGenerator) {
			try {
				Field fld = CubicStrongholdGenerator.class.getDeclaredField("conf");
				fld.setAccessible(true);
				CustomGeneratorSettings conf = (CustomGeneratorSettings) fld.get(event.getOriginalGen());
				event.setNewGen(new CubicStrongholdGeneratorWithDifferentName(conf));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			
		}
	}
}
