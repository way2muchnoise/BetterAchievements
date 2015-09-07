package betterachievements;

import betterachievements.handler.ConfigHandler;
import betterachievements.handler.MessageHandler;
import betterachievements.proxy.CommonProxy;
import betterachievements.reference.MetaData;
import betterachievements.reference.Reference;
import betterachievements.registry.AchievementRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, guiFactory = Reference.MOD_GUI_FACTORY)
public class BetterAchievements
{
    @Mod.Instance
    public BetterAchievements instance;

    @Mod.Metadata
    public ModMetadata metadata;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = MetaData.init(metadata);
        if (event.getSide() == Side.CLIENT)
        {
            // Config is only needed client side since no server side things need setting
            ConfigHandler.init(event.getSuggestedConfigurationFile());
            FMLCommonHandler.instance().bus().register(new ConfigHandler());
        }
        MessageHandler.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerHandlers();
    }
    
    @NetworkCheckHandler
    public final boolean networkCheck(Map<String, String> remoteVersions, Side side)
    {
        return true;
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event)
    {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
            if (message.isItemStackMessage())
                AchievementRegistry.instance().registerIcon(message.key, message.getItemStackValue());
    }
}
