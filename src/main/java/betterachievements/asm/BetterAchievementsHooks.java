package betterachievements.asm;

import betterachievements.registry.AchievementRegistry;
import net.minecraft.stats.Achievement;

public class BetterAchievementsHooks
{
    public static void addAchievement(Achievement achievement)
    {
        AchievementRegistry.instance().addAchievement(achievement);
    }
}
