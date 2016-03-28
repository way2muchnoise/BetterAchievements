package betterachievements.proxy;

import betterachievements.handler.AchievementHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

public class CommonProxy
{
    public void registerHandlers()
    {
        MinecraftForge.EVENT_BUS.register(AchievementHandler.getInstance());
    }

    public void initConfig(File file)
    {

    }
}
