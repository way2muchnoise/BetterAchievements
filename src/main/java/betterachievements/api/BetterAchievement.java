package betterachievements.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

/**
 * Default implementation of {@link IBetterAchievement}
 */
public class BetterAchievement extends Achievement implements IBetterAchievement
{
    public BetterAchievement(String id, String name, int column, int row, Item item, Achievement parent)
    {
        super(id, name, column, row, item, parent);
    }

    public BetterAchievement(String id, String name, int column, int row, Block block, Achievement parent)
    {
        super(id, name, column, row, block, parent);
    }

    public BetterAchievement(String id, String name, int column, int row, ItemStack itemStack, Achievement parent)
    {
        super(id, name, column, row, itemStack, parent);
    }

    @Override
    public boolean recolourBackground()
    {
        return false;
    }

    @Override
    public int recolourBackground(float greyScale)
    {
        return 0;
    }

    @Override
    public boolean hasSpecialIconRenderer()
    {
        return false;
    }

    @Override
    public void renderIcon(int top, int left)
    {

    }
}
