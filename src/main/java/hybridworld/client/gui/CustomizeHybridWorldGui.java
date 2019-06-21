package hybridworld.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import hybridworld.ClientProxy;
import hybridworld.HybridWorldMod;
import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.CustomCubicWorldType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;

public class CustomizeHybridWorldGui extends GuiScreen {

	int selectedVanillaType = 0;
	private GuiButton btnMapType;
	private GuiCreateWorld parent;

	public CustomizeHybridWorldGui(GuiCreateWorld parentIn) {
		parent = parentIn;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		this.buttonList.clear();
		btnMapType = new GuiButton(0, this.width / 2 - 155, this.height / 2 - 28, 150, 20, "");
		btnMapType.displayString = I18n.format("selectWorld.mapType") + " "
				+ I18n.format(WorldType.WORLD_TYPES[this.selectedVanillaType].getTranslationKey());
		this.buttonList.add(btnMapType);
		this.buttonList.add(new GuiButton(1, this.width / 2 - 155, this.height / 2, 150, 20,
				I18n.format("hybrid.customizeVanilla")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 155, this.height / 2 + 28, 150, 20,
				I18n.format("hybrid.customizeCubic")));
		this.buttonList.add(
				new GuiButton(3, this.width / 2 - 155, this.height / 2 + 28 * 2, 150, 20, I18n.format("hybrid.done")));

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (!button.enabled)
			return;
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(new SelectVanillaWorldTypeGui(this));
			break;
		case 1:
			ClientProxy proxy = (ClientProxy) HybridWorldMod.proxy;
			proxy.guiEventHandler.redirectGuiOutputToVanillaSettings(this);
			WorldType.WORLD_TYPES[this.selectedVanillaType].onCustomizeButton(mc, parent);
			break;
		case 2:
			proxy = (ClientProxy) HybridWorldMod.proxy;
			proxy.guiEventHandler.redirectGuiOutputToCustomCubicSettings(this);
			WorldType.parseWorldType("CustomCubic").onCustomizeButton(mc, parent);
			break;
		case 3:
			HybridWorldMod.worldSettingsCache.hybridWorldSettings.worldTypeVanilla = WorldType.WORLD_TYPES[this.selectedVanillaType]
					.getName();
			mc.displayGuiScreen(parent);
			break;
		default:
			break;
		}
	}

	public void updateDisplayState() {
		this.btnMapType.displayString = I18n.format("selectWorld.mapType") + " "
				+ I18n.format(WorldType.WORLD_TYPES[this.selectedVanillaType].getTranslationKey());
	}
}
