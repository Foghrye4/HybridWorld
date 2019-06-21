package hybridworld.client.gui;

import hybridworld.HybridWorldMod;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.gui.CustomCubicGui;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler {
	private boolean redirectGuiOutputToVanillaSettings = false;
	private boolean redirectGuiOutputToCustomCubicSettings = false;
	private CustomizeHybridWorldGui hybridWorldGui;

	@SubscribeEvent
	public void onBeforeGuiOpen(GuiOpenEvent action) {
		GuiScreen nextScreen = action.getGui();
		if (!(nextScreen instanceof GuiCreateWorld))
			return;
		// If this is called by customization GUIs of hybrid world we will show hybrid
		// world Gui instead
		if (redirectGuiOutputToVanillaSettings) {
			action.setGui(hybridWorldGui);
			HybridWorldMod.worldSettingsCache.vanillaSettings = ((GuiCreateWorld) nextScreen).chunkProviderSettingsJson;
		}
		if (redirectGuiOutputToCustomCubicSettings) {
			action.setGui(hybridWorldGui);
			HybridWorldMod.worldSettingsCache.customCubicSettings = ((GuiCreateWorld) nextScreen).chunkProviderSettingsJson;
		}
		redirectGuiOutputToVanillaSettings = false;
		redirectGuiOutputToCustomCubicSettings = false;
	}

	@SubscribeEvent
	public void onButtonPressed(GuiScreenEvent.ActionPerformedEvent.Pre action) {
		if (!(action.getGui() instanceof GuiCreateWorld))
			return;
		if (action.getButton().id == 0) {
			HybridWorldMod.worldSettingsCache.pendingSaveFolderLocation = ((GuiCreateWorld) action
					.getGui()).saveDirName;
		}
		if (action.getButton().id != 5)
			return;
		action.setCanceled(true);
		action.getGui().mc.displayGuiScreen(new SelectWorldTypeGui((GuiCreateWorld) action.getGui()));
	}

	public void redirectGuiOutputToVanillaSettings(CustomizeHybridWorldGui hybridWorldGuiIn) {
		hybridWorldGui = hybridWorldGuiIn;
		redirectGuiOutputToVanillaSettings = true;
	}

	public void redirectGuiOutputToCustomCubicSettings(CustomizeHybridWorldGui hybridWorldGuiIn) {
		hybridWorldGui = hybridWorldGuiIn;
		redirectGuiOutputToCustomCubicSettings = true;
	}
}
