package betterachievementsdemo.betterachivements;

import betterachievements.api.components.page.ICustomIcon;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public abstract class DemoPage extends AchievementPage implements ICustomIcon
{
    public DemoPage(String name, Achievement... achievements)
    {
        super(name, achievements);
    }
}
