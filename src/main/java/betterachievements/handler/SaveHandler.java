package betterachievements.handler;

import betterachievements.registry.AchievementRegistry;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SaveHandler
{
    public static String[] userSetIcons = new String[0];

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        ConfigHandler.saveUserSetIcons();
        if (!event.getWorld().isRemote) {
            AchievementHandler.getInstance().dumpAchievementData(event.getWorld().getSaveHandler().getWorldDirectory());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        AchievementRegistry.instance().setUserSetIcons(userSetIcons);
        if (!event.getWorld().isRemote) {
            AchievementHandler.getInstance().compat(event.getWorld().getWorldInfo().getWorldName(), event.getWorld().getSaveHandler().getWorldDirectory());
            AchievementHandler.getInstance().constructFromData(event.getWorld().getSaveHandler().getWorldDirectory());
        }
    }
}
