package betterachievements.registry;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import java.util.LinkedList;
import java.util.List;

public final class AchievementRegistry
{
    private static AchievementRegistry instance;
    public static final AchievementPage mcPage = new AchievementPage("minecraft");
    private List<Achievement> mcAchievements;

    public static AchievementRegistry instance()
    {
        if (instance == null)
            instance = new AchievementRegistry();
        return instance;
    }

    private AchievementRegistry()
    {
        mcAchievements = new LinkedList<Achievement>();
    }

    public void addAchievement(Achievement achievement)
    {
        if (!AchievementPage.isAchievementInPages(achievement))
            mcAchievements.add(achievement);
    }

    public List<Achievement> getAchievements(AchievementPage page)
    {
        return page == mcPage ? mcAchievements : page.getAchievements();
    }
}
