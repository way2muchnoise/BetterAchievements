package betterachievementsdemo.achivement;

import betterachievements.api.components.achievement.ICustomBackgroundColour;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public abstract class BetterAchievementsDemoAchievement extends Achievement implements ICustomBackgroundColour
{
    public BetterAchievementsDemoAchievement(String id, String name, int column, int row, Item item, Achievement parent)
    {
        super(id, name, column, row, item, parent);
    }

    public BetterAchievementsDemoAchievement(String id, String name, int column, int row, Block block, Achievement parent)
    {
        super(id, name, column, row, block, parent);
    }

    public BetterAchievementsDemoAchievement(String id, String name, int column, int row, ItemStack itemStack, Achievement parent)
    {
        super(id, name, column, row, itemStack, parent);
    }
}
