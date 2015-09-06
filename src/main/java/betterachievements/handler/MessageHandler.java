package betterachievements.handler;

import betterachievements.handler.message.AchievementUnlockMessage;
import betterachievements.reference.Reference;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class MessageHandler
{
    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.ID);
    private static int id = 0;

    public static void init()
    {
        INSTANCE.registerMessage(AchievementUnlockMessage.class, AchievementUnlockMessage.class, id++, Side.SERVER);
    }
}
