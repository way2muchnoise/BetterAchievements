package betterachievements;

import betterachievements.handler.MessageHandler;
import betterachievements.proxy.CommonProxy;
import betterachievements.reference.MetaData;
import betterachievements.reference.Reference;
import betterachievements.registry.AchievementRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, guiFactory = Reference.MOD_GUI_FACTORY)
public class BetterAchievements {
    @Mod.Instance
    public BetterAchievements instance;

    @Mod.Metadata
    public ModMetadata metadata;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        metadata = MetaData.init(metadata);
        proxy.initConfig(event.getModConfigurationDirectory());
        MessageHandler.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerHandlers();
    }

    @NetworkCheckHandler
    public final boolean networkCheck(Map<String, String> remoteVersions, Side side) {
        return true;
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
            if (message.isItemStackMessage())
                AchievementRegistry.instance().registerIcon(message.key, message.getItemStackValue(), false);
    }
}
