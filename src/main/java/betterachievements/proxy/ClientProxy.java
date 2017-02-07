package betterachievements.proxy;

import betterachievements.gui.GuiBetterAchievement;
import betterachievements.handler.ConfigHandler;
import betterachievements.handler.GuiOpenHandler;
import betterachievements.handler.SaveHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new GuiOpenHandler());
        MinecraftForge.EVENT_BUS.register(new SaveHandler());

        Minecraft.getMinecraft().guiAchievement = new GuiBetterAchievement(Minecraft.getMinecraft());
    }

    @Override
    public void initConfig(File configDir) {
        super.initConfig(configDir);
        ConfigHandler.init();
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }
}
