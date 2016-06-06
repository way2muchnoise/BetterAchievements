package betterachievements.handler.message;

import betterachievements.registry.AchievementRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Stack;

public class AchievementLockUnlockMessage implements IMessage
{
    private String achievementId;
    private boolean lockUnlock; // true for unlock

    public AchievementLockUnlockMessage() {}

    public AchievementLockUnlockMessage(Achievement achievement, boolean lockUnlock)
    {
        this.achievementId = achievement.statId;
        this.lockUnlock = lockUnlock;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.lockUnlock = buf.readBoolean();
        int length = buf.readInt();
        this.achievementId = new String(buf.readBytes(length).array());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.lockUnlock);
        byte[] bytes = this.achievementId.getBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static class Handler implements IMessageHandler<AchievementLockUnlockMessage, IMessage>
    {
        public static boolean opLockUnlock = true;

        @Override
        public IMessage onMessage(final AchievementLockUnlockMessage message, final MessageContext ctx)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> onServerMessage(message, ctx));
            return null;
        }

        public void onServerMessage(AchievementLockUnlockMessage message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (canLockUnlock(player))
            {
                Achievement achievement = AchievementRegistry.instance().getAchievement(message.achievementId);
                if (message.lockUnlock)
                    unlockAchievement(achievement, player);
                else
                    lockAchievement(achievement, player);
            }
        }

        private void unlockAchievement(Achievement achievement, EntityPlayerMP player)
        {
            Stack<Achievement> stack = new Stack<>();
            stack.push(achievement);
            while (achievement.parentAchievement != null && !player.getStatFile().hasAchievementUnlocked(achievement.parentAchievement))
            {
                stack.push(achievement.parentAchievement);
                achievement = achievement.parentAchievement;
            }
            while (!stack.isEmpty()) player.addStat(stack.pop());
        }

        private void lockAchievement(Achievement achievement, EntityPlayerMP player)
        {
            AchievementRegistry.instance().getAllChildren(achievement).forEach(player::takeStat);
        }

        private boolean canLockUnlock(EntityPlayerMP player)
        {
            if (opLockUnlock) return player.getServer() != null && player.getServer().getPlayerList().canSendCommands(player.getGameProfile());
            else return player.isCreative();
        }
    }
}
