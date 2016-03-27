package betterachievements.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraftforge.event.entity.player.AchievementEvent;

import java.util.*;

public class AchievementHandler
{
    private final Map<UUID, Set<Achievement>> playerAchievementMap;

    public AchievementHandler()
    {
        this.playerAchievementMap = new HashMap<>();
    }

    public void onAchievementUnlocked(AchievementEvent event)
    {
        if (event.getEntityPlayer() instanceof EntityPlayerMP && !((EntityPlayerMP) event.getEntityPlayer()).getStatFile().canUnlockAchievement(event.getAchievement()))
        {
            addAchievementToMap(event.getEntityPlayer().getUniqueID(), event.getAchievement());
            tryUnlock((EntityPlayerMP) event.getEntityPlayer());
        }
    }

    public void addAchievementToMap(UUID uuid, Achievement achievement)
    {
        Set<Achievement> achievements = this.playerAchievementMap.get(uuid);
        if (achievements == null) achievements = new HashSet<>();
        achievements.add(achievement);
        this.playerAchievementMap.put(uuid, achievements);
    }

    public void tryUnlock(EntityPlayerMP player)
    {
        Set<Achievement> achievements = this.playerAchievementMap.get(player.getUniqueID());
        boolean doItr = achievements != null;
        while (doItr)
        {
            doItr = false;
            Iterator<Achievement> itr = achievements.iterator();
            while (itr.hasNext())
            {
                Achievement current = itr.next();
                if (player.getStatFile().canUnlockAchievement(current))
                {
                    player.addStat(current);
                    itr.remove();
                    doItr = true;
                }
            }
        }
    }
}
