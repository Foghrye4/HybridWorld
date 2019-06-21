package hybridworld;

import java.io.File;

import hybridworld.client.gui.GuiEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends ServerProxy {

	public GuiEventHandler guiEventHandler = new GuiEventHandler();
	
	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(guiEventHandler);
	}
	
	@Override
	public File getMinecraftFolder() {
		return Minecraft.getMinecraft().mcDataDir;
	}

}
