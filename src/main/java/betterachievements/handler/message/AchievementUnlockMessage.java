package betterachievements.handler.message;

import betterachievements.registry.AchievementRegistry;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class AchievementUnlockMessage implements IMessage, IMessageHandler<AchievementUnlockMessage, IMessage>
{
    private String achievementId;

    public AchievementUnlockMessage()
    {

    }

    public AchievementUnlockMessage(Achievement achievement)
    {
        this.achievementId = achievement.statId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int length = buf.readInt();
        this.achievementId = new String(buf.readBytes(length).array());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        byte[] bytes = this.achievementId.getBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public IMessage onMessage(final AchievementUnlockMessage message, final MessageContext ctx)
    {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isCallingFromMinecraftThread())
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
            {
                public void run()
                {
                    onServerMessage(message, ctx);
                }
            });
        }
        else
        {
            onServerMessage(message, ctx);
        }
        return null;
    }

    public void onServerMessage(AchievementUnlockMessage message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player.getServer() != null && player.getServer().getPlayerList().canSendCommands(player.getGameProfile()))
        {
            Achievement achievement = AchievementRegistry.instance().getAchievement(message.achievementId);
            unlockAchievement(achievement, player);
        }
    }

    private void unlockAchievement(Achievement achievement, EntityPlayerMP player)
    {
        List<Achievement> list = Lists.newArrayList(achievement);
        while(achievement.parentAchievement != null && !player.getStatFile().hasAchievementUnlocked(achievement.parentAchievement))
        {
            list.add(achievement.parentAchievement);
            achievement = achievement.parentAchievement;
        }

        for (Achievement achievement1 : Lists.reverse(list))
        {
            player.addStat(achievement1);
        }
    }
}
