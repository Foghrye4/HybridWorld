package hybridworld.world.gen;

import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomGeneratorSettings;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.structure.feature.CubicStrongholdGenerator;

public class CubicStrongholdGeneratorWithDifferentName extends CubicStrongholdGenerator {

    public CubicStrongholdGeneratorWithDifferentName(CustomGeneratorSettings conf) {
		super(conf);
	}

	@Override 
    public String getStructureName() {
        return "CubicStronghold";
    }
}
