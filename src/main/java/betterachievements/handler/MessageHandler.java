package betterachievements.handler;

import betterachievements.handler.message.AchievementLockUnlockMessage;
import betterachievements.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandler {
    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.ID);
    private static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(AchievementLockUnlockMessage.Handler.class, AchievementLockUnlockMessage.class, id++, Side.SERVER);
    }
}
