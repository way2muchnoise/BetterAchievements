package betterachievementsdemo;

import betterachievements.api.util.IMCHelper;
import betterachievementsdemo.proxy.CommonProxy;
import betterachievementsdemo.reference.MetaData;
import betterachievementsdemo.reference.Reference;
import betterachievementsdemo.registry.AchievementRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL)
public class BetterAchievementsDemo
{
    @Mod.Instance
    public BetterAchievementsDemo instance;

    @Mod.Metadata
    public ModMetadata metadata;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = MetaData.init(metadata);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        AchievementRegistry.registerAchievements();
        IMCHelper.sendIconForPage("demoPage5", Blocks.GOLD_BLOCK);
    }
}
