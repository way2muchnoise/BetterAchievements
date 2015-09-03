package betterachievements.api;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * Default implementation of {@link IBetterAchievementPage}
 */
public class BetterAchievementPage extends AchievementPage implements IBetterAchievementPage
{
    public BetterAchievementPage(String name, Achievement... achievements)
    {
        super(name, achievements);
    }

    @Override
    public boolean hasCustomBackGround()
    {
        return false;
    }

    @Override
    public void drawBackground(int left, int top, float z, float scale)
    {

    }

    @Override
    public boolean setScaleOnLoad()
    {
        return false;
    }

    @Override
    public float setScale()
    {
        return 1.0F;
    }

    @Override
    public Achievement setPositionOnLoad()
    {
        return null;
    }

    @Override
    public float getMaxZoom()
    {
        return 2.0F;
    }

    @Override
    public float getMinZoom()
    {
        return 1.0F;
    }

    @Override
    public ItemStack getPageIcon()
    {
        return null;
    }
}
