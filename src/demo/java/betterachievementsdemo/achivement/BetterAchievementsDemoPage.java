package betterachievementsdemo.achivement;

import betterachievements.api.components.page.ICustomIcon;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public abstract class BetterAchievementsDemoPage extends AchievementPage implements ICustomIcon
{
    public BetterAchievementsDemoPage(String name, Achievement... achievements)
    {
        super(name, achievements);
    }
}
