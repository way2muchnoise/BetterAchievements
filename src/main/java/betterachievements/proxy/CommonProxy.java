package betterachievements.proxy;

import betterachievements.handler.AchievementHandler;
import betterachievements.handler.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

public class CommonProxy {
    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(AchievementHandler.getInstance());
    }

    public void initConfig(File configDir) {
        ConfigHandler.initConfigDir(configDir);
    }
}
