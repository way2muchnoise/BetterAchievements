package betterachievements.gui;

import betterachievements.handler.ConfigHandler;
import betterachievements.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {
    public ModGuiConfig(GuiScreen guiScreen) {
        super(guiScreen,
            ConfigHandler.getConfigElements(),
            Reference.ID,
            false,
            false,
            GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }
}
