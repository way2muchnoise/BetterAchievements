package betterachievements.handler;

import betterachievements.handler.message.AchievementUnlockMessage;
import betterachievements.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandler
{
    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.ID);
    private static int id = 0;

    public static void init()
    {
        INSTANCE.registerMessage(AchievementUnlockMessage.class, AchievementUnlockMessage.class, id++, Side.SERVER);
    }
}
