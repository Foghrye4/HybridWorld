package hybridworld.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;

public class SelectWorldTypeGui extends GuiScreen {

	private GuiCreateWorld parent;

	public SelectWorldTypeGui(GuiCreateWorld parentIn) {
		parent = parentIn;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		this.buttonList.clear();
		int l2 = WorldType.WORLD_TYPES.length/2;
		for (int id = 0; id < WorldType.WORLD_TYPES.length; id++) {
			if (WorldType.WORLD_TYPES[id] == null)
				continue;
			String typeName = I18n.format(WorldType.WORLD_TYPES[id].getTranslationKey());
			GuiButton button = new GuiButton(id, this.width / 2 - 155 + id / l2 * 155, id % l2 * 25 + 25, 150, 20, "");
			button.displayString = I18n.format("selectWorld.mapType") + " " + typeName;
			this.addButton(button);
		}
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			parent.selectedIndex = button.id;
			parent.chunkProviderSettingsJson = "";
			parent.updateDisplayState();
			parent.showMoreWorldOptions(true);
			mc.displayGuiScreen(parent);
		}
	}
}
