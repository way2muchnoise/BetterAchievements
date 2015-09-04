package betterachievements.proxy;

import betterachievements.handler.GuiOpenHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerHandlers()
    {
        MinecraftForge.EVENT_BUS.register(new GuiOpenHandler());
    }
}
